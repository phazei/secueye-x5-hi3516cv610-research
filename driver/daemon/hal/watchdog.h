/*
 * watchdog.h -- Hardware watchdog interface
 */

#ifndef HAL_WATCHDOG_H
#define HAL_WATCHDOG_H

/* Open /dev/watchdog and take over feeding from superb.
 * Sets timeout to 120s. Non-fatal if watchdog unavailable. */
void watchdog_open(void);

/* Feed the watchdog (re-set timeout to prevent SoC reset).
 * Uses WDIOC_SETTIMEOUT as the ot_wdt driver doesn't support
 * WDIOC_KEEPALIVE or write(). */
void watchdog_feed(void);

/* Cleanly disarm the watchdog (magic close 'V') so mySystem
 * can take it back after we exit. */
void watchdog_close(void);

#endif /* HAL_WATCHDOG_H */
