#!/bin/sh
# debug.sh -- persistent boot script on configfs (jffs2, survives reboot+factory reset)
# Called by startup.sh INSTEAD of launching superb directly.
# We add remote access, then launch superb ourselves.
#
# Deployed to: /etc/conf.d/debug.sh (on camera configfs partition)
# See CAMERA.md "SD Card Jailbreak" for documentation.
#
# This file is the repo copy. The live copy is on the camera's configfs.
# To deploy: upload this file to /etc/conf.d/debug.sh on the camera.

DRVDIR=/progs/rec/00/ipc_drv
SDMOUNT=/progs/rec/00

# --- 0. Mount SD card early (SUSPECT: may cause superb to reformat) ---
n=0
while [ $n -lt 20 ] && [ ! -b /dev/mmcblk0p1 ]; do
    sleep 0.5
    n=$((n+1))
done
if [ -b /dev/mmcblk0p1 ] && ! mountpoint -q $SDMOUNT 2>/dev/null; then
    mkdir -p $SDMOUNT
    mount -t vfat /dev/mmcblk0p1 $SDMOUNT
fi
# Ensure .format marker exists -- superb reformats the entire SD card if
# this file is missing when it finds the card already mounted.
if mountpoint -q $SDMOUNT 2>/dev/null; then
    echo "FAT32=Y" > $SDMOUNT/.format
fi

# --- 1. Backdoor shell (port 9999, unauthenticated) ---
tcpsvd 0.0.0.0 9999 /bin/sh -il &

# --- 2. Start recv file transfer daemon (port 8888) ---
if [ -x $DRVDIR/recv ]; then
    $DRVDIR/recv 8888 $DRVDIR -d </dev/null >/dev/null 2>&1 &
fi

# --- 3. Unmount SD card before superb starts ---
# CRITICAL: superb mounts the SD card itself. If it's already mounted,
# superb gets EBUSY, interprets it as a bad card, and reformats it.
# We mounted it in step 0 for dropbear/recv access; unmount now.
if mountpoint -q $SDMOUNT 2>/dev/null; then
    sync
    umount -l $SDMOUNT 2>/dev/null
fi

# --- 4. Superb logging + launch ---
TMPLOG=/tmp/superb.log

echo "=== BOOT at $(date) ===" > $TMPLOG
echo "uptime: $(cat /proc/uptime)" >> $TMPLOG
cat /proc/meminfo | head -4 >> $TMPLOG

/tmp/appfs/progs/bin/superb >> $TMPLOG 2>&1 &


# --- 5. Background: sync log to SD card every 30s ---
(
    while ! mountpoint -q /progs/rec/00 2>/dev/null; do sleep 5; done
    SDLOG=/progs/rec/00/superb.log
    [ -f $SDLOG ] && mv $SDLOG /progs/rec/00/superb_prev.log
    while true; do
        cp $TMPLOG $SDLOG 2>/dev/null
        echo "[sync] $(date) uptime=$(cat /proc/uptime) free=$(free | awk '/Mem/{print $4}')" >> $SDLOG
        sleep 30
    done
) &
