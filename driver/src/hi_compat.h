/*
 * hi_compat.h - B040 hi_ to V1.0.2.1 ot_/td_/ss_mpi_ compatibility layer
 *
 * The B040 SDK used hi_ type names and hi_mpi_ API names as aliases for
 * the underlying ot_ types and ot_mpi_ APIs. The V1.0.2.1 SDK dropped all
 * hi_ aliases and renamed APIs to ss_mpi_. This header bridges the gap.
 */

#ifndef __HI_COMPAT_H__
#define __HI_COMPAT_H__

#include "ot_type.h"
#include "ot_common.h"
#include "ot_common_vi.h"
#include "ot_common_vpss.h"
#include "ot_common_venc.h"
#include "ot_common_isp.h"
#include "ot_common_video.h"
#include "ot_mipi_rx.h"

/* ── Scalar types ───────────────────────────────────────────────── */
typedef td_s32   hi_s32;
typedef td_u32   hi_u32;
typedef td_s64   hi_s64;
typedef td_u64   hi_u64;
typedef td_u16   hi_u16;
typedef td_u8    hi_u8;
typedef td_bool  hi_bool;
typedef td_void  hi_void;
typedef td_float hi_float;
typedef td_char  hi_char;

/* ── Constants ──────────────────────────────────────────────────── */
#define HI_SUCCESS    TD_SUCCESS
#define HI_FAILURE    TD_FAILURE
#define HI_NULL       TD_NULL
#define HI_TRUE       TD_TRUE
#define HI_FALSE      TD_FALSE

/* ── Module IDs ─────────────────────────────────────────────────── */
#define HI_ID_VI      OT_ID_VI
#define HI_ID_VPSS    OT_ID_VPSS
#define HI_ID_VENC    OT_ID_VENC
#define HI_ID_VO      OT_ID_VO

/* ── VB types ───────────────────────────────────────────────────── */
typedef ot_vb_cfg          hi_vb_cfg;
typedef ot_vb_pool         hi_vb_pool;
typedef ot_vb_blk          hi_vb_blk;

/* ── SYS types ──────────────────────────────────────────────────── */
typedef ot_vi_vpss_mode    hi_vi_vpss_mode;
typedef ot_mpp_chn         hi_mpp_chn;

/* ── VI types ───────────────────────────────────────────────────── */
typedef ot_vi_dev_attr     hi_vi_dev_attr;
typedef ot_vi_pipe_attr    hi_vi_pipe_attr;
typedef ot_vi_chn_attr     hi_vi_chn_attr;
typedef ot_vi_wdr_fusion_grp_attr hi_vi_wdr_fusion_grp_attr;

/* ── ISP types ──────────────────────────────────────────────────── */
typedef ot_isp_pub_attr    hi_isp_pub_attr;
typedef ot_isp_sns_obj     hi_isp_sns_obj;
typedef ot_isp_3a_alg_lib  hi_isp_3a_alg_lib;
typedef ot_isp_sns_commbus hi_isp_sns_commbus;

/* ── VPSS types ─────────────────────────────────────────────────── */
typedef ot_vpss_grp_attr   hi_vpss_grp_attr;
typedef ot_vpss_chn_attr   hi_vpss_chn_attr;

/* ── VENC types ─────────────────────────────────────────────────── */
typedef ot_venc_chn_attr     hi_venc_chn_attr;
typedef ot_venc_stream       hi_venc_stream;
typedef ot_venc_pack         hi_venc_pack;
typedef ot_venc_chn_status   hi_venc_chn_status;
typedef ot_venc_start_param  hi_venc_start_param;

/* ── Video types ────────────────────────────────────────────────── */
typedef ot_video_frame_info hi_video_frame_info;

/* ── VI enums ───────────────────────────────────────────────────── */
#define HI_VI_INTF_MODE_MIPI          OT_VI_INTF_MODE_MIPI
#define HI_VI_WORK_MODE_MULTIPLEX_1   OT_VI_WORK_MODE_MULTIPLEX_1
#define HI_VI_SCAN_PROGRESSIVE        OT_VI_SCAN_PROGRESSIVE
#define HI_VI_DATA_TYPE_RAW           OT_VI_DATA_TYPE_RAW
#define HI_VI_PIPE_BYPASS_NONE        OT_VI_PIPE_BYPASS_NONE
#define HI_VI_OFFLINE_VPSS_OFFLINE    OT_VI_OFFLINE_VPSS_OFFLINE
#define HI_VI_OFFLINE_VPSS_ONLINE     OT_VI_OFFLINE_VPSS_ONLINE
#define HI_VI_ONLINE_VPSS_OFFLINE     OT_VI_ONLINE_VPSS_OFFLINE
#define HI_VI_ONLINE_VPSS_ONLINE      OT_VI_ONLINE_VPSS_ONLINE

/* ── Data rate enums ────────────────────────────────────────────── */
#define HI_DATA_RATE_X1               OT_DATA_RATE_X1
#define HI_DATA_RATE_X2               OT_DATA_RATE_X2

/* ── Pixel format enums ─────────────────────────────────────────── */
#define HI_PIXEL_FORMAT_RGB_BAYER_10BPP   OT_PIXEL_FORMAT_RGB_BAYER_10BPP
#define HI_PIXEL_FORMAT_RGB_BAYER_12BPP   OT_PIXEL_FORMAT_RGB_BAYER_12BPP
#define HI_PIXEL_FORMAT_YVU_SEMIPLANAR_420 OT_PIXEL_FORMAT_YVU_SEMIPLANAR_420
#define HI_PIXEL_FORMAT_YUV_SEMIPLANAR_420 OT_PIXEL_FORMAT_YUV_SEMIPLANAR_420

/* ── Compress/video/dynamic range enums ─────────────────────────── */
#define HI_COMPRESS_MODE_NONE         OT_COMPRESS_MODE_NONE
#define HI_VIDEO_FORMAT_LINEAR        OT_VIDEO_FORMAT_LINEAR
#define HI_DYNAMIC_RANGE_SDR8         OT_DYNAMIC_RANGE_SDR8

/* ── VPSS enums ─────────────────────────────────────────────────── */
#define HI_VPSS_DEI_MODE_OFF          OT_VPSS_DEI_MODE_OFF
#define HI_VPSS_CHN_MODE_AUTO         OT_VPSS_CHN_MODE_AUTO

/* ── WDR enums ──────────────────────────────────────────────────── */
#define HI_WDR_MODE_NONE              OT_WDR_MODE_NONE

/* ── ISP enums ──────────────────────────────────────────────────── */
#define HI_ISP_BAYER_RGGB             OT_ISP_BAYER_RGGB
#define HI_ISP_BAYER_BGGR             OT_ISP_BAYER_BGGR

/* ── VENC enums ─────────────────────────────────────────────────── */
#define HI_PT_JPEG                    OT_PT_JPEG
#define HI_PT_H264                    OT_PT_H264
#define HI_PT_H265                    OT_PT_H265

/* ── AE/AWB library names ───────────────────────────────────────── */
#define HI_AE_LIB_NAME               OT_AE_LIB_NAME
#define HI_AWB_LIB_NAME              OT_AWB_LIB_NAME

/* ── MIPI RX enums ──────────────────────────────────────────────── */
#define HI_MIPI_SET_HS_MODE           OT_MIPI_SET_HS_MODE
#define HI_MIPI_ENABLE_SENSOR_CLOCK   OT_MIPI_ENABLE_SENSOR_CLOCK
#define HI_MIPI_RESET_SENSOR          OT_MIPI_RESET_SENSOR
#define HI_MIPI_UNRESET_SENSOR        OT_MIPI_UNRESET_SENSOR
#define HI_MIPI_ENABLE_MIPI_CLOCK     OT_MIPI_ENABLE_MIPI_CLOCK
#define HI_MIPI_RESET_MIPI            OT_MIPI_RESET_MIPI
#define HI_MIPI_UNRESET_MIPI          OT_MIPI_UNRESET_MIPI
#define HI_MIPI_SET_DEV_ATTR          OT_MIPI_SET_DEV_ATTR
#define HI_MIPI_SET_PHY_CMVMODE       OT_MIPI_SET_PHY_CMVMODE
#define HI_MIPI_WDR_MODE_NONE         OT_MIPI_WDR_MODE_NONE

/* ── MPI API mappings ───────────────────────────────────────────── */
/* SYS */
#define hi_mpi_sys_init                ss_mpi_sys_init
#define hi_mpi_sys_exit                ss_mpi_sys_exit
#define hi_mpi_sys_bind                ss_mpi_sys_bind
#define hi_mpi_sys_unbind              ss_mpi_sys_unbind
#define hi_mpi_sys_set_vi_vpss_mode    ss_mpi_sys_set_vi_vpss_mode
#define hi_mpi_sys_get_vi_vpss_mode    ss_mpi_sys_get_vi_vpss_mode

/* VB */
#define hi_mpi_vb_init                 ss_mpi_vb_init
#define hi_mpi_vb_exit                 ss_mpi_vb_exit
#define hi_mpi_vb_set_cfg              ss_mpi_vb_set_cfg

/* VI */
#define hi_mpi_vi_set_dev_attr         ss_mpi_vi_set_dev_attr
#define hi_mpi_vi_get_dev_attr         ss_mpi_vi_get_dev_attr
#define hi_mpi_vi_enable_dev           ss_mpi_vi_enable_dev
#define hi_mpi_vi_disable_dev          ss_mpi_vi_disable_dev
#define hi_mpi_vi_bind_pipe            ss_mpi_vi_bind
#define hi_mpi_vi_bind                 ss_mpi_vi_bind
#define hi_mpi_vi_unbind_pipe          ss_mpi_vi_unbind
#define hi_mpi_vi_unbind               ss_mpi_vi_unbind
#define hi_mpi_vi_set_wdr_fusion_grp_attr ss_mpi_vi_set_wdr_fusion_grp_attr
#define hi_mpi_vi_create_pipe          ss_mpi_vi_create_pipe
#define hi_mpi_vi_destroy_pipe         ss_mpi_vi_destroy_pipe
#define hi_mpi_vi_set_pipe_attr        ss_mpi_vi_set_pipe_attr
#define hi_mpi_vi_start_pipe           ss_mpi_vi_start_pipe
#define hi_mpi_vi_stop_pipe            ss_mpi_vi_stop_pipe
#define hi_mpi_vi_set_chn_attr         ss_mpi_vi_set_chn_attr
#define hi_mpi_vi_enable_chn           ss_mpi_vi_enable_chn
#define hi_mpi_vi_disable_chn          ss_mpi_vi_disable_chn
#define hi_mpi_vi_get_chn_frame        ss_mpi_vi_get_chn_frame
#define hi_mpi_vi_release_chn_frame    ss_mpi_vi_release_chn_frame

/* VPSS */
#define hi_mpi_vpss_create_grp         ss_mpi_vpss_create_grp
#define hi_mpi_vpss_destroy_grp        ss_mpi_vpss_destroy_grp
#define hi_mpi_vpss_start_grp          ss_mpi_vpss_start_grp
#define hi_mpi_vpss_stop_grp           ss_mpi_vpss_stop_grp
#define hi_mpi_vpss_set_chn_attr       ss_mpi_vpss_set_chn_attr
#define hi_mpi_vpss_enable_chn         ss_mpi_vpss_enable_chn
#define hi_mpi_vpss_disable_chn        ss_mpi_vpss_disable_chn
#define hi_mpi_vpss_get_chn_frame      ss_mpi_vpss_get_chn_frame
#define hi_mpi_vpss_release_chn_frame  ss_mpi_vpss_release_chn_frame
#define hi_mpi_vpss_set_grp_attr       ss_mpi_vpss_set_grp_attr

/* VENC */
#define hi_mpi_venc_create_chn         ss_mpi_venc_create_chn
#define hi_mpi_venc_destroy_chn        ss_mpi_venc_destroy_chn
#define hi_mpi_venc_start_chn          ss_mpi_venc_start_chn
#define hi_mpi_venc_stop_chn           ss_mpi_venc_stop_chn
#define hi_mpi_venc_send_frame         ss_mpi_venc_send_frame
#define hi_mpi_venc_get_stream         ss_mpi_venc_get_stream
#define hi_mpi_venc_release_stream     ss_mpi_venc_release_stream
#define hi_mpi_venc_get_chn_attr       ss_mpi_venc_get_chn_attr
#define hi_mpi_venc_get_fd             ss_mpi_venc_get_fd

/* ISP */
#define hi_mpi_isp_init                ss_mpi_isp_init
#define hi_mpi_isp_exit                ss_mpi_isp_exit
#define hi_mpi_isp_run                 ss_mpi_isp_run
#define hi_mpi_isp_mem_init            ss_mpi_isp_mem_init
#define hi_mpi_isp_set_pub_attr        ss_mpi_isp_set_pub_attr
#define hi_mpi_isp_get_pub_attr        ss_mpi_isp_get_pub_attr
#define hi_mpi_isp_sensor_reg_callback   ss_mpi_isp_sensor_reg_callback
#define hi_mpi_isp_sensor_unreg_callback ss_mpi_isp_sensor_unreg_callback

/* AE */
#define hi_mpi_ae_register             ss_mpi_ae_register
#define hi_mpi_ae_unregister           ss_mpi_ae_unregister
#define hi_mpi_ae_sensor_reg_callback    ss_mpi_ae_sensor_reg_callback
#define hi_mpi_ae_sensor_unreg_callback  ss_mpi_ae_sensor_unreg_callback

/* AWB */
#define hi_mpi_awb_register            ss_mpi_awb_register
#define hi_mpi_awb_unregister          ss_mpi_awb_unregister
#define hi_mpi_awb_sensor_reg_callback   ss_mpi_awb_sensor_reg_callback
#define hi_mpi_awb_sensor_unreg_callback ss_mpi_awb_sensor_unreg_callback

/* SYS MEM */
#define hi_mpi_sys_mmap                ss_mpi_sys_mmap
#define hi_mpi_sys_munmap              ss_mpi_sys_munmap

#endif /* __HI_COMPAT_H__ */
