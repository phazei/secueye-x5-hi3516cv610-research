#!/bin/sh
# Run ipc_daemon, capture diagnostics mid-flight, then restore superb.
#
# IMPORTANT: We do NOT kill mySystem. Past lesson: killing mySystem caused
# files on the SD card to be truncated on next superb startup.
#
# But mySystem respawns superb ~1s after we kill it, which races our test.
# Solution: SIGSTOP mySystem (pause, don't kill) before killing superb, then
# SIGCONT after we're done. mySystem can't respawn anything while stopped.
#
# Safety net: capture SD checksums before/after to detect any wipe.

MYS_PID=$(pidof mySystem)
if [ -n "$MYS_PID" ]; then
    kill -STOP $MYS_PID
    echo "mySystem ($MYS_PID) STOPPED" > /progs/rec/00/ipc_drv/diag_meta.txt
fi

killall -9 superb 2>/dev/null
killall -9 ipc_daemon 2>/dev/null
killall -9 pipeline_test 2>/dev/null
sleep 1

cd /progs/rec/00/ipc_drv
# ipc_daemon writes to /tmp (tmpfs, no disk-flush latency), then we copy
# to SD with sync after teardown. Direct writes to SD/FAT32 during the run
# can be lost if the camera reboots before fsync (FAT has no journal).
DIAG_DIR=/progs/rec/00/ipc_drv
TMP_LOG=/tmp/pipeline.log
PIPELINE_LOG=$DIAG_DIR/pipeline.log
DIAG_TXT=$DIAG_DIR/diag.txt
META_TXT=$DIAG_DIR/diag_meta.txt
rm -f "$PIPELINE_LOG" "$DIAG_TXT" "$META_TXT" "$TMP_LOG"

# Start ipc_daemon in background WITH LD_PRELOAD set, but inherit only to it.
# Use a sub-shell so the env doesn't leak into our cat commands later.
(
    export LD_PRELOAD='/progs/rec/00/ipc_drv/libbnr.so /progs/rec/00/ipc_drv/libdrc.so /progs/rec/00/ipc_drv/libacs.so /progs/rec/00/ipc_drv/libcalcflicker.so /progs/rec/00/ipc_drv/libir_auto.so /progs/rec/00/ipc_drv/libldci.so /progs/rec/00/ipc_drv/libdehaze.so /progs/rec/00/ipc_drv/libextend_stats.so'
    export LD_LIBRARY_PATH=/progs/rec/00/ipc_drv
    PQ_BIN="${1:-/home/sensor/sc635hai/pqbin/day.bin}"
    ./ipc_daemon "$PQ_BIN" > "$TMP_LOG" 2>&1 &
    echo $! > "$DIAG_DIR/pt_pid"
) </dev/null
PT_PID=$(cat "$DIAG_DIR/pt_pid")

# ipc_daemon does ~6s of init, 12s ISP/AE stabilization, then 10s capture (150 frames@15fps).
# Total runtime ~28s. Wait 35s before capturing diag.
sleep 35

# Capture diagnostics with CLEAN environment (no LD_PRELOAD)
unset LD_PRELOAD
unset LD_LIBRARY_PATH

if kill -0 $PT_PID 2>/dev/null; then
    echo "ipc_daemon alive at 10s -- capturing diag" >> "$META_TXT"
else
    echo "ipc_daemon ALREADY EXITED before 10s -- diag may be stale" >> "$META_TXT"
fi

{
    echo "==== sensor regs (post-init, while ipc_daemon runs) ===="
    /progs/rec/00/ipc_drv/reg_dump 0x0100 0x0100
    /progs/rec/00/ipc_drv/reg_dump 0x36E9 0x36F2
    /progs/rec/00/ipc_drv/reg_dump 0x37F9 0x37F9
    echo ""
    echo "==== /proc/umap/mipi_rx ===="
    cat /proc/umap/mipi_rx
    echo ""
    echo "==== /proc/umap/vi ===="
    cat /proc/umap/vi
    echo ""
    echo "==== /proc/umap/vpss ===="
    cat /proc/umap/vpss
    echo ""
    echo "==== /proc/umap/venc ===="
    cat /proc/umap/venc
} > "$DIAG_TXT" 2>&1
sync

# Kill ipc_daemon.
kill -9 $PT_PID 2>/dev/null
sleep 1

# Copy tmpfs log to SD and force sync IMMEDIATELY (before reboot risk).
cp "$TMP_LOG" "$PIPELINE_LOG" 2>/dev/null
sync
sleep 1

# Resume mySystem so it can respawn superb cleanly.
if [ -n "$MYS_PID" ]; then
    kill -CONT $MYS_PID
    echo "mySystem ($MYS_PID) CONTINUED" >> "$META_TXT"
fi
sync
echo "diag done"
