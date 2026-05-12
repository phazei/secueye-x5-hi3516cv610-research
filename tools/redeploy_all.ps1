# Re-deploy everything to /progs/rec/00 after camera reboot wipes SD card.
# Run from project root.

$ErrorActionPreference = "Stop"
$cam = "192.168.1.153"
$port = 8888
$sdkLib = "research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/hisilicon"

Write-Host "=== Re-deploying to camera ==="

# Test binaries
$ourFiles = @(
    "driver/build/pipeline_test",
    "driver/build/libsns_sc635hai.so",
    "driver/build/reg_dump",
    "driver/build/sensor_test",
    "driver/build/recv"
)

# SDK MPI libs (required for pipeline_test)
$sdkFiles = @(
    "$sdkLib/libss_mpi.so",
    "$sdkLib/libot_mpi_isp.so",
    "$sdkLib/libss_mpi_isp.so",
    "$sdkLib/libss_mpi_ae.so",
    "$sdkLib/libss_mpi_awb.so",
    "$sdkLib/libss_mpi_sysbind.so",
    "$sdkLib/libss_mpi_sysmem.so",
    "$sdkLib/libsecurec.so",
    "$sdkLib/libot_osal.so"
)

# ISP plugin libs (LD_PRELOAD'ed)
$ispPlugins = @(
    "$sdkLib/libbnr.so",
    "$sdkLib/libdrc.so",
    "$sdkLib/libacs.so",
    "$sdkLib/libcalcflicker.so",
    "$sdkLib/libir_auto.so",
    "$sdkLib/libldci.so",
    "$sdkLib/libdehaze.so",
    "$sdkLib/libextend_stats.so"
)

$all = $ourFiles + $sdkFiles + $ispPlugins
Write-Host "Sending $($all.Count) files..."
python tools/send_file.py $cam $port @all

# Chmod our binaries
Write-Host "Setting permissions..."
python tools/cam_cmd.py "cd /progs/rec/00 && chmod +x pipeline_test reg_dump sensor_test recv && ls -la pipeline_test libsns_sc635hai.so reg_dump"
