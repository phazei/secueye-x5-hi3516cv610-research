/*
 * venc.h -- VENC H.265 channel setup and streaming loop
 */

#ifndef HAL_VENC_H
#define HAL_VENC_H

#include "../pipeline.h"

/* Initialize VENC: create H.265 channel, set RC params, bind VPSS->VENC */
hi_s32 venc_init(void);

/* Main capture/streaming loop.
 * File mode: captures CAPTURE_FRAMES then returns.
 * RTSP mode: runs until g_stop is set (SIGINT/SIGTERM).
 * Handles both video (VENC) and audio (AENC) multiplexing. */
hi_s32 capture_h265(void);

#endif /* HAL_VENC_H */
