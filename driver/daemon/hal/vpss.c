/*
 * vpss.c -- VPSS group and channel setup
 *
 * Uses V1.0.2.1 SDK APIs. The SDK internally manages /dev/vpss fds.
 */

#include "vpss.h"

hi_s32 vpss_init(void)
{
    hi_s32 ret;
    ot_vpss_grp_attr grp_attr;
    ot_vpss_chn_attr chn_attr;

    printf("\n=== VPSS init (V1.0.2.1 SDK) ===\n");

    /* Create + configure group */
    memset(&grp_attr, 0, sizeof(grp_attr));
    grp_attr.max_width      = SENSOR_WIDTH;
    grp_attr.max_height     = SENSOR_HEIGHT;
    grp_attr.dynamic_range  = OT_DYNAMIC_RANGE_SDR8;
    grp_attr.pixel_format   = OT_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    grp_attr.dei_mode       = OT_VPSS_DEI_MODE_OFF;
    grp_attr.frame_rate.src_frame_rate = -1;
    grp_attr.frame_rate.dst_frame_rate = -1;

    ret = ss_mpi_vpss_create_grp(VPSS_GRP, &grp_attr);
    CHECK_RET("ss_mpi_vpss_create_grp", ret);

    ret = ss_mpi_vpss_start_grp(VPSS_GRP);
    CHECK_RET("ss_mpi_vpss_start_grp", ret);

    /* Configure channel */
    memset(&chn_attr, 0, sizeof(chn_attr));
    chn_attr.mirror_en      = HI_FALSE;
    chn_attr.flip_en        = HI_FALSE;
    chn_attr.border_en      = HI_FALSE;
    chn_attr.width          = SENSOR_WIDTH;
    chn_attr.height         = SENSOR_HEIGHT;
    chn_attr.depth          = 0;
    chn_attr.chn_mode       = OT_VPSS_CHN_MODE_USER;
    chn_attr.video_format   = OT_VIDEO_FORMAT_LINEAR;
    chn_attr.dynamic_range  = OT_DYNAMIC_RANGE_SDR8;
    chn_attr.pixel_format   = OT_PIXEL_FORMAT_YVU_SEMIPLANAR_420;
    chn_attr.compress_mode  = OT_COMPRESS_MODE_NONE;
    chn_attr.frame_rate.src_frame_rate = -1;
    chn_attr.frame_rate.dst_frame_rate = -1;

    ret = ss_mpi_vpss_set_chn_attr(VPSS_GRP, VPSS_CHN, &chn_attr);
    CHECK_RET("ss_mpi_vpss_set_chn_attr", ret);

    ret = ss_mpi_vpss_enable_chn(VPSS_GRP, VPSS_CHN);
    CHECK_RET("ss_mpi_vpss_enable_chn", ret);

    /* Bind VI -> VPSS */
    hi_mpp_chn src_chn, dst_chn;
    src_chn.mod_id = HI_ID_VI;
    src_chn.dev_id = VI_PIPE;
    src_chn.chn_id = VI_CHN;

    dst_chn.mod_id = HI_ID_VPSS;
    dst_chn.dev_id = VPSS_GRP;
    dst_chn.chn_id = 0;

    ret = hi_mpi_sys_bind(&src_chn, &dst_chn);
    CHECK_RET("hi_mpi_sys_bind(VI->VPSS)", ret);

    return HI_SUCCESS;
}
