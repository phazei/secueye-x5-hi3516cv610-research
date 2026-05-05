"""
Rewrite debug.sh to log superb output directly to SD card.
Strategy: start superb logging to /tmp (tmpfs, 17MB free), then 
once SD is mounted, move the log there and tail -f to SD.
Actually simpler: superb mounts the SD early in its startup,
so we just need a small delay then redirect.
"""
import socket
import time

def cam(cmd, wait=2):
    s = socket.socket()
    s.settimeout(10)
    s.connect(('192.168.1.153', 9999))
    time.sleep(0.3)
    s.recv(4096)
    marker = '__END__'
    s.sendall(f'{cmd}; echo {marker}\n'.encode())
    time.sleep(wait)
    data = b''
    while True:
        try:
            s.settimeout(2)
            chunk = s.recv(65536)
            if not chunk:
                break
            data += chunk
            if marker.encode() in data:
                break
        except socket.timeout:
            break
    s.close()
    text = data.decode('latin-1', errors='replace')
    idx = text.find(marker)
    if idx >= 0:
        text = text[:idx]
    lines = text.strip().split('\n')
    if lines and cmd[:15] in lines[0]:
        lines = lines[1:]
    return '\n'.join(lines).strip()


# The simplest reliable approach:
# - Log to /tmp/superb.log (tmpfs, always available, lost on reboot)
# - Background copier syncs to SD every 30s once SD is mounted
# - On boot, rotate previous SD log
# - No configfs log files at all
new_script = r'''#!/bin/sh
# Backdoor shell
tcpsvd 0.0.0.0 9999 /bin/sh -il &

TMPLOG=/tmp/superb.log
SDLOG=/progs/rec/00/superb.log

# Boot header
echo "=== BOOT at $(date) ===" > $TMPLOG
echo "uptime: $(cat /proc/uptime)" >> $TMPLOG
cat /proc/meminfo | head -4 >> $TMPLOG

# Start superb with output to tmpfs log
/tmp/appfs/progs/bin/superb >> $TMPLOG 2>&1 &

# PQTools after delay
sleep 15
cd /progs/rec/00/PQtool && ./PQTools.sh -c 2>/dev/null

# Background: sync log to SD card every 30s
(
    # Wait for SD to be mounted
    while ! mountpoint -q /progs/rec/00 2>/dev/null; do
        sleep 5
    done

    # Rotate old SD log
    [ -f $SDLOG ] && mv $SDLOG /progs/rec/00/superb_prev.log

    # Sync loop
    while true; do
        cp $TMPLOG $SDLOG 2>/dev/null
        echo "[sync] $(date) uptime=$(cat /proc/uptime) free=$(free | awk '/Mem/{print $4}')" >> $SDLOG
        sleep 30
    done
) &
'''

print("Writing new debug.sh (SD-only logging, no configfs)...")
lines = new_script.strip().split('\n')

first = lines[0]
cam(f"echo '{first}' > /etc/conf.d/debug.sh", wait=1)

for line in lines[1:]:
    escaped = line.replace("'", "'\\''")
    cam(f"echo '{escaped}' >> /etc/conf.d/debug.sh", wait=0.3)

cam("chmod +x /etc/conf.d/debug.sh", wait=1)

print("\nNew debug.sh:")
print(cam("cat /etc/conf.d/debug.sh"))
print()

# Clean any leftover configfs logs
cam("rm -f /etc/conf.d/superb.log /etc/conf.d/superb_prev.log")

print("Configfs free space:")
print(cam("df -h /etc/conf.d/"))
print()

print("Done. On next reboot, superb logs to /tmp/superb.log,")
print("which gets synced to SD at /progs/rec/00/superb.log every 30s.")
print()
print("Current uptime:")
print(cam("cat /proc/uptime"))
