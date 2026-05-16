#!/bin/sh
#
# start_pqcontrol.sh -- Launch ittb_control as a sidecar to the running ISP
#
# Usage:  cd /progs/rec/00/PQtool && ./start_pqcontrol.sh
#
# Prerequisites:
#   - superb or pipeline_test must already be running (owns the ISP pipe)
#   - PQTools.exe on PC connects to <camera_ip>:4321
#   - View live feed via RTSP (VLC etc.) -- ittb_control has no viewer
#
# To stop:  killall ittb_control
#

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)
cd "$SCRIPT_DIR"

# Kill any existing instance
if pidof ittb_control > /dev/null 2>&1; then
    echo "Stopping existing ittb_control..."
    killall ittb_control
    sleep 2
    # Force-kill if still alive
    pidof ittb_control > /dev/null 2>&1 && kill -9 $(pidof ittb_control) 2>/dev/null
fi

export LD_LIBRARY_PATH="${LD_LIBRARY_PATH}:${SCRIPT_DIR}/libs"

echo "Starting ittb_control (port 4321)..."
./ittb_control &
echo "ittb_control PID: $!"
echo "Connect PQTools.exe to $(hostname -i 2>/dev/null || echo '<camera_ip>'):4321"
