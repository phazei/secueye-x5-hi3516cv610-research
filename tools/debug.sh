#!/bin/sh
# debug.sh -- persistent boot script on configfs (jffs2, survives reboot+factory reset)
# Called by startup.sh INSTEAD of launching superb directly.
# We set up SSH access, then launch superb ourselves.
#
# Deployed to: /etc/conf.d/debug.sh (on camera configfs partition)
# See CAMERA.md "debug.sh Boot Hook" for documentation.
#
# This file is a repo backup. The live copy is on the camera's configfs.
# To deploy: upload this file to /etc/conf.d/debug.sh on the camera.

CONFDIR=/etc/conf.d
DRVDIR=/progs/rec/00/ipc_drv
SDMOUNT=/progs/rec/00

# --- 0. Mount SD card (superb normally does this, but we need it first) ---
# Block device may not exist yet at early boot; wait up to 10 seconds.
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

# --- 1. Set root password (tmpfs /etc/shadow has empty password from rootfs) ---
if [ -f $CONFDIR/shadow_root ]; then
    sed -i "s|^root:.*|$(cat $CONFDIR/shadow_root)|" /etc/shadow
fi

# --- 2. Fix PTY allocation (stock mounts devpts with ptmxmode=000) ---
umount /dev/pts 2>/dev/null
mount -t devpts devpts /dev/pts -o mode=620,ptmxmode=666

# --- 3. Create /etc/shells (stock rootfs has none) ---
echo '/bin/sh' > /etc/shells

# --- 4. Set up writable /root with SSH authorized_keys ---
mount -t tmpfs tmpfs /root
mkdir -p /root/.ssh
chmod 700 /root /root/.ssh
if [ -f $CONFDIR/dropbear/authorized_keys ]; then
    cp $CONFDIR/dropbear/authorized_keys /root/.ssh/authorized_keys
    chmod 600 /root/.ssh/authorized_keys
fi

# --- 5. Set up dropbear host keys ---
mkdir -p /etc/dropbear
cp $CONFDIR/dropbear/dropbear_*_host_key /etc/dropbear/ 2>/dev/null

# --- 6. Start dropbear SSH daemon ---
if [ -x $DRVDIR/dropbear ]; then
    $DRVDIR/dropbear -p 22
fi

# --- 7. Backdoor shell (legacy, keep until SSH proven reliable) ---
tcpsvd 0.0.0.0 9999 /bin/sh -il &

# --- 8. Start recv file transfer daemon ---
if [ -x $DRVDIR/recv ]; then
    $DRVDIR/recv 8888 $DRVDIR -d </dev/null >/dev/null 2>&1 &
fi

# --- 9. Unmount SD card before superb starts ---
# CRITICAL: superb mounts the SD card itself. If it's already mounted,
# superb gets EBUSY, interprets it as a bad card, and reformats it.
# We mounted it in step 0 for dropbear/recv access; unmount now.
if mountpoint -q $SDMOUNT 2>/dev/null; then
    sync
    umount -l $SDMOUNT 2>/dev/null
fi

# --- 10. Superb logging + launch ---
TMPLOG=/tmp/superb.log
SDLOG=/progs/rec/00/superb.log

echo "=== BOOT at $(date) ===" > $TMPLOG
echo "uptime: $(cat /proc/uptime)" >> $TMPLOG
cat /proc/meminfo | head -4 >> $TMPLOG

/tmp/appfs/progs/bin/superb >> $TMPLOG 2>&1 &

# --- 11. Background: sync log to SD card every 30s ---
(
    while ! mountpoint -q /progs/rec/00 2>/dev/null; do sleep 5; done
    [ -f $SDLOG ] && mv $SDLOG /progs/rec/00/superb_prev.log
    while true; do
        cp $TMPLOG $SDLOG 2>/dev/null
        echo "[sync] $(date) uptime=$(cat /proc/uptime) free=$(free | awk '/Mem/{print $4}')" >> $SDLOG
        sleep 30
    done
) &
