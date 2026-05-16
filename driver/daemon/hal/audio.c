/*
 * audio.c -- Audio input (AI) and encoding (AENC) for G.711A
 *
 * Initializes the internal audio codec (/dev/acodec), configures
 * AI device/channel, creates AENC channel for G.711A, and binds AI->AENC.
 *
 * Reference: shumjj dev_aenc.cpp, SDK sample_comm_audio.c
 */

#include "audio.h"

hi_s32 audio_init(void)
{
    hi_s32 ret;
    int fd_acodec;
    ot_aio_attr aio_attr;
    ot_aenc_chn_attr aenc_attr;
    hi_mpp_chn src_chn, dst_chn;

    printf("\n=== Audio Init (G.711A, 8kHz, mono) ===\n");

    /* Step 1: Initialize audio subsystem */
    ss_mpi_audio_exit();
    ret = ss_mpi_audio_init();
    if (ret != HI_SUCCESS) {
        printf("[WARN] ss_mpi_audio_init: 0x%08X (non-fatal, may already be init)\n",
               (unsigned)ret);
    } else {
        printf("[ OK ] ss_mpi_audio_init\n");
    }

    /* Step 2: Configure AI device attributes */
    memset(&aio_attr, 0, sizeof(aio_attr));
    aio_attr.sample_rate      = AUDIO_SAMPLE_RATE;
    aio_attr.bit_width        = OT_AUDIO_BIT_WIDTH_16;
    aio_attr.work_mode        = OT_AIO_MODE_I2S_MASTER;
    aio_attr.snd_mode         = OT_AUDIO_SOUND_MODE_MONO;
    aio_attr.expand_flag      = 0;
    aio_attr.frame_num        = 5;
    aio_attr.point_num_per_frame = AUDIO_PTNUMPERFRM;
    aio_attr.chn_cnt          = 1;
    aio_attr.clk_share        = 1;
    aio_attr.i2s_type         = OT_AIO_I2STYPE_INNERCODEC;

    ret = ss_mpi_ai_set_pub_attr(AI_DEV, &aio_attr);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_ai_set_pub_attr: 0x%08X\n", (unsigned)ret);
        return ret;
    }
    printf("[ OK ] ss_mpi_ai_set_pub_attr (8kHz/16bit/mono)\n");

    /* Step 3: Enable AI device and channel */
    ret = ss_mpi_ai_enable(AI_DEV);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_ai_enable: 0x%08X\n", (unsigned)ret);
        return ret;
    }
    printf("[ OK ] ss_mpi_ai_enable\n");

    ret = ss_mpi_ai_enable_chn(AI_DEV, AI_CHN);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_ai_enable_chn: 0x%08X\n", (unsigned)ret);
        ss_mpi_ai_disable(AI_DEV);
        return ret;
    }
    printf("[ OK ] ss_mpi_ai_enable_chn\n");

    /* Step 4: Configure internal audio codec */
    fd_acodec = open(ACODEC_FILE, O_RDWR);
    if (fd_acodec < 0) {
        printf("[FAIL] open(%s): %s\n", ACODEC_FILE, strerror(errno));
        printf("[WARN] Audio codec not available -- audio will not work\n");
        ss_mpi_ai_disable_chn(AI_DEV, AI_CHN);
        ss_mpi_ai_disable(AI_DEV);
        return HI_FAILURE;
    }

    /* 4a: Soft reset */
    ret = ioctl(fd_acodec, OT_ACODEC_SOFT_RESET_CTRL);
    if (ret != 0) {
        printf("[WARN] OT_ACODEC_SOFT_RESET_CTRL failed: %s\n", strerror(errno));
    } else {
        printf("[ OK ] acodec soft reset\n");
    }

    /* 4b: Set I2S sample rate to 8kHz */
    {
        unsigned int i2s_fs = OT_ACODEC_FS_8000;
        ret = ioctl(fd_acodec, OT_ACODEC_SET_I2S1_FS, &i2s_fs);
        if (ret != 0) {
            printf("[WARN] OT_ACODEC_SET_I2S1_FS failed: %s\n", strerror(errno));
        } else {
            printf("[ OK ] acodec I2S1 FS = 8000Hz\n");
        }
    }

    /* 4c: Set input to pseudo-differential (built-in microphone) */
    {
        unsigned int input_mode = OT_ACODEC_MIXER_IN_D;
        ret = ioctl(fd_acodec, OT_ACODEC_SET_MIXER_MIC, &input_mode);
        if (ret != 0) {
            printf("[WARN] OT_ACODEC_SET_MIXER_MIC failed: %s\n", strerror(errno));
        } else {
            printf("[ OK ] acodec mixer = pseudo-differential (IN_D)\n");
        }
    }

    /* 4d: Set input volume (mic gain) */
    {
        unsigned int input_vol = (unsigned int)g_mic_gain;
        ret = ioctl(fd_acodec, OT_ACODEC_SET_INPUT_VOLUME, &input_vol);
        if (ret != 0) {
            printf("[WARN] OT_ACODEC_SET_INPUT_VOLUME failed: %s\n", strerror(errno));
        } else {
            printf("[ OK ] acodec input volume = %u dB\n", input_vol);
        }
    }

    close(fd_acodec);

    /* Step 5: Create AENC channel for G.711A */
    ot_aenc_attr_g711 g711_attr;
    memset(&g711_attr, 0, sizeof(g711_attr));

    memset(&aenc_attr, 0, sizeof(aenc_attr));
    aenc_attr.type = OT_PT_G711A;
    aenc_attr.buf_size = 30;
    aenc_attr.point_num_per_frame = AUDIO_PTNUMPERFRM;
    aenc_attr.value = &g711_attr;

    ret = ss_mpi_aenc_create_chn(AENC_CHN, &aenc_attr);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_aenc_create_chn: 0x%08X\n", (unsigned)ret);
        ss_mpi_ai_disable_chn(AI_DEV, AI_CHN);
        ss_mpi_ai_disable(AI_DEV);
        return ret;
    }
    printf("[ OK ] ss_mpi_aenc_create_chn (G.711A)\n");

    /* Step 6: Bind AI -> AENC */
    src_chn.mod_id = OT_ID_AI;
    src_chn.dev_id = AI_DEV;
    src_chn.chn_id = AI_CHN;
    dst_chn.mod_id = OT_ID_AENC;
    dst_chn.dev_id = 0;
    dst_chn.chn_id = AENC_CHN;

    ret = ss_mpi_sys_bind(&src_chn, &dst_chn);
    if (ret != HI_SUCCESS) {
        printf("[FAIL] ss_mpi_sys_bind(AI->AENC): 0x%08X\n", (unsigned)ret);
        ss_mpi_aenc_destroy_chn(AENC_CHN);
        ss_mpi_ai_disable_chn(AI_DEV, AI_CHN);
        ss_mpi_ai_disable(AI_DEV);
        return ret;
    }
    printf("[ OK ] ss_mpi_sys_bind(AI->AENC)\n");

    g_audio_enabled = 1;
    printf("[ OK ] Audio pipeline ready: mic -> AI -> AENC(G.711A) -> RTSP\n");
    return HI_SUCCESS;
}

void audio_deinit(void)
{
    hi_mpp_chn src_chn, dst_chn;

    if (!g_audio_enabled) return;

    printf("[INFO] Audio deinit...\n");

    src_chn.mod_id = OT_ID_AI;
    src_chn.dev_id = AI_DEV;
    src_chn.chn_id = AI_CHN;
    dst_chn.mod_id = OT_ID_AENC;
    dst_chn.dev_id = 0;
    dst_chn.chn_id = AENC_CHN;
    ss_mpi_sys_unbind(&src_chn, &dst_chn);

    ss_mpi_aenc_destroy_chn(AENC_CHN);

    ss_mpi_ai_disable_chn(AI_DEV, AI_CHN);
    ss_mpi_ai_disable(AI_DEV);

    ss_mpi_audio_exit();

    g_audio_enabled = 0;
    printf("[ OK ] Audio deinit complete\n");
}
