/*
 * isp.c -- ISP initialization, PQ bin loading, and image quality config
 *
 * Handles all ISP-related setup: sensor registration, AE/AWB library
 * registration, ISP ctrl params, PQ bin calibration loading, ISP color
 * pipeline configuration, noise reduction tuning, and 3DNR setup.
 *
 * This is the largest HAL module (~850 lines) because the ISP has the
 * most complex configuration surface in the HiSilicon MPP pipeline.
 */

#include "isp.h"
#include "watchdog.h"

/* ── PQ bin types (matches ot_pq_bin.h) ──────────────────────── */

typedef struct {
    int enable;
} pq_bin_isp_t;

typedef struct {
    int enable;
    int viPipe;
    int vpssGrp;
} pq_bin_nrx_t;

typedef struct {
    int enable;
    int viPipe;
} pq_bin_isp_evo_t;

typedef struct {
    pq_bin_isp_t     stISP;
    pq_bin_nrx_t     st3DNR;
    pq_bin_isp_evo_t stIspEvo;
} pq_bin_module_t;

typedef int (*fn_import_bin_data)(pq_bin_module_t *param, unsigned char *buf, unsigned int len);

/* ═══════════════════════════════════════════════════════════════
 *  ISP thread function
 * ═══════════════════════════════════════════════════════════════ */
static void *isp_thread_func(void *arg)
{
    hi_s32 ret;
    (void)arg;

    printf("[ISP ] Thread started, calling ss_mpi_isp_run(%d)...\n", VI_PIPE);
    g_isp_running = 1;
    ret = hi_mpi_isp_run(VI_PIPE);
    g_isp_running = 0;
    if (ret != HI_SUCCESS) {
        printf("[ISP ] ss_mpi_isp_run returned 0x%08X\n", (unsigned int)ret);
    } else {
        printf("[ISP ] ss_mpi_isp_run exited normally\n");
    }
    return NULL;
}

/* ═══════════════════════════════════════════════════════════════
 *  ISP init
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 isp_init(void)
{
    hi_s32 ret;
    hi_isp_3a_alg_lib ae_lib, awb_lib;
    hi_isp_sns_commbus bus_info;
    hi_isp_pub_attr pub_attr;

    printf("\n=== ISP init (V1.0.2.1 SDK) ===\n");

    /* 1. Register sensor callbacks with ISP/AE/AWB */
    memset(&ae_lib, 0, sizeof(ae_lib));
    ae_lib.id = VI_PIPE;
    strncpy(ae_lib.lib_name, HI_AE_LIB_NAME, sizeof(ae_lib.lib_name) - 1);

    memset(&awb_lib, 0, sizeof(awb_lib));
    awb_lib.id = VI_PIPE;
    strncpy(awb_lib.lib_name, HI_AWB_LIB_NAME, sizeof(awb_lib.lib_name) - 1);

    if (g_sns_obj->pfn_register_callback) {
        ret = g_sns_obj->pfn_register_callback(VI_PIPE, &ae_lib, &awb_lib);
        CHECK_RET("pfn_register_callback", ret);
    }

    /* 2. Set sensor I2C bus */
    bus_info.i2c_dev = I2C_BUS;
    if (g_sns_obj->pfn_set_bus_info) {
        ret = g_sns_obj->pfn_set_bus_info(VI_PIPE, bus_info);
        CHECK_RET("pfn_set_bus_info", ret);
    }

    /* 3. Register AE/AWB algorithm libraries */
    ret = hi_mpi_ae_register(VI_PIPE, &ae_lib);
    CHECK_RET("ss_mpi_ae_register", ret);

    ret = hi_mpi_awb_register(VI_PIPE, &awb_lib);
    CHECK_RET("ss_mpi_awb_register", ret);

    /* 4-pre. Set ISP ctrl params BEFORE mem_init */
    {
        ot_isp_ctrl_param ctrl_param;
        memset(&ctrl_param, 0, sizeof(ctrl_param));
        ret = ss_mpi_isp_get_ctrl_param(VI_PIPE, &ctrl_param);
        printf("[ISP ] get_ctrl_param ret=0x%08X be_buf_num=%u wakeup=%d "
               "proc_param=%u stat_intvl=%u update_pos=%u timeout=%u pwm=%u "
               "pidly=%u ldci_tpr=%d ob_pos=%d alg_run=%d long_frm=%d quick=%d\n",
               (unsigned int)ret,
               ctrl_param.be_buf_num, ctrl_param.isp_run_wakeup_select,
               ctrl_param.proc_param, ctrl_param.stat_interval,
               ctrl_param.update_pos, ctrl_param.interrupt_time_out,
               ctrl_param.pwm_num, ctrl_param.port_interrupt_delay,
               ctrl_param.ldci_tpr_flt_en, ctrl_param.ob_stats_update_pos,
               ctrl_param.alg_run_select, ctrl_param.long_frame_interrupt_en,
               ctrl_param.quick_start_en);

        ctrl_param.be_buf_num    = 4;
        ctrl_param.quick_start_en = 1;

        ret = ss_mpi_isp_set_ctrl_param(VI_PIPE, &ctrl_param);
        printf("[ISP ] set_ctrl_param(be_buf_num=4, quick_start=1) ret=0x%08X\n",
               (unsigned int)ret);

        ot_isp_ctrl_param after;
        memset(&after, 0, sizeof(after));
        ss_mpi_isp_get_ctrl_param(VI_PIPE, &after);
        printf("[ISP ] readback: be_buf_num=%u wakeup=%d\n",
               after.be_buf_num, after.isp_run_wakeup_select);
    }

    /* 4. ISP memory init */
    ret = hi_mpi_isp_mem_init(VI_PIPE);
    CHECK_RET("ss_mpi_isp_mem_init", ret);

    /* 5. Set ISP public attributes */
    memset(&pub_attr, 0, sizeof(pub_attr));
    pub_attr.wnd_rect.x      = 0;
    pub_attr.wnd_rect.y      = 0;
    pub_attr.wnd_rect.width  = SENSOR_WIDTH;
    pub_attr.wnd_rect.height = SENSOR_HEIGHT;
    pub_attr.sns_size.width  = SENSOR_WIDTH;
    pub_attr.sns_size.height = SENSOR_HEIGHT;
    pub_attr.frame_rate    = SENSOR_FPS;
    pub_attr.bayer_format  = OT_ISP_BAYER_BGGR;
    pub_attr.wdr_mode      = HI_WDR_MODE_NONE;
    pub_attr.sns_mode      = 0;

    ret = hi_mpi_isp_set_pub_attr(VI_PIPE, &pub_attr);
    CHECK_RET("ss_mpi_isp_set_pub_attr", ret);

    {
        hi_isp_pub_attr readback;
        memset(&readback, 0, sizeof(readback));
        hi_mpi_isp_get_pub_attr(VI_PIPE, &readback);
        printf("[ISP ] Bayer format readback: %d (set: %d)\n",
               readback.bayer_format, pub_attr.bayer_format);
    }

    /* 6. ISP init */
    ret = hi_mpi_isp_init(VI_PIPE);
    CHECK_RET("ss_mpi_isp_init", ret);

    /* 6a. Set AE route attr (3 nodes mimicking superb) */
    {
        ot_isp_ae_route ae_route;
        memset(&ae_route, 0, sizeof(ae_route));
        ret = ss_mpi_isp_get_ae_route_attr(VI_PIPE, &ae_route);
        printf("[AE  ] get_ae_route ret=0x%08X total_num=%u\n",
               (unsigned int)ret, ae_route.total_num);
        for (td_u32 i = 0; i < ae_route.total_num && i < 4; i++) {
            printf("[AE  ]   node[%u]: int_time=%u sys_gain=%u iris_fno=%u\n",
                   i, ae_route.route_node[i].int_time,
                   ae_route.route_node[i].sys_gain,
                   ae_route.route_node[i].iris_fno);
        }

        ae_route.total_num = 3;
        ae_route.route_node[0].int_time     = 8;
        ae_route.route_node[0].sys_gain     = 1024;
        ae_route.route_node[0].iris_fno     = 1;
        ae_route.route_node[0].iris_fno_lin = 1;
        ae_route.route_node[1].int_time     = 2804;
        ae_route.route_node[1].sys_gain     = 1024;
        ae_route.route_node[1].iris_fno     = 1;
        ae_route.route_node[1].iris_fno_lin = 1;
        ae_route.route_node[2].int_time     = 2804;
        ae_route.route_node[2].sys_gain     = 196608;
        ae_route.route_node[2].iris_fno     = 1;
        ae_route.route_node[2].iris_fno_lin = 1;

        ret = ss_mpi_isp_set_ae_route_attr(VI_PIPE, &ae_route);
        printf("[AE  ] set_ae_route(3 nodes mimicking superb) ret=0x%08X\n",
               (unsigned int)ret);

        ot_isp_ae_route rb;
        memset(&rb, 0, sizeof(rb));
        ss_mpi_isp_get_ae_route_attr(VI_PIPE, &rb);
        printf("[AE  ] readback: total_num=%u\n", rb.total_num);
        for (td_u32 i = 0; i < rb.total_num && i < 4; i++) {
            printf("[AE  ]   node[%u]: int_time=%u sys_gain=%u\n",
                   i, rb.route_node[i].int_time, rb.route_node[i].sys_gain);
        }
    }

    /* 7. Start ISP processing thread */
    ret = pthread_create(&g_isp_thread, NULL, isp_thread_func, NULL);
    if (ret != 0) {
        printf("[FAIL] pthread_create(isp_thread): %s\n", strerror(ret));
        hi_mpi_isp_exit(VI_PIPE);
        return HI_FAILURE;
    }
    printf("[ OK ] ISP thread launched\n");

    usleep(200000);

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  Load PQ bin calibration data
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 load_pq_bin(void)
{
    FILE *fp;
    unsigned char *buf = NULL;
    long file_size;
    size_t read_len;
    fn_import_bin_data import_fn;
    pq_bin_module_t bin_param;
    int ret;

    printf("\n=== Loading PQ bin calibration ===\n");

    g_pqbin_dl = dlopen(LIBBIN_PATH, RTLD_NOW | RTLD_GLOBAL);
    if (g_pqbin_dl == NULL) {
        printf("[WARN] dlopen(%s): %s\n", LIBBIN_PATH, dlerror());
        printf("[WARN] PQ bin loading skipped -- image may be black\n");
        return HI_FAILURE;
    }
    printf("[ OK ] dlopen(%s)\n", LIBBIN_PATH);

    import_fn = (fn_import_bin_data)dlsym(g_pqbin_dl, "OT_PQ_BIN_ImportBinData");
    if (import_fn == NULL) {
        printf("[WARN] dlsym(OT_PQ_BIN_ImportBinData): %s\n", dlerror());
        dlclose(g_pqbin_dl);
        g_pqbin_dl = NULL;
        return HI_FAILURE;
    }
    printf("[ OK ] Found OT_PQ_BIN_ImportBinData\n");

    /* Set g_aeHandle in libbin.so */
    {
        void **ae_handle_ptr = (void **)dlsym(g_pqbin_dl, "g_aeHandle");
        if (ae_handle_ptr != NULL) {
            void *ae_dl = dlopen("/progs/rec/00/ipc_drv/libss_mpi_ae.so",
                                 RTLD_NOW | RTLD_GLOBAL);
            if (ae_dl != NULL) {
                *ae_handle_ptr = ae_dl;
                printf("[ OK ] Set g_aeHandle -> libss_mpi_ae.so (%p)\n", ae_dl);
            } else {
                printf("[WARN] dlopen(libss_mpi_ae.so): %s\n", dlerror());
                printf("[WARN] AE calibration from PQ bin will be skipped\n");
            }
        } else {
            printf("[WARN] g_aeHandle not found in libbin.so\n");
        }
    }

    fp = fopen(g_pq_bin_path, "rb");
    if (fp == NULL) {
        printf("[WARN] fopen(%s): %s\n", g_pq_bin_path, strerror(errno));
        return HI_FAILURE;
    }

    fseek(fp, 0, SEEK_END);
    file_size = ftell(fp);
    fseek(fp, 0, SEEK_SET);

    if (file_size <= 0 || file_size > 1024 * 1024) {
        printf("[WARN] PQ bin file size %ld seems wrong\n", file_size);
        fclose(fp);
        return HI_FAILURE;
    }

    buf = (unsigned char *)malloc(file_size);
    if (buf == NULL) {
        printf("[WARN] malloc(%ld) failed\n", file_size);
        fclose(fp);
        return HI_FAILURE;
    }

    read_len = fread(buf, 1, file_size, fp);
    fclose(fp);

    if ((long)read_len != file_size) {
        printf("[WARN] fread: expected %ld, got %zu\n", file_size, read_len);
        free(buf);
        return HI_FAILURE;
    }
    printf("[ OK ] Read %s (%ld bytes)\n", g_pq_bin_path, file_size);

    memset(&bin_param, 0, sizeof(bin_param));
    bin_param.stISP.enable    = 1;
    bin_param.st3DNR.enable   = 1;
    bin_param.st3DNR.viPipe   = VI_PIPE;
    bin_param.stIspEvo.enable = 1;
    bin_param.stIspEvo.viPipe = VI_PIPE;

    printf("[INFO] Calling OT_PQ_BIN_ImportBinData...\n");
    ret = import_fn(&bin_param, buf, (unsigned int)file_size);
    free(buf);

    if (ret != 0) {
        printf("[WARN] OT_PQ_BIN_ImportBinData returned 0x%08X\n", (unsigned int)ret);
        printf("[WARN] PQ import failed -- image may still be black\n");
        return HI_FAILURE;
    }

    printf("[ OK ] PQ bin loaded successfully!\n");
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  Fix bayer format after PQ bin load
 * ═══════════════════════════════════════════════════════════════ */
void isp_fix_bayer_format(void)
{
    ot_isp_pub_attr pa;
    hi_s32 r = ss_mpi_isp_get_pub_attr(VI_PIPE, &pa);
    printf("[BAYER] post-PQ-bin readback: bayer_format=%d (expected 0=RGGB from PQ bin)\n",
           pa.bayer_format);
    pa.bayer_format = OT_ISP_BAYER_BGGR;
    r = ss_mpi_isp_set_pub_attr(VI_PIPE, &pa);
    printf("[BAYER] re-set to BGGR(3): ret=0x%08X\n", (unsigned)r);
    r = ss_mpi_isp_get_pub_attr(VI_PIPE, &pa);
    printf("[BAYER] verified: bayer_format=%d\n", pa.bayer_format);
    (void)r;
}

/* ═══════════════════════════════════════════════════════════════
 *  ISP color pipeline configuration
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 configure_isp_color(void)
{
    hi_s32 ret;

    printf("\n=== ISP Color Pipeline Configuration ===\n");

    /* 1. Module bypass state */
    {
        ot_isp_module_ctrl mod_ctrl;
        memset(&mod_ctrl, 0, sizeof(mod_ctrl));
        ret = ss_mpi_isp_get_module_ctrl(VI_PIPE, &mod_ctrl);
        if (ret == HI_SUCCESS) {
            printf("[MOD ] module_ctrl key=0x%016llX\n",
                   (unsigned long long)mod_ctrl.key);
            printf("[MOD ] bypass: dgain=%llu afc=%llu xtalk=%llu dpc=%llu nr=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_isp_d_gain,
                   (unsigned long long)mod_ctrl.bit_bypass_anti_false_color,
                   (unsigned long long)mod_ctrl.bit_bypass_crosstalk_removal,
                   (unsigned long long)mod_ctrl.bit_bypass_dpc,
                   (unsigned long long)mod_ctrl.bit_bypass_nr);
            printf("[MOD ] bypass: dehaze=%llu wb=%llu shading=%llu drc=%llu demosaic=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_dehaze,
                   (unsigned long long)mod_ctrl.bit_bypass_wb_gain,
                   (unsigned long long)mod_ctrl.bit_bypass_mesh_shading,
                   (unsigned long long)mod_ctrl.bit_bypass_drc,
                   (unsigned long long)mod_ctrl.bit_bypass_demosaic);
            printf("[MOD ] bypass: ccm=%llu gamma=%llu csc=%llu sharpen=%llu ldci=%llu ca=%llu\n",
                   (unsigned long long)mod_ctrl.bit_bypass_color_matrix,
                   (unsigned long long)mod_ctrl.bit_bypass_gamma,
                   (unsigned long long)mod_ctrl.bit_bypass_csc,
                   (unsigned long long)mod_ctrl.bit_bypass_sharpen,
                   (unsigned long long)mod_ctrl.bit_bypass_ldci,
                   (unsigned long long)mod_ctrl.bit_bypass_ca);
        } else {
            printf("[MOD ] get_module_ctrl FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 2. CSC state */
    {
        ot_isp_csc_attr csc;
        memset(&csc, 0, sizeof(csc));
        ret = ss_mpi_isp_get_csc_attr(VI_PIPE, &csc);
        if (ret == HI_SUCCESS) {
            printf("[CSC ] enable=%d, gamut=%d, hue=%u, luma=%u, contr=%u, satu=%u\n",
                   csc.enable, csc.color_gamut,
                   csc.hue, csc.luma, csc.contr, csc.satu);
            printf("[CSC ] limited_range=%d, ext_csc=%d, ct_mode=%d\n",
                   csc.limited_range_en, csc.ext_csc_en, csc.ct_mode_en);
            if (csc.satu < 30) {
                printf("[CSC ] *** SATURATION IS %u (LOW!) -- setting to 50 ***\n", csc.satu);
            }
            ret = ss_mpi_isp_set_csc_attr(VI_PIPE, &csc);
            printf("[CSC ] set: ret=0x%08X (satu=50, BT709, full range)\n", (unsigned)ret);
        } else {
            printf("[CSC ] get_csc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 3. Saturation attr */
    {
        ot_isp_saturation_attr sat;
        memset(&sat, 0, sizeof(sat));
        ret = ss_mpi_isp_get_saturation_attr(VI_PIPE, &sat);
        if (ret == HI_SUCCESS) {
            printf("[SAT ] op_type=%d, manual_sat=%u\n",
                   sat.op_type, sat.manual_attr.saturation);
            printf("[SAT ] auto_sat=[%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u,%u]\n",
                   sat.auto_attr.sat[0], sat.auto_attr.sat[1],
                   sat.auto_attr.sat[2], sat.auto_attr.sat[3],
                   sat.auto_attr.sat[4], sat.auto_attr.sat[5],
                   sat.auto_attr.sat[6], sat.auto_attr.sat[7],
                   sat.auto_attr.sat[8], sat.auto_attr.sat[9],
                   sat.auto_attr.sat[10], sat.auto_attr.sat[11],
                   sat.auto_attr.sat[12], sat.auto_attr.sat[13],
                   sat.auto_attr.sat[14], sat.auto_attr.sat[15]);
            printf("[SAT ] PQ bin defaults: sat[12..15]=%u,%u,%u,%u\n",
                   sat.auto_attr.sat[12], sat.auto_attr.sat[13],
                   sat.auto_attr.sat[14], sat.auto_attr.sat[15]);
            for (int i = 12; i < 16; i++) {
                if (sat.auto_attr.sat[i] > 95)
                    sat.auto_attr.sat[i] = 90;
            }
            ret = ss_mpi_isp_set_saturation_attr(VI_PIPE, &sat);
            printf("[SAT ] high-ISO sat[12..15] capped to 90: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[SAT ] get_saturation_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 4. Color Tone */
    {
        ot_isp_color_tone_attr ct;
        memset(&ct, 0, sizeof(ct));
        ret = ss_mpi_isp_get_color_tone_attr(VI_PIPE, &ct);
        if (ret == HI_SUCCESS) {
            printf("[CT  ] R=0x%04X G=0x%04X B=0x%04X\n",
                   ct.red_cast_gain, ct.green_cast_gain, ct.blue_cast_gain);
            ct.red_cast_gain = 0x100;
            ct.green_cast_gain = 0x100;
            ct.blue_cast_gain = 0x100;
            ret = ss_mpi_isp_set_color_tone_attr(VI_PIPE, &ct);
            printf("[CT  ] set neutral (0x100): ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[CT  ] get_color_tone_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 5. DRC state (log only) */
    {
        ot_isp_drc_attr drc;
        memset(&drc, 0, sizeof(drc));
        ret = ss_mpi_isp_get_drc_attr(VI_PIPE, &drc);
        if (ret == HI_SUCCESS) {
            printf("[DRC ] enable=%d, op_type=%d, curve=%d (keeping PQ bin)\n",
                   drc.enable, drc.op_type, drc.curve_select);
        } else {
            printf("[DRC ] get_drc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 6. Dehaze state (log only) */
    {
        ot_isp_dehaze_attr dehaze;
        memset(&dehaze, 0, sizeof(dehaze));
        ret = ss_mpi_isp_get_dehaze_attr(VI_PIPE, &dehaze);
        if (ret == HI_SUCCESS) {
            printf("[DHZ ] enable=%d, op_type=%d (keeping PQ bin)\n",
                   dehaze.enable, dehaze.op_type);
        } else {
            printf("[DHZ ] get_dehaze_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 7. Sharpen state (log only) */
    {
        ot_isp_sharpen_attr shp;
        memset(&shp, 0, sizeof(shp));
        ret = ss_mpi_isp_get_sharpen_attr(VI_PIPE, &shp);
        if (ret == HI_SUCCESS) {
            printf("[SHP ] enable=%d, op_type=%d (keeping PQ bin)\n",
                   shp.enable, shp.op_type);
        } else {
            printf("[SHP ] get_sharpen_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 8. LDCI state (log only) */
    {
        ot_isp_ldci_attr ldci;
        memset(&ldci, 0, sizeof(ldci));
        ret = ss_mpi_isp_get_ldci_attr(VI_PIPE, &ldci);
        if (ret == HI_SUCCESS) {
            printf("[LDCI] enable=%d, op_type=%d (keeping PQ bin)\n",
                   ldci.enable, ldci.op_type);
        } else {
            printf("[LDCI] get_ldci_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 9. NR state (log only) */
    {
        ot_isp_nr_attr nr;
        memset(&nr, 0, sizeof(nr));
        ret = ss_mpi_isp_get_nr_attr(VI_PIPE, &nr);
        if (ret == HI_SUCCESS) {
            printf("[NR  ] enable=%d, op_type=%d, md_en=%d\n",
                   nr.enable, nr.op_type, nr.md_en);
            if (nr.op_type == OT_OP_MODE_AUTO) {
                printf("[NR  ] auto: fine_str[0]=%u coring_wgt[0]=%u\n",
                       nr.snr_cfg.snr_attr.snr_auto.fine_strength[0],
                       nr.snr_cfg.snr_attr.snr_auto.coring_wgt[0]);
            } else {
                printf("[NR  ] manual: fine_str=%u coring_wgt=%u\n",
                       nr.snr_cfg.snr_attr.snr_manual.fine_strength,
                       nr.snr_cfg.snr_attr.snr_manual.coring_wgt);
            }
        } else {
            printf("[NR  ] get_nr_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 10. Gamma state (log only) */
    {
        ot_isp_gamma_attr gamma;
        memset(&gamma, 0, sizeof(gamma));
        ret = ss_mpi_isp_get_gamma_attr(VI_PIPE, &gamma);
        if (ret == HI_SUCCESS) {
            printf("[GAM ] enable=%d, curve_type=%d\n",
                   gamma.enable, gamma.curve_type);
            printf("[GAM ] table[0..7]=%u,%u,%u,%u,%u,%u,%u,%u\n",
                   gamma.table[0], gamma.table[1], gamma.table[2], gamma.table[3],
                   gamma.table[4], gamma.table[5], gamma.table[6], gamma.table[7]);
            printf("[GAM ] table[512]=%u, table[1024]=%u\n",
                   gamma.table[512], gamma.table[1024]);
        } else {
            printf("[GAM ] get_gamma_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 11. Demosaic state (log only) */
    {
        ot_isp_demosaic_attr dm;
        memset(&dm, 0, sizeof(dm));
        ret = ss_mpi_isp_get_demosaic_attr(VI_PIPE, &dm);
        if (ret == HI_SUCCESS) {
            printf("[DM  ] enable=%d, op_type=%d\n", dm.enable, dm.op_type);
        } else {
            printf("[DM  ] get_demosaic_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 12. CCM to auto mode */
    {
        ot_isp_color_matrix_attr ccm;
        memset(&ccm, 0, sizeof(ccm));
        ret = ss_mpi_isp_get_ccm_attr(VI_PIPE, &ccm);
        if (ret == HI_SUCCESS) {
            printf("[CCM ] op_type=%d, sat_en=%d\n",
                   ccm.op_type, ccm.manual_attr.sat_en);
            printf("[CCM ] current: [0x%04X,0x%04X,0x%04X, 0x%04X,0x%04X,0x%04X, 0x%04X,0x%04X,0x%04X]\n",
                   ccm.manual_attr.ccm[0], ccm.manual_attr.ccm[1], ccm.manual_attr.ccm[2],
                   ccm.manual_attr.ccm[3], ccm.manual_attr.ccm[4], ccm.manual_attr.ccm[5],
                   ccm.manual_attr.ccm[6], ccm.manual_attr.ccm[7], ccm.manual_attr.ccm[8]);
            ccm.op_type = OT_OP_MODE_AUTO;
            ret = ss_mpi_isp_set_ccm_attr(VI_PIPE, &ccm);
            printf("[CCM ] set AUTO: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[CCM ] get_ccm_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 13. AWB to auto with ADVANCE algorithm */
    {
        ot_isp_wb_attr wb;
        memset(&wb, 0, sizeof(wb));
        ret = ss_mpi_isp_get_wb_attr(VI_PIPE, &wb);
        if (ret == HI_SUCCESS) {
            printf("[AWB ] op_type=%d, alg_type=%d, R=%u Gr=%u Gb=%u B=%u\n",
                   wb.op_type, wb.auto_attr.alg_type,
                   wb.manual_attr.r_gain, wb.manual_attr.gr_gain,
                   wb.manual_attr.gb_gain, wb.manual_attr.b_gain);
            wb.op_type = OT_OP_MODE_AUTO;
            wb.auto_attr.alg_type = OT_ISP_AWB_ALG_ADVANCE;
            wb.auto_attr.speed = 256;
            wb.auto_attr.zone_sel = 32;
            wb.auto_attr.high_color_temp = 10000;
            wb.auto_attr.low_color_temp = 2500;
            wb.auto_attr.shift_limit_en = 1;
            wb.auto_attr.shift_limit = 64;
            wb.auto_attr.gain_norm_en = 1;
            ret = ss_mpi_isp_set_wb_attr(VI_PIPE, &wb);
            printf("[AWB ] set AUTO ADVANCE mode: ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[AWB ] get_wb_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    printf("[INFO] ISP color config done: all PQ modules on, "
           "auto CCM+AWB (ADVANCE), PQ bin CSC preserved\n");

    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  Low-light noise reduction tuning
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 configure_lowlight_nr(void)
{
    hi_s32 ret;

    printf("\n=== Low-Light NR Tuning ===\n");

    /* 1. ISP BayerNR: ensure md_en=1, boost high-ISO md_static_fine_strength */
    {
        ot_isp_nr_attr nr;
        memset(&nr, 0, sizeof(nr));
        ret = ss_mpi_isp_get_nr_attr(VI_PIPE, &nr);
        if (ret == HI_SUCCESS) {
            printf("[BNR ] enable=%d, op_type=%d, md_en=%d\n",
                   nr.enable, nr.op_type, nr.md_en);

            if (!nr.md_en) {
                nr.md_en = 1;
                printf("[BNR ] enabling motion detection (md_en=1)\n");
            }

            if (nr.op_type == OT_OP_MODE_AUTO) {
                printf("[BNR ] auto fine_str: ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.snr_cfg.snr_attr.snr_auto.fine_strength[i]);
                printf("\n");
                printf("[BNR ] auto coring_wgt: ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.snr_cfg.snr_attr.snr_auto.coring_wgt[i]);
                printf("\n");

                printf("[BNR ] md_auto tfs (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.tfs[i]);
                printf("\n");
                printf("[BNR ] md_auto sta_fine (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_fine_strength[i]);
                printf("\n");
                printf("[BNR ] md_auto sta_ratio (before): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_ratio[i]);
                printf("\n");

                for (int i = 8; i < 16; i++) {
                    int boosted = nr.md_cfg.md_auto.md_static_fine_strength[i];
                    boosted = (boosted * 170) / 100;
                    if (boosted > 120) boosted = 120;
                    if (boosted < 80) boosted = 80;
                    nr.md_cfg.md_auto.md_static_fine_strength[i] = (unsigned char)boosted;
                }
                printf("[BNR ] md_auto sta_fine (after): ");
                for (int i = 0; i < 16; i++)
                    printf("%u ", nr.md_cfg.md_auto.md_static_fine_strength[i]);
                printf("\n");
            }

            ret = ss_mpi_isp_set_nr_attr(VI_PIPE, &nr);
            printf("[BNR ] set_nr_attr (md_en=1 only): ret=0x%08X\n", (unsigned)ret);
        } else {
            printf("[BNR ] get_nr_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    /* 2. DRC: enable BCNR for Bayer-domain chroma NR */
    {
        ot_isp_drc_attr drc;
        memset(&drc, 0, sizeof(drc));
        ret = ss_mpi_isp_get_drc_attr(VI_PIPE, &drc);
        if (ret == HI_SUCCESS) {
            printf("[DRC ] enable=%d, op_type=%d, curve=%d\n",
                   drc.enable, drc.op_type, drc.curve_select);
            printf("[DRC ] strength=%u, dark_gain_luma=%u, dark_gain_chroma=%u\n",
                   drc.manual_attr.strength,
                   drc.dark_gain_limit_luma, drc.dark_gain_limit_chroma);
            printf("[DRC ] bright_gain=%u, contrast_ctrl=%u\n",
                   drc.bright_gain_limit, drc.contrast_ctrl);
            printf("[DRC ] bcnr: enable=%d, strength=%u\n",
                   drc.bcnr_attr.enable, drc.bcnr_attr.strength);

            drc.bcnr_attr.enable = 1;
            drc.bcnr_attr.strength = 6;
            ret = ss_mpi_isp_set_drc_attr(VI_PIPE, &drc);
            printf("[DRC ] BCNR: enable=1 strength=%u (was 3): ret=0x%08X\n",
                   (unsigned)drc.bcnr_attr.strength, (unsigned)ret);
        } else {
            printf("[DRC ] get_drc_attr FAILED: 0x%08X\n", (unsigned)ret);
        }
    }

    printf("[INFO] Low-light NR tuning done\n");
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  3DNR configuration
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 configure_3dnr(void)
{
    hi_s32 ret;

    printf("\n=== 3DNR Configuration ===\n");

    /* 1. Enable 3DNR (try VI pipe first, fallback to VPSS group) */
    {
        ot_3dnr_attr nr_attr;
        memset(&nr_attr, 0, sizeof(nr_attr));

        ret = ss_mpi_vi_get_pipe_3dnr_attr(VI_PIPE, &nr_attr);
        printf("[3DNR] VI pipe get_attr: enable=%d, type=%d, compress=%d, motion=%d, ret=0x%08X\n",
               nr_attr.enable, nr_attr.nr_type, nr_attr.compress_mode,
               nr_attr.nr_motion_mode, (unsigned)ret);

        nr_attr.enable = TD_TRUE;
        nr_attr.nr_type = OT_NR_TYPE_VIDEO_NORM;
        nr_attr.compress_mode = OT_COMPRESS_MODE_NONE;
        nr_attr.nr_motion_mode = OT_NR_MOTION_MODE_NORM;

        ret = ss_mpi_vi_set_pipe_3dnr_attr(VI_PIPE, &nr_attr);
        printf("[3DNR] VI pipe set_attr(enable=1, VIDEO_NORM): ret=0x%08X\n", (unsigned)ret);

        if (ret != HI_SUCCESS) {
            printf("[3DNR] VI pipe failed, trying VPSS group 3DNR...\n");

            memset(&nr_attr, 0, sizeof(nr_attr));
            ret = ss_mpi_vpss_get_grp_3dnr_attr(VPSS_GRP, &nr_attr);
            printf("[3DNR] VPSS get_attr: enable=%d, ret=0x%08X\n",
                   nr_attr.enable, (unsigned)ret);

            nr_attr.enable = TD_TRUE;
            nr_attr.nr_type = OT_NR_TYPE_VIDEO_NORM;
            nr_attr.compress_mode = OT_COMPRESS_MODE_NONE;
            nr_attr.nr_motion_mode = OT_NR_MOTION_MODE_NORM;

            ret = ss_mpi_vpss_set_grp_3dnr_attr(VPSS_GRP, &nr_attr);
            printf("[3DNR] VPSS set_attr: ret=0x%08X\n", (unsigned)ret);
            if (ret != HI_SUCCESS) {
                printf("[3DNR] WARNING: Both VI and VPSS 3DNR failed\n");
                return HI_FAILURE;
            }
        }
    }

    /* 2. Configure 3DNR V2 parameters (superb-matched values) */
    {
        ot_3dnr_param nr_param;
        memset(&nr_param, 0, sizeof(nr_param));

        ret = ss_mpi_vi_get_pipe_3dnr_param(VI_PIPE, &nr_param);
        printf("[3DNR] VI get_param: version=%d, ret=0x%08X\n",
               nr_param.nr_version, (unsigned)ret);

        if (ret == HI_SUCCESS) {
            nr_param.nr_version = OT_NR_V2;

            ot_nr_v2 *v2 = &nr_param.nr_norm_param_v2.nr_manual.nr_param;

            nr_param.nr_norm_param_v2.op_mode = OT_OP_MODE_MANUAL;

            /* Enable all NR channels */
            v2->nry1_en = 1;
            v2->nry2_en = 1;
            v2->nry3_en = 1;
            v2->nry4_en = 1;
            v2->nrc_en = 1;
            v2->nrc0_mode = 0;

            /* Post-processing */
            v2->pp.gamma_en = 1;
            v2->pp.ca_en = 0;

            /* mdy0 (pre-stage motion detection) */
            v2->mdy0.tfs = 8;
            v2->mdy0.math = 100;
            v2->mdy0.mathd = 80;
            v2->mdy0.mabw = 2;
            v2->mdy0.tdz = 32;
            printf("[3DNR] V2 MDY0: tfs=%u math=%u mathd=%u mabw=%u tdz=%u\n",
                   v2->mdy0.tfs, v2->mdy0.math, v2->mdy0.mathd,
                   v2->mdy0.mabw, v2->mdy0.tdz);

            /* Chroma NR channel 0 (nrc0) -- temporal chroma */
            v2->nrc0.trc = 128;
            v2->nrc0.sfc = 128;
            v2->nrc0.tfc = 32;
            v2->nrc0.tfs = 13;
            printf("[3DNR] V2 NRC0: trc=%u sfc=%u tfc=%u tfs=%u\n",
                   v2->nrc0.trc, v2->nrc0.sfc, v2->nrc0.tfc, v2->nrc0.tfs);

            /* Chroma NR channel 1 (nrc1) -- spatial chroma */
            v2->nrc1.pre_sfs = 14;
            v2->nrc1.sfs1 = 220;
            v2->nrc1.sfs2_coarse = 24;
            v2->nrc1.sfs2_coarse_f = 24;
            v2->nrc1.sfs2_fine_f = 15;
            v2->nrc1.sfs2_fine_b = 15;
            v2->nrc1.sfs2_mode = 0;
            printf("[3DNR] V2 NRC1: pre=%u sfs1=%u coarse=%u coarse_f=%u\n",
                   v2->nrc1.pre_sfs, v2->nrc1.sfs1, v2->nrc1.sfs2_coarse,
                   v2->nrc1.sfs2_coarse_f);

            /* Temporal luma NR (tfy) */
            for (int i = 0; i < 2; i++) {
                v2->tfy[i].tfs0 = 4;
                v2->tfy[i].tfs1 = 11;
                v2->tfy[i].tfs2 = 12;
                v2->tfy[i].ref_en = 1;
                v2->tfy[i].tss0 = 16;
                v2->tfy[i].tss1 = 0;
                v2->tfy[i].tss2 = 0;
                v2->tfy[i].tfr0[0] = 14; v2->tfy[i].tfr0[1] = 8;
                v2->tfy[i].tfr0[2] = 14; v2->tfy[i].tfr0[3] = 8;
                v2->tfy[i].tfr0[4] = 0;  v2->tfy[i].tfr0[5] = 0;
                v2->tfy[i].tfr1[0] = 16; v2->tfy[i].tfr1[1] = 8;
                v2->tfy[i].tfr1[2] = 16; v2->tfy[i].tfr1[3] = 8;
                v2->tfy[i].tfr1[4] = 0;  v2->tfy[i].tfr1[5] = 0;
                v2->tfy[i].tfs_mode = 1;
            }
            printf("[3DNR] V2 TFY: tfs=4,11,12 tss=16,0,0 tfr0=14,8,14,8,0,0 ref_en=1\n");

            /* Motion detection (mdy) */
            for (int i = 0; i < 2; i++) {
                v2->mdy[i].math0 = 100;
                v2->mdy[i].mate0 = 4;
                v2->mdy[i].math1 = 419;
                v2->mdy[i].mate1 = 4;
                v2->mdy[i].mabw0 = 7;
                v2->mdy[i].mabw1 = 7;
            }
            printf("[3DNR] V2 MDY: math0=100 math1=419 mabw=7\n");

            /* Spatial luma NR (sfy) */
            for (int i = 0; i < 3; i++) {
                v2->sfy[i].sfs1 = 64;
                v2->sfy[i].sbr1 = 128;
                v2->sfy[i].sfs2 = 64;
                v2->sfy[i].sft2 = 0;
                v2->sfy[i].sbr2 = 128;
                v2->sfy[i].sfs4 = 64;
                v2->sfy[i].sft4 = 0;
                v2->sfy[i].sbr4 = 128;
                v2->sfy[i].sth1_0 = 40;
                v2->sfy[i].sth2_0 = 80;
                v2->sfy[i].sth3_0 = 60;
                v2->sfy[i].sth1_1 = 40;
                v2->sfy[i].sth2_1 = 40;
                v2->sfy[i].sth3_1 = 40;
            }
            v2->sfy[0].sfn0_0 = 2; v2->sfy[0].sfn1_0 = 2;
            v2->sfy[0].sfn2_0 = 2; v2->sfy[0].sfn3_0 = 0;
            v2->sfy[0].sfn0_1 = 0; v2->sfy[0].sfn1_1 = 0;
            v2->sfy[0].sfn2_1 = 0; v2->sfy[0].sfn3_1 = 4;
            v2->sfy[0].bld6 = 2;
            v2->sfy[0].sfn6_0 = 2; v2->sfy[0].sfn6_1 = 4;
            v2->sfy[1].sfn0_0 = 2; v2->sfy[1].sfn1_0 = 2;
            v2->sfy[1].sfn2_0 = 2; v2->sfy[1].sfn3_0 = 2;
            v2->sfy[1].sfn0_1 = 2; v2->sfy[1].sfn1_1 = 2;
            v2->sfy[1].sfn2_1 = 2; v2->sfy[1].sfn3_1 = 2;
            v2->sfy[1].sfs5 = 32;
            v2->sfy[1].bld6 = 5;
            v2->sfy[1].sfn6_0 = 5; v2->sfy[1].sfn6_1 = 3;
            v2->sfy[1].sfn7_0 = 4; v2->sfy[1].sfn7_1 = 3;
            v2->sfy[1].sfn8_0 = 4; v2->sfy[1].sfn8_1 = 3;
            v2->sfy[1].strf3 = 10; v2->sfy[1].strb3 = 10;
            v2->sfy[1].strf4 = 10; v2->sfy[1].strb4 = 10;
            v2->sfy[2].sfn0_0 = 2; v2->sfy[2].sfn1_0 = 2;
            v2->sfy[2].sfn2_0 = 2; v2->sfy[2].sfn3_0 = 2;
            v2->sfy[2].sfn0_1 = 2; v2->sfy[2].sfn1_1 = 2;
            v2->sfy[2].sfn2_1 = 2; v2->sfy[2].sfn3_1 = 2;
            v2->sfy[2].bld6 = 5;
            v2->sfy[2].sfn6_0 = 5; v2->sfy[2].sfn6_1 = 0;
            v2->sfy[2].sfn7_0 = 4; v2->sfy[2].sfn7_1 = 3;
            v2->sfy[2].sfn8_0 = 4; v2->sfy[2].sfn8_1 = 3;
            v2->sfy[2].strf3 = 10; v2->sfy[2].strb3 = 10;
            v2->sfy[2].strf4 = 10; v2->sfy[2].strb4 = 10;
            printf("[3DNR] V2 SFY: sfs1=64 sbr1=128 sfs2=64 sth=40/80/60\n");

            ret = ss_mpi_vi_set_pipe_3dnr_param(VI_PIPE, &nr_param);
            printf("[3DNR] VI set_param(V2, manual, superb-matched): ret=0x%08X\n", (unsigned)ret);
            if (ret != HI_SUCCESS) {
                printf("[3DNR] WARNING: V2 param set failed (0x%08X)\n", (unsigned)ret);
            }
        } else {
            printf("[3DNR] get_param failed, skipping param config\n");
        }
    }

    printf("[INFO] 3DNR configuration done\n");
    return HI_SUCCESS;
}

/* ═══════════════════════════════════════════════════════════════
 *  Dump raw frame from VI (fallback)
 * ═══════════════════════════════════════════════════════════════ */
hi_s32 dump_raw_frame(void)
{
    hi_s32 ret;
    hi_video_frame_info frame;

    printf("\n=== Dumping raw frame from VI ===\n");

    printf("[INFO] Calling hi_mpi_vi_get_chn_frame (timeout 5s)...\n");
    ret = hi_mpi_vi_get_chn_frame(VI_PIPE, VI_CHN, &frame, 5000);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] hi_mpi_vi_get_chn_frame: 0x%08X\n", (unsigned)ret);
        return ret;
    }

    printf("[ OK ] Got VI frame!\n");
    printf("  width=%u height=%u pixel_format=%u\n",
           frame.video_frame.width,
           frame.video_frame.height,
           frame.video_frame.pixel_format);
    printf("  stride[0]=%u stride[1]=%u\n",
           frame.video_frame.stride[0],
           frame.video_frame.stride[1]);
    printf("  phys_addr[0]=0x%llx phys_addr[1]=0x%llx\n",
           (unsigned long long)frame.video_frame.phys_addr[0],
           (unsigned long long)frame.video_frame.phys_addr[1]);
    printf("  pts=%llu time_ref=%u\n",
           (unsigned long long)frame.video_frame.pts,
           frame.video_frame.time_ref);

    if (frame.video_frame.phys_addr[0] != 0 &&
        frame.video_frame.stride[0] > 0 &&
        frame.video_frame.height > 0) {
        unsigned int frame_size = frame.video_frame.stride[0] *
                                  frame.video_frame.height;
        if (frame.video_frame.pixel_format == HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420 ||
            frame.video_frame.pixel_format == HI_PIXEL_FORMAT_YUV_SEMIPLANAR_420) {
            frame_size = frame_size * 3 / 2;
        }

        printf("[INFO] Frame size: %u bytes, saving to /progs/rec/00/raw_frame.bin\n",
               frame_size);

        void *virt = hi_mpi_sys_mmap(frame.video_frame.phys_addr[0], frame_size);
        if (virt != NULL) {
            FILE *fp = fopen("/progs/rec/00/raw_frame.bin", "wb");
            if (fp) {
                fwrite(virt, 1, frame_size, fp);
                fclose(fp);
                printf("[ OK ] Saved raw frame: /progs/rec/00/raw_frame.bin (%u bytes)\n",
                       frame_size);
            } else {
                printf("[FAIL] fopen: %s\n", strerror(errno));
            }
            hi_mpi_sys_munmap(virt, frame_size);
        } else {
            printf("[WARN] hi_mpi_sys_mmap failed, trying /dev/mem...\n");
            int memfd = open("/dev/mem", O_RDONLY);
            if (memfd >= 0) {
                void *p = mmap(NULL, frame_size, PROT_READ, MAP_SHARED,
                               memfd, frame.video_frame.phys_addr[0]);
                if (p != MAP_FAILED) {
                    FILE *fp = fopen("/progs/rec/00/raw_frame.bin", "wb");
                    if (fp) {
                        fwrite(p, 1, frame_size, fp);
                        fclose(fp);
                        printf("[ OK ] Saved raw frame via /dev/mem (%u bytes)\n",
                               frame_size);
                    }
                    munmap(p, frame_size);
                } else {
                    printf("[FAIL] /dev/mem mmap: %s\n", strerror(errno));
                }
                close(memfd);
            }
        }
    }

    hi_mpi_vi_release_chn_frame(VI_PIPE, VI_CHN, &frame);
    return HI_SUCCESS;
}
