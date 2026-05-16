/*
 * isp.h -- ISP initialization, PQ bin loading, and image quality config
 */

#ifndef HAL_ISP_H
#define HAL_ISP_H

#include "../pipeline.h"

/* Initialize ISP: register sensor/AE/AWB, set ctrl params, mem init,
 * pub attr, isp init, AE route, launch ISP thread. */
hi_s32 isp_init(void);

/* Load PQ bin calibration data via libbin.so.
 * Must be called AFTER isp_init() and BEFORE capture. */
hi_s32 load_pq_bin(void);

/* Re-set bayer_format to BGGR after PQ bin load (PQ bin overrides to RGGB) */
void isp_fix_bayer_format(void);

/* Configure ISP color pipeline: CSC, saturation, color tone, CCM, AWB.
 * Call AFTER load_pq_bin() and AFTER ISP thread is running. */
hi_s32 configure_isp_color(void);

/* Low-light noise reduction tuning: BayerNR md_en, DRC BCNR.
 * Call AFTER configure_isp_color(). */
hi_s32 configure_lowlight_nr(void);

/* 3DNR configuration (temporal + spatial + chroma NR).
 * Call AFTER vpss_init() -- VPSS group must exist, or uses VI pipe 3DNR. */
hi_s32 configure_3dnr(void);

/* Dump a raw frame from VI (fallback if ISP/VPSS fails) */
hi_s32 dump_raw_frame(void);

#endif /* HAL_ISP_H */
