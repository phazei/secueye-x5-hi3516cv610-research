# Re-deploy everything to /progs/rec/00/ipc_drv/ after camera reboot wipes SD card.
# Run from project root.
#
# Prerequisites:
#   1. Build in WSL first:  cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all
#   2. recv daemon running on camera (bootstrap with deploy_file.py if not):
#      python tools/cam_cmd.py "pidof recv || (nohup /progs/rec/00/ipc_drv/recv 8888 /progs/rec/00/ipc_drv -d > /dev/null 2>&1 &)"
#
# What this deploys (to /progs/rec/00/ipc_drv/):
#   - Our binaries:    pipeline_test, libsns_sc635hai.so, recv, reg_dump, sensor_test
#   - SDK libs:        libss_mpi.so, libot_mpi_isp.so, etc. (linked by pipeline_test)
#   - ISP plugins:     libbnr.so, libdrc.so, etc. (LD_PRELOAD'd at runtime)
#   - PQ lib:          libbin.so (PQ bin file loader)
#   - Shell scripts:   rtsp_run.sh, diag_run.sh (on-camera launch/diagnostic scripts)

$ErrorActionPreference = "Stop"
$cam = "192.168.1.153"
$port = 8888
$sdkLib = "research/Hi3516CV610_SDK_V1.0.2.1_MPP_Sample/lib/hisilicon"
$pqLib = "research/hi3516cv610_PictureQuality/Hi3516CV610_PQ_ext_api_V1.0.2.1/libbin/release"
$targetDir = "/progs/rec/00/ipc_drv"

# --- Our build artifacts ---
$buildFiles = @(
    "driver/build/pipeline_test",
    "driver/build/libsns_sc635hai.so",
    "driver/build/recv",
    "driver/build/reg_dump",
    "driver/build/sensor_test"
)

# --- SDK libs (all from the same SDK dir, split only for readability) ---
# Linked by pipeline_test at load time (via LD_LIBRARY_PATH)
$sdkFiles = @(
    "$sdkLib/libss_mpi.so",
    "$sdkLib/libot_mpi_isp.so",
    "$sdkLib/libss_mpi_isp.so",
    "$sdkLib/libss_mpi_ae.so",
    "$sdkLib/libss_mpi_awb.so",
    "$sdkLib/libss_mpi_sysbind.so",
    "$sdkLib/libss_mpi_sysmem.so",
    "$sdkLib/libsecurec.so",
    "$sdkLib/libot_osal.so",
    # ISP algorithm plugins (loaded via LD_PRELOAD in rtsp_run.sh)
    "$sdkLib/libbnr.so",
    "$sdkLib/libdrc.so",
    "$sdkLib/libacs.so",
    "$sdkLib/libcalcflicker.so",
    "$sdkLib/libir_auto.so",
    "$sdkLib/libldci.so",
    "$sdkLib/libdehaze.so",
    "$sdkLib/libextend_stats.so"
)

# --- PQ bin loader lib ---
$pqFiles = @(
    "$pqLib/libbin.so"
)

# --- On-camera shell scripts ---
$scriptFiles = @(
    "tools/rtsp_run.sh",
    "tools/diag_run.sh"
)

# --- Pre-flight checks ---
Write-Host "=== Re-deploying to $targetDir ==="

$all = $buildFiles + $sdkFiles + $pqFiles + $scriptFiles
$missing = @()
foreach ($f in $all) {
    if (-not (Test-Path $f)) {
        $missing += $f
    }
}
if ($missing.Count -gt 0) {
    Write-Host "ERROR: Missing files:" -ForegroundColor Red
    $missing | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
    Write-Host ""
    Write-Host "Build artifacts missing? Run in WSL:  cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all"
    exit 1
}

# --- Ensure target directory exists ---
Write-Host "Ensuring $targetDir exists..."
python tools/cam_cmd.py "mkdir -p $targetDir"

# --- Send all files ---
Write-Host "Sending $($all.Count) files..."
python tools/send_file.py $cam $port @all

# --- Set permissions ---
Write-Host "Setting permissions..."
python tools/cam_cmd.py "cd $targetDir && chmod +x pipeline_test recv reg_dump sensor_test rtsp_run.sh diag_run.sh"

# --- Verify ---
Write-Host ""
Write-Host "=== Deployed files ==="
python tools/cam_cmd.py "ls -la $targetDir/"

Write-Host ""
Write-Host "=== Done. To start RTSP stream: ==="
Write-Host "python tools/cam_cmd.py `"setsid $targetDir/rtsp_run.sh </dev/null &>/dev/null &`""
