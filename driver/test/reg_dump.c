/*
 * SC635HAI Register Dump Utility
 *
 * Reads register ranges from the running sensor (works while superb is
 * streaming -- I2C is shared). Dumps in {addr, value} format suitable
 * for copy-paste into init tables.
 *
 * Usage: reg_dump <start_hex> <end_hex>
 * Example: ./reg_dump 0x23b0 0x23c9
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/ioctl.h>
#include <linux/i2c-dev.h>

#define I2C_SLAVE_FORCE 0x0706
#define SC635HAI_I2C_ADDR 0x30

static int g_fd = -1;

static int i2c_read_reg(unsigned short reg) {
    unsigned char buf[2];
    buf[0] = (reg >> 8) & 0xFF;
    buf[1] = reg & 0xFF;
    if (write(g_fd, buf, 2) != 2) return -1;
    if (read(g_fd, buf, 1) != 1) return -1;
    return buf[0];
}

int main(int argc, char *argv[]) {
    unsigned int start, end;
    int i;

    if (argc != 3) {
        printf("Usage: %s <start_hex> <end_hex>\n", argv[0]);
        printf("  e.g. %s 0x23b0 0x23c9\n", argv[0]);
        return 1;
    }
    start = strtoul(argv[1], NULL, 16);
    end   = strtoul(argv[2], NULL, 16);

    g_fd = open("/dev/i2c-0", O_RDWR);
    if (g_fd < 0) { perror("open"); return 1; }
    if (ioctl(g_fd, I2C_SLAVE_FORCE, SC635HAI_I2C_ADDR) < 0) {
        perror("I2C_SLAVE_FORCE"); close(g_fd); return 1;
    }

    printf("/* SC635HAI register dump 0x%04x..0x%04x */\n", start, end);
    for (i = (int)start; i <= (int)end; i++) {
        int v = i2c_read_reg((unsigned short)i);
        if (v < 0) {
            printf("    { 0x%04X, 0x?? },  /* read error */\n", i);
        } else {
            printf("    { 0x%04X, 0x%02X },\n", i, v);
        }
    }

    close(g_fd);
    return 0;
}
