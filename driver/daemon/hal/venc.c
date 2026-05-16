/*
 * venc.c -- VENC H.265 channel setup and streaming loop
 *
 * Creates a VBR H.265 encode channel matching superb's config:
 * 3840x2160 @ 15fps (from 20fps sensor, upscaled via VPSS ext chn 3),
 * 4Mbps max, QP 35-44.
 * Handles both file capture and RTSP streaming with audio mux.
 */

#include "venc.h"
#include "watchdog.h"

/* ═══════════════════════════════════════════════════════════════
 *  VENC H.265 channel init
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 venc_init(void)
{
    hi_s32 ret;
    ot_venc_chn_attr chn_attr;
    ot_venc_start_param start_param;
    hi_mpp_chn src_chn, dst_chn;

    printf("\n=== VENC H.265 init ===\n");

    /* Extra cleanup: superb's VENC state may persist in kernel */
    ss_mpi_venc_stop_chn(VENC_CHN);
    ss_mpi_venc_destroy_chn(VENC_CHN);
    ss_mpi_venc_stop_chn(1);
    ss_mpi_venc_destroy_chn(1);

    memset(&chn_attr, 0, sizeof(chn_attr));

    chn_attr.venc_attr.type           = OT_PT_H265;
    chn_attr.venc_attr.max_pic_width  = ENCODE_WIDTH;
    chn_attr.venc_attr.max_pic_height = ENCODE_HEIGHT;
    chn_attr.venc_attr.pic_width      = ENCODE_WIDTH;
    chn_attr.venc_attr.pic_height     = ENCODE_HEIGHT;
    /* H.265 buf_size: SDK uses w*h*3/4 aligned to 64 */
    chn_attr.venc_attr.buf_size       = (ENCODE_WIDTH * ENCODE_HEIGHT * 3 / 4 + 63) & ~63;
    chn_attr.venc_attr.is_by_frame    = HI_TRUE;
    chn_attr.venc_attr.profile        = 0;  /* main profile */

    chn_attr.venc_attr.h265_attr.frame_buf_ratio = 100;
    chn_attr.venc_attr.h265_attr.rcn_ref_share_buf_en = HI_FALSE;

    /* VBR at ~4Mbps max, sensor 20fps -> encode 15fps */
    chn_attr.rc_attr.rc_mode = OT_VENC_RC_MODE_H265_VBR;
    chn_attr.rc_attr.h265_vbr.gop           = 15;
    chn_attr.rc_attr.h265_vbr.stats_time    = 1;
    chn_attr.rc_attr.h265_vbr.src_frame_rate = 20;
    chn_attr.rc_attr.h265_vbr.dst_frame_rate = 15;
    chn_attr.rc_attr.h265_vbr.max_bit_rate  = 4096;

    chn_attr.gop_attr.gop_mode = OT_VENC_GOP_MODE_NORMAL_P;
    chn_attr.gop_attr.normal_p.ip_qp_delta = 2;

    printf("[INFO] VENC H265 chn_attr: sizeof=%zu, type=%u, %ux%u, buf=%u, gop=%u, max_br=%u\n",
           sizeof(chn_attr), chn_attr.venc_attr.type,
           chn_attr.venc_attr.pic_width, chn_attr.venc_attr.pic_height,
           chn_attr.venc_attr.buf_size,
           chn_attr.rc_attr.h265_vbr.gop,
           chn_attr.rc_attr.h265_vbr.max_bit_rate);

    ret = ss_mpi_venc_create_chn(VENC_CHN, &chn_attr);
    CHECK_RET("ss_mpi_venc_create_chn(H265)", ret);

    /* VBR QP limits (superb: minQP=35, maxQP=44) */
    {
        ot_venc_rc_param rc_param;
        memset(&rc_param, 0, sizeof(rc_param));
        rc_param.h265_vbr_param.max_qp = 44;
        rc_param.h265_vbr_param.min_qp = 35;
        rc_param.h265_vbr_param.max_i_qp = 44;
        rc_param.h265_vbr_param.min_i_qp = 35;
        rc_param.h265_vbr_param.max_reencode_times = 3;
        rc_param.h265_vbr_param.max_i_proportion = 100;
        rc_param.h265_vbr_param.min_i_proportion = 1;
        rc_param.h265_vbr_param.qpmap_en = HI_FALSE;
        hi_s32 rc_ret = ss_mpi_venc_set_rc_param(VENC_CHN, &rc_param);
        printf("[INFO] VBR QP limits set: max_qp=44 min_qp=35 ret=0x%08X\n", (unsigned)rc_ret);
    }

    /* Start VENC channel (continuous receive) */
    start_param.recv_pic_num = -1;
    ret = ss_mpi_venc_start_chn(VENC_CHN, &start_param);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_venc_start_chn: 0x%08X\n", (unsigned)ret);
        ss_mpi_venc_destroy_chn(VENC_CHN);
        return ret;
    }
    printf("[ OK ] ss_mpi_venc_start_chn\n");

    /* Bind VPSS ext channel 3 (3840x2160) -> VENC */
    src_chn.mod_id = HI_ID_VPSS;
    src_chn.dev_id = VPSS_GRP;
    src_chn.chn_id = VPSS_EXT_CHN;
    dst_chn.mod_id = HI_ID_VENC;
    dst_chn.dev_id = 0;
    dst_chn.chn_id = VENC_CHN;

    ret = hi_mpi_sys_bind(&src_chn, &dst_chn);
    CHECK_RET("hi_mpi_sys_bind(VPSS_EXT3->VENC)", ret);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  Capture / streaming loop
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 capture_h265(void)
{
    hi_s32 ret;
    hi_s32 venc_fd;
    fd_set read_fds;
    struct timeval timeout_val;
    ot_venc_chn_status stat;
    ot_venc_stream stream;
    FILE *fp = NULL;
    hi_u32 total_bytes = 0;
    int frames_saved = 0;

    if (g_rtsp_mode) {
        printf("\n=== RTSP streaming mode ===\n");
    } else {
        printf("\n=== Capturing H.265 bitstream ===\n");
    }

    /* Let ISP/AE stabilize (12s with watchdog feeding) */
    printf("[INFO] Waiting 12s for ISP/AE/AWB to stabilize...\n");
    for (int stab_i = 0; stab_i < 12; stab_i++) {
        sleep(1);
        watchdog_feed();
    }

    /* Readback sensor exposure registers via I2C */
    if (g_sns_obj && g_sns_obj->pfn_read_reg) {
        int e0 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E00);
        int e1 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E01);
        int e2 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E02);
        int ag_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E08);
        int ag_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E09);
        int dg_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E06);
        int dg_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E07);
        int vts_h = g_sns_obj->pfn_read_reg(VI_PIPE, 0x320E);
        int vts_l = g_sns_obj->pfn_read_reg(VI_PIPE, 0x320F);
        unsigned int exp_val = ((e0 & 0x0F) << 12) | (e1 << 4) | ((e2 >> 4) & 0x0F);
        unsigned int vts_val = (vts_h << 8) | vts_l;
        printf("[I2C] exp=0x%X (%u half-lines), VTS=0x%X (%u)\n",
               exp_val, exp_val, vts_val, vts_val);
        printf("[I2C] again: coarse=0x%02X fine=0x%02X | dgain: coarse=0x%02X fine=0x%02X\n",
               ag_c, ag_f, dg_c, dg_f);
    }

    /* Query ISP AE + AWB convergence */
    {
        ot_isp_exp_info exp_info;
        ot_isp_wb_info wb_info;

        memset(&exp_info, 0, sizeof(exp_info));
        hi_s32 qret = ss_mpi_isp_query_exposure_info(VI_PIPE, &exp_info);
        if (qret == HI_SUCCESS) {
            printf("[AE ] exp_time=%u, exposure=%u, ave_lum=%u\n",
                   exp_info.exp_time, exp_info.exposure, exp_info.ave_lum);
            printf("[AE ] a_gain=%u, d_gain=%u, isp_d_gain=%u, iso=%u\n",
                   exp_info.a_gain, exp_info.d_gain, exp_info.isp_d_gain,
                   exp_info.iso);
            printf("[AE ] fps=%u, hist_error=%d, exposure_is_max=%u\n",
                   exp_info.fps, (int)exp_info.hist_error,
                   exp_info.exposure_is_max);
        } else {
            printf("[AE ] query failed: 0x%08X\n", (unsigned)qret);
        }

        memset(&wb_info, 0, sizeof(wb_info));
        qret = ss_mpi_isp_query_wb_info(VI_PIPE, &wb_info);
        if (qret == HI_SUCCESS) {
            printf("[AWB] r_gain=%u, gr_gain=%u, gb_gain=%u, b_gain=%u\n",
                   wb_info.r_gain, wb_info.gr_gain,
                   wb_info.gb_gain, wb_info.b_gain);
            printf("[AWB] color_temp=%u, saturation=%u\n",
                   wb_info.color_temp, wb_info.saturation);
            printf("[AWB] ccm=[%u,%u,%u, %u,%u,%u, %u,%u,%u]\n",
                   wb_info.ccm[0], wb_info.ccm[1], wb_info.ccm[2],
                   wb_info.ccm[3], wb_info.ccm[4], wb_info.ccm[5],
                   wb_info.ccm[6], wb_info.ccm[7], wb_info.ccm[8]);
        } else {
            printf("[AWB] query failed: 0x%08X\n", (unsigned)qret);
        }
    }

    venc_fd = ss_mpi_venc_get_fd(VENC_CHN);
    if (venc_fd < 0) {
        printf("[FAIL] ss_mpi_venc_get_fd: %d\n", venc_fd);
        return HI_FAILURE;
    }
    printf("[ OK ] VENC fd = %d\n", venc_fd);

    /* Get AENC fd if audio enabled */
    hi_s32 aenc_fd = -1;
    if (g_audio_enabled) {
        aenc_fd = ss_mpi_aenc_get_fd(AENC_CHN);
        if (aenc_fd < 0) {
            printf("[WARN] ss_mpi_aenc_get_fd failed: %d -- audio disabled\n", aenc_fd);
            g_audio_enabled = 0;
        } else {
            printf("[ OK ] AENC fd = %d\n", aenc_fd);
        }
    }

    /* Start RTSP server if in streaming mode */
    if (g_rtsp_mode) {
        int rtsp_ret;
        if (g_audio_enabled) {
            rtsp_ret = rtsp_start_with_audio(g_rtsp_ip, g_rtsp_port);
        } else {
            rtsp_ret = rtsp_start(g_rtsp_ip, g_rtsp_port);
        }
        if (rtsp_ret != 0) {
            printf("[FAIL] RTSP server failed to start\n");
            return HI_FAILURE;
        }
    }

    if (!g_rtsp_mode) {
        fp = fopen(OUTPUT_FILE, "wb");
        if (fp == NULL) {
            printf("[FAIL] fopen(%s): %s\n", OUTPUT_FILE, strerror(errno));
            return HI_FAILURE;
        }
    }

    int audio_frames_pushed = 0;

    while (!g_stop) {
        if (!g_rtsp_mode && frames_saved >= CAPTURE_FRAMES)
            break;

        FD_ZERO(&read_fds);
        FD_SET(venc_fd, &read_fds);
        int max_fd = venc_fd;

        if (g_audio_enabled && aenc_fd >= 0) {
            FD_SET(aenc_fd, &read_fds);
            if (aenc_fd > max_fd) max_fd = aenc_fd;
        }

        timeout_val.tv_sec  = 5;
        timeout_val.tv_usec = 0;

        ret = select(max_fd + 1, &read_fds, NULL, NULL, &timeout_val);
        if (ret <= 0) {
            watchdog_feed();
            if (g_rtsp_mode) continue;
            printf("[WARN] select returned %d after %d frames\n", ret, frames_saved);
            break;
        }

        /* Handle audio data (AENC) */
        if (g_audio_enabled && aenc_fd >= 0 && FD_ISSET(aenc_fd, &read_fds)) {
            ot_audio_stream aenc_stream;
            memset(&aenc_stream, 0, sizeof(aenc_stream));

            hi_s32 aret = ss_mpi_aenc_get_stream(AENC_CHN, &aenc_stream, 0);
            if (aret == HI_SUCCESS) {
                if (g_rtsp_mode && aenc_stream.len > AENC_G711A_HDR_SIZE) {
                    unsigned char *audio_data = (unsigned char *)aenc_stream.stream + AENC_G711A_HDR_SIZE;
                    unsigned int audio_len = aenc_stream.len - AENC_G711A_HDR_SIZE;
                    rtsp_push_audio_stream(audio_data, audio_len);
                    audio_frames_pushed++;
                }
                ss_mpi_aenc_release_stream(AENC_CHN, &aenc_stream);
            }
        }

        /* Handle video data (VENC) */
        if (!FD_ISSET(venc_fd, &read_fds))
            continue;

        ret = ss_mpi_venc_query_status(VENC_CHN, &stat);
        if (ret != HI_SUCCESS || stat.cur_packs == 0) {
            if (!g_rtsp_mode)
                printf("[WARN] query_status: ret=0x%08X, cur_packs=%u\n",
                       (unsigned)ret, stat.cur_packs);
            continue;
        }

        stream.pack = (ot_venc_pack *)malloc(sizeof(ot_venc_pack) * stat.cur_packs);
        if (stream.pack == NULL) break;
        stream.pack_cnt = stat.cur_packs;

        ret = ss_mpi_venc_get_stream(VENC_CHN, &stream, -1);
        if (ret != HI_SUCCESS) {
            free(stream.pack);
            if (!g_rtsp_mode)
                printf("[WARN] get_stream: 0x%08X\n", (unsigned)ret);
            continue;
        }

        if (g_rtsp_mode) {
            rtsp_push_venc_stream(&stream);
        }

        if (fp) {
            for (hi_u32 i = 0; i < stream.pack_cnt; i++) {
                hi_u8 *data = stream.pack[i].addr + stream.pack[i].offset;
                hi_u32 len  = stream.pack[i].len - stream.pack[i].offset;
                fwrite(data, 1, len, fp);
                total_bytes += len;
            }
        }

        ss_mpi_venc_release_stream(VENC_CHN, &stream);
        free(stream.pack);
        frames_saved++;

        watchdog_feed();

        if (g_rtsp_mode && (frames_saved % 20 == 0)) {
            fprintf(stderr, "[VENC] frame=%d, audio_frames=%d\n",
                    frames_saved, audio_frames_pushed);
        }

        /* Periodic AE/AWB status */
        {
            int interval = g_rtsp_mode ? 300 : 50;
            if (frames_saved % interval == 0) {
                ot_isp_exp_info ei;
                ot_isp_wb_info wi;
                memset(&ei, 0, sizeof(ei));
                memset(&wi, 0, sizeof(wi));
                ss_mpi_isp_query_exposure_info(VI_PIPE, &ei);
                ss_mpi_isp_query_wb_info(VI_PIPE, &wi);
                printf("[F%d] AE: exp=%u, again=%u, dgain=%u, lum=%u, iso=%u | "
                       "AWB: R=%u G=%u B=%u ct=%u\n",
                       frames_saved, ei.exp_time, ei.a_gain, ei.d_gain,
                       ei.ave_lum, ei.iso,
                       wi.r_gain, wi.gr_gain, wi.b_gain, wi.color_temp);
            }
        }
    }

    if (fp) fclose(fp);
    if (g_rtsp_mode) rtsp_stop();

    /* Final I2C readback */
    if (g_sns_obj && g_sns_obj->pfn_read_reg) {
        int e0 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E00);
        int e1 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E01);
        int e2 = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E02);
        int ag_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E08);
        int ag_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E09);
        int dg_c = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E06);
        int dg_f = g_sns_obj->pfn_read_reg(VI_PIPE, 0x3E07);
        unsigned int exp_val = ((e0 & 0x0F) << 12) | (e1 << 4) | ((e2 >> 4) & 0x0F);
        printf("[I2C-END] exp=%u, again=0x%02X/0x%02X, dgain=0x%02X/0x%02X\n",
               exp_val, ag_c, ag_f, dg_c, dg_f);
    }

    if (g_rtsp_mode) {
        printf("[ OK ] RTSP streaming ended after %d video frames, %d audio frames\n",
               frames_saved, audio_frames_pushed);
        return HI_SUCCESS;
    } else if (frames_saved > 0) {
        printf("[ OK ] Saved H.265: %s (%u bytes, %d frames)\n",
               OUTPUT_FILE, total_bytes, frames_saved);
        return HI_SUCCESS;
    } else {
        printf("[FAIL] No H.265 frames captured\n");
        return HI_FAILURE;
    }
}
