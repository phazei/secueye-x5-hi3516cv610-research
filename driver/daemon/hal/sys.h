/*
 * sys.h -- System init, sensor driver loading, MIPI RX
 */

#ifndef HAL_SYS_H
#define HAL_SYS_H

#include "../pipeline.h"

/* Load sensor driver .so via dlopen, resolve g_sns_obj */
hi_s32 load_sensor_driver(void);

/* System init: tear down superb state, configure VB pools, init sys */
hi_s32 sys_init(void);

/* MIPI RX init: configure lanes, clocks, device attributes for SC635HAI */
hi_s32 mipi_init(void);

#endif /* HAL_SYS_H */
