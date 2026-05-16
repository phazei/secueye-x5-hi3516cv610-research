/*
 * watchdog.c -- Hardware watchdog interface
 *
 * The HiSilicon ot_wdt driver on Hi3516CV610 does NOT support
 * WDIOC_KEEPALIVE or write() for feeding -- both return EPERM.
 * The only way to reset the countdown is WDIOC_SETTIMEOUT, which
 * resets the hardware countdown as a side effect.
 */

#include "../pipeline.h"
#include "watchdog.h"
#include <linux/watchdog.h>

void watchdog_open(void)
{
    g_watchdog_fd = open("/dev/watchdog", O_RDWR);
    if (g_watchdog_fd < 0) {
        g_watchdog_fd = open("/dev/watchdog", O_WRONLY);
    }
    if (g_watchdog_fd < 0) {
        printf("[WDT ] open(/dev/watchdog): %s (non-fatal)\n", strerror(errno));
        return;
    }

    int timeout = 0;
    if (ioctl(g_watchdog_fd, WDIOC_GETTIMEOUT, &timeout) == 0) {
        printf("[WDT ] timeout = %d seconds\n", timeout);
    } else {
        printf("[WDT ] WDIOC_GETTIMEOUT failed (errno=%d), timeout unknown\n", errno);
    }

    int new_timeout = 120;
    if (ioctl(g_watchdog_fd, WDIOC_SETTIMEOUT, &new_timeout) == 0) {
        printf("[WDT ] timeout extended to %d seconds\n", new_timeout);
    } else {
        printf("[WDT ] WDIOC_SETTIMEOUT failed (errno=%d) -- using default\n", errno);
    }

    ioctl(g_watchdog_fd, WDIOC_KEEPALIVE, NULL);

    printf("[ OK ] Watchdog opened (fd=%d) -- we are now feeding it\n", g_watchdog_fd);
}

void watchdog_feed(void)
{
    if (g_watchdog_fd >= 0) {
        int timeout = 120;
        ioctl(g_watchdog_fd, WDIOC_SETTIMEOUT, &timeout);
    }
}

void watchdog_close(void)
{
    if (g_watchdog_fd >= 0) {
        write(g_watchdog_fd, "V", 1);
        close(g_watchdog_fd);
        g_watchdog_fd = -1;
        printf("[ OK ] Watchdog disarmed (magic close)\n");
    }
}
