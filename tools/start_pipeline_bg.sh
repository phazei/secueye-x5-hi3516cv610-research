#!/bin/sh
# Start pipeline_test fully detached so cam_cmd disconnect doesn't kill it
# mySystem is the watchdog that respawns superb -- kill it first
killall -9 mySystem 2>/dev/null
killall -9 superb 2>/dev/null
killall -9 pipeline_test 2>/dev/null
sleep 2
cd /progs/rec/00
rm -f /tmp/pipeline.log
export LD_PRELOAD='/progs/rec/00/libbnr.so /progs/rec/00/libdrc.so /progs/rec/00/libacs.so /progs/rec/00/libcalcflicker.so /progs/rec/00/libir_auto.so /progs/rec/00/libldci.so /progs/rec/00/libdehaze.so /progs/rec/00/libextend_stats.so'
export LD_LIBRARY_PATH=/progs/rec/00
setsid sh -c './pipeline_test > /tmp/pipeline.log 2>&1' </dev/null >/dev/null 2>&1 &
echo "started, pid=$!"
