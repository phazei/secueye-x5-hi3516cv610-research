/*
 * audio.h -- Audio input (AI) and encoding (AENC) for G.711A
 */

#ifndef HAL_AUDIO_H
#define HAL_AUDIO_H

#include "../pipeline.h"

/* Initialize audio pipeline: acodec, AI device/channel, AENC G.711A, bind AI->AENC.
 * Non-fatal if it fails -- video continues without audio. */
hi_s32 audio_init(void);

/* Teardown audio pipeline: unbind, destroy channels, disable devices. */
void audio_deinit(void);

#endif /* HAL_AUDIO_H */
