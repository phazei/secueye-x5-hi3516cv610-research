/*
 * vi.h -- Video Input (VI) device, pipe, and channel setup
 */

#ifndef HAL_VI_H
#define HAL_VI_H

#include "../pipeline.h"

/* Raw VI ioctl commands (fallback if B040 library fails) */
#define VI_IOC_REG_DEV        0x40044961
#define VI_IOC_SET_DEV_ATTR   0x40784900
#define VI_IOC_ENABLE_DEV     0x00004902
#define VI_IOC_DISABLE_DEV    0x00004903
#define VI_IOC_BIND           0x4004490a
#define VI_IOC_UNBIND         0x4004490b
#define VI_IOC_WDR_FUSION     0x401c490d
#define VI_IOC_SET_PIPE_ATTR  0x40204910
#define VI_IOC_START_PIPE     0x0000491e
#define VI_IOC_STOP_PIPE      0x0000491f
#define VI_IOC_PIPE_FREQ      0x4004494d
#define VI_IOC_PIPE_CFG       0x40084939
#define VI_IOC_SET_CHN_ATTR   0x402c494e
#define VI_IOC_ENABLE_CHN     0x00004952
#define VI_IOC_DISABLE_CHN    0x00004953
#define VI_IOC_ISP_ALGO_CFG   0x40104914

/* Helper macro for raw ioctl approach */
#define W32(buf, off, val) do { \
    unsigned int _v = (unsigned int)(val); \
    memcpy((buf) + (off), &_v, 4); \
} while (0)

/* Initialize VI: tries B040 library first, falls back to raw ioctls.
 * Sets up VI device, pipe, and channel for SC635HAI. */
hi_s32 vi_init(void);

/* Teardown VI resources (called from main teardown).
 * Handles both B040 library and raw ioctl cleanup paths. */
void vi_deinit(void);

#endif /* HAL_VI_H */
