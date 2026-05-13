/*
 * wdt_test.c -- Test watchdog feed mechanisms on Hi3516CV610
 *
 * Opens /dev/watchdog, tries SETTIMEOUT, then feeds via both ioctl
 * and write, printing return values. Runs for 60 seconds to confirm
 * the watchdog is actually being fed. If we survive 60s, feeding works.
 *
 * Usage: ./wdt_test
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <errno.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/ioctl.h>
#include <time.h>
#include <linux/watchdog.h>

int main(void)
{
    int fd, ret;
    int timeout = 0;
    struct watchdog_info info;
    time_t start;

    setvbuf(stdout, NULL, _IONBF, 0);

    printf("=== Watchdog Feed Test ===\n");

    /* Open watchdog */
    fd = open("/dev/watchdog", O_RDWR);
    if (fd < 0) {
        fd = open("/dev/watchdog", O_WRONLY);
    }
    if (fd < 0) {
        printf("FAIL: open(/dev/watchdog): %s\n", strerror(errno));
        return 1;
    }
    printf("Opened /dev/watchdog fd=%d\n", fd);

    /* Get identity */
    memset(&info, 0, sizeof(info));
    ret = ioctl(fd, WDIOC_GETSUPPORT, &info);
    if (ret == 0) {
        printf("Identity: %s\n", info.identity);
        printf("Options:  0x%08X\n", info.options);
        printf("  KEEPALIVEPING: %s\n", (info.options & WDIOF_KEEPALIVEPING) ? "YES" : "NO");
        printf("  SETTIMEOUT:    %s\n", (info.options & WDIOF_SETTIMEOUT) ? "YES" : "NO");
        printf("  MAGICCLOSE:    %s\n", (info.options & WDIOF_MAGICCLOSE) ? "YES" : "NO");
        printf("Firmware ver: %u\n", info.firmware_version);
    } else {
        printf("WDIOC_GETSUPPORT: failed errno=%d (%s)\n", errno, strerror(errno));
    }

    /* Get current timeout */
    ret = ioctl(fd, WDIOC_GETTIMEOUT, &timeout);
    printf("GETTIMEOUT: ret=%d, timeout=%d\n", ret, timeout);

    /* Set timeout to 120s */
    int new_timeout = 120;
    ret = ioctl(fd, WDIOC_SETTIMEOUT, &new_timeout);
    printf("SETTIMEOUT(120): ret=%d, actual=%d, errno=%d\n", ret, new_timeout, ret ? errno : 0);

    /* Verify */
    timeout = 0;
    ret = ioctl(fd, WDIOC_GETTIMEOUT, &timeout);
    printf("GETTIMEOUT after set: ret=%d, timeout=%d\n", ret, timeout);

    /* Get time left */
    int timeleft = 0;
    ret = ioctl(fd, WDIOC_GETTIMELEFT, &timeleft);
    printf("GETTIMELEFT: ret=%d, timeleft=%d, errno=%d\n", ret, timeleft, ret ? errno : 0);

    /* Now test feeding for 60 seconds */
    printf("\n=== Feeding test (60 seconds) ===\n");
    printf("If we survive 60s, the feed method works.\n");
    printf("Feeding every 1 second...\n\n");

    /* First, let's try SETTIMEOUT again to "reset" the countdown.
     * Some HiSilicon drivers reset the timer on SETTIMEOUT but not on KEEPALIVE. */
    printf("=== Feed test (timeout=30s, run 45s) ===\n");
    printf("If we survive >35s, the feed works.\n");
    printf("If we die at ~30s, nothing is resetting the timer.\n\n");

    /* Set a short timeout so we know quickly if feed works */
    int short_timeout = 30;
    ret = ioctl(fd, WDIOC_SETTIMEOUT, &short_timeout);
    printf("SETTIMEOUT(%d): ret=%d actual=%d\n", 30, ret, short_timeout);

    start = time(NULL);
    int count = 0;
    while (time(NULL) - start < 45) {
        int elapsed = (int)(time(NULL) - start);

        /* Try multiple feed methods */

        /* Method 1: ioctl KEEPALIVE with argument */
        int dummy = 0;
        ret = ioctl(fd, WDIOC_KEEPALIVE, &dummy);
        int e1 = errno;

        /* Method 2: write a single byte */
        ssize_t w1 = write(fd, "\x00", 1);
        int e2 = errno;

        /* Method 3: write "V" (magic close char -- but don't close) */
        /* Skip this -- it might disarm the watchdog */

        /* Method 4: SETTIMEOUT to reset timer */
        int reset_timeout = 30;
        int r4 = ioctl(fd, WDIOC_SETTIMEOUT, &reset_timeout);
        int e4 = errno;

        /* Method 5: write multiple bytes */
        ssize_t w5 = write(fd, "feed", 4);
        int e5 = errno;

        count++;
        if (count <= 10 || count % 10 == 0) {
            printf("[%3ds] ioctl_keepalive=%d(e=%d) write1=%zd(e=%d) settimeout=%d(e=%d) write4=%zd(e=%d)\n",
                   elapsed, ret, ret ? e1 : 0,
                   w1, w1 <= 0 ? e2 : 0,
                   r4, r4 ? e4 : 0,
                   w5, w5 <= 0 ? e5 : 0);
        }
        sleep(1);
    }

    printf("\n=== SURVIVED 45 SECONDS with 30s timeout -- feed works! ===\n");

    /* Disarm with magic close */
    write(fd, "V", 1);
    close(fd);
    printf("Watchdog disarmed and closed.\n");

    return 0;
}
