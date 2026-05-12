#!/bin/sh
# Run pipeline_test once, then immediately probe sensor state before superb returns
killall -9 mySystem 2>/dev/null
sleep 1
killall -9 superb 2>/dev/null
sleep 1

cd /progs/rec/00
rm -f /tmp/pipeline.log /tmp/probe.txt

# Run pipeline_test (it will timeout at 10s on select)
(
    export LD_PRELOAD='/progs/rec/00/libbnr.so /progs/rec/00/libdrc.so /progs/rec/00/libacs.so /progs/rec/00/libcalcflicker.so /progs/rec/00/libir_auto.so /progs/rec/00/libldci.so /progs/rec/00/libdehaze.so /progs/rec/00/libextend_stats.so'
    export LD_LIBRARY_PATH=/progs/rec/00
    ./pipeline_test > /tmp/pipeline.log 2>&1
)

# Immediately probe sensor (clean env - no LD_PRELOAD)
unset LD_PRELOAD
unset LD_LIBRARY_PATH
{
    echo "==== Sensor state AFTER pipeline_test ===="
    ./sensor_test 1
    echo ""
    echo "==== MIPI RX state ===="
    cat /proc/umap/mipi_rx | head -30
} > /tmp/probe.txt 2>&1

# Restart superb fast
nohup /tmp/appfs/progs/bin/superb > /dev/null 2>&1 &
echo "probe done, superb restarted"
