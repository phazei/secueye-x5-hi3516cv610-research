#!/bin/sh
# Run pipeline_test in background and dump VI/VPSS/VENC status mid-flight
cd /progs/rec/00
killall -9 superb 2>/dev/null
killall -9 pipeline_test 2>/dev/null
sleep 1
export LD_PRELOAD='/progs/rec/00/libbnr.so /progs/rec/00/libdrc.so /progs/rec/00/libacs.so /progs/rec/00/libcalcflicker.so /progs/rec/00/libir_auto.so /progs/rec/00/libldci.so /progs/rec/00/libdehaze.so /progs/rec/00/libextend_stats.so'
export LD_LIBRARY_PATH=/progs/rec/00
./pipeline_test > /tmp/pipeline.log 2>&1 &
PID=$!
echo "pipeline_test PID=$PID"
# Wait for it to reach select() (init completes in ~3-5s)
sleep 6
echo ""
echo "=== /proc/umap/vi (key sections) ==="
sed -n '/dev attr1/,/dev attr2/p;/pipe attr1/,/pipe attr2/p;/pipe status/,/pipe offline/p;/chn status/,/chn out frame/p' /proc/umap/vi
echo ""
echo "=== /proc/umap/vpss ==="
cat /proc/umap/vpss | head -80
echo ""
echo "=== /proc/umap/venc ==="
cat /proc/umap/venc | head -40
echo ""
echo "=== /proc/umap/mipi_rx ==="
cat /proc/umap/mipi_rx
echo ""
# wait for pipeline to finish (select will timeout at 10s)
wait $PID
echo "=== pipeline_test log ==="
cat /tmp/pipeline.log
