#!/bin/sh
# Launch pipeline_test in RTSP streaming mode.
#
# pipeline_test now feeds /dev/watchdog internally (ioctl KEEPALIVE),
# so this script just needs to SIGSTOP mySystem, kill superb, and launch.
#
# Stream URL: rtsp://<camera_ip>:554/live0
#
# NOTE: SIGSTOP of mySystem kills the shell (tcpsvd is a child of mySystem).
# This script must be launched via setsid from a remote session. The shell
# will reconnect after pipeline_test exits and mySystem resumes.
#
# Usage (from remote shell):
#   setsid /progs/rec/00/ipc_drv/rtsp_run.sh </dev/null &>/dev/null &
#
# To stop:
#   # Reconnect shell (after mySystem resumes) or use another method:
#   kill $(cat /tmp/pipeline_test.pid)

RTSP_PORT="${1:-554}"
DIR=/progs/rec/00/ipc_drv
PQ_BIN=/home/sensor/sc635hai/pqbin/day.bin
LOG=$DIR/rtsp_pipeline.log

# Ignore HUP (shell will die when mySystem stops)
trap '' HUP

# Save mySystem PID for cleanup
MYS_PID=$(pidof mySystem)

# Kill old instances
killall pipeline_test 2>/dev/null
sleep 1

# Freeze mySystem so it can't respawn superb
if [ -n "$MYS_PID" ]; then
    kill -STOP "$MYS_PID"
fi

# Kill superb (frees ISP/VENC resources and /dev/watchdog fd)
killall -9 superb 2>/dev/null
sleep 1
sync
# Re-create .format marker -- superb deletes it on shutdown and reformats
# the entire SD card if it's missing when it next starts.
echo "FAT32=Y" > "$SDMOUNT/.format"

cd "$DIR"

# ISP extension libraries (same as diag_run.sh)
export LD_PRELOAD="$DIR/libbnr.so $DIR/libdrc.so $DIR/libacs.so $DIR/libcalcflicker.so $DIR/libir_auto.so $DIR/libldci.so $DIR/libdehaze.so $DIR/libextend_stats.so"
export LD_LIBRARY_PATH="$DIR"

echo "=== rtsp_run start $(date) ===" > "$LOG"

# Run pipeline_test (feeds watchdog internally via WDIOC_KEEPALIVE)
./pipeline_test --rtsp --rtsp-port "$RTSP_PORT" "$PQ_BIN" >> "$LOG" 2>&1 &
PT_PID=$!
echo "$PT_PID" > /tmp/pipeline_test.pid
echo "PID=$PT_PID" >> "$LOG"

# Wait for it to exit
wait $PT_PID

# Resume mySystem (restores shell, superb, watchdog)
if [ -n "$MYS_PID" ]; then
    kill -CONT "$MYS_PID" 2>/dev/null
fi

echo "=== rtsp_run done $(date) ===" >> "$LOG"
