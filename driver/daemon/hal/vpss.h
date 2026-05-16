/*
 * vpss.h -- VPSS group and channel setup
 */

#ifndef HAL_VPSS_H
#define HAL_VPSS_H

#include "../pipeline.h"

/* Initialize VPSS: create group, configure channel, bind VI->VPSS */
hi_s32 vpss_init(void);

#endif /* HAL_VPSS_H */
