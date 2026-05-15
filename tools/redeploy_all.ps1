# Re-deploy everything to /progs/rec/00/ipc_drv/ after camera reboot wipes SD card.
# Run from project root.
#
# Usage:
#   .\tools\redeploy_all.ps1          # Auto-detect: try SCP, fall back to recv
#   .\tools\redeploy_all.ps1 -scp     # Force SCP mode (requires dropbear running)
#   .\tools\redeploy_all.ps1 -recv    # Force recv mode (requires recv daemon)
#
# Prerequisites:
#   1. Build in WSL first:  cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all
#   2. For recv mode: recv daemon running on camera (bootstrap with deploy_file.py if not)
#   3. For SCP mode: dropbear SSH running on camera + key auth configured
#
# What this deploys (to /progs/rec/00/ipc_drv/):
#   - Our binaries:    pipeline_test, libsns_sc635hai.so, recv, reg_dump, sensor_test
#   - Dropbear SSH:    dropbearmulti -> hard copies: dropbear, scp, dropbearkey
#   - SDK libs:        libss_mpi.so, libot_mpi_isp.so, etc. (linked by pipeline_test)
#   - ISP plugins:     libbnr.so, libdrc.so, etc. (LD_PRELOAD'd at runtime)
#   - PQ lib:          libbin.so (PQ bin file loader)
#   - Shell scripts:   rtsp_run.sh, diag_run.sh (on-camera launch/diagnostic scripts)

param(
    [switch]$scp,
    [switch]$recv
)

$ErrorActionPreference = "Stop"
$cam = "192.168.1.153"
$recvPort = 8888
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

# --- Dropbear SSH multi-binary ---
# FAT32 doesn't support symlinks; send once as dropbearmulti, then make
# hard copies named 'dropbear', 'scp', 'dropbearkey' on camera.
$dropbearFiles = @(
    "tools/dropbearmulti"
)

# --- On-camera shell scripts ---
$scriptFiles = @(
    "tools/rtsp_run.sh",
    "tools/diag_run.sh"
)

# --- Pre-flight checks ---
Write-Host "=== Re-deploying to $targetDir ==="

$all = $buildFiles + $dropbearFiles + $sdkFiles + $pqFiles + $scriptFiles
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

# --- Determine transfer method ---
$useSSH = $false
if ($scp -and $recv) {
    Write-Host "ERROR: Specify -scp or -recv, not both." -ForegroundColor Red
    exit 1
}
if ($scp) {
    $useSSH = $true
} elseif (-not $recv) {
    # Auto-detect: try SSH port 22
    Write-Host "Auto-detecting transfer method..."
    try {
        $tcp = New-Object System.Net.Sockets.TcpClient
        $tcp.Connect($cam, 22)
        $tcp.Close()
        $useSSH = $true
        Write-Host "  SSH (port 22) reachable -- using SCP"
    } catch {
        Write-Host "  SSH not available -- using recv daemon"
    }
}

# --- Ensure target directory exists ---
Write-Host "Ensuring $targetDir exists..."
python tools/cam_cmd.py "mkdir -p $targetDir"

# --- Send all files ---
if ($useSSH) {
    Write-Host "Sending $($all.Count) files via SCP..."
    # scp -O forces legacy SCP protocol (Windows scp.exe defaults to SFTP)
    $scpDest = "root@${cam}:${targetDir}/"
    foreach ($f in $all) {
        $name = Split-Path $f -Leaf
        Write-Host "  $name"
        # Use -O for legacy SCP protocol (dropbear doesn't support SFTP)
        scp -O $f $scpDest
        if ($LASTEXITCODE -ne 0) {
            Write-Host "ERROR: SCP failed for $f. Falling back to recv for remaining files." -ForegroundColor Yellow
            $useSSH = $false
            # Send this file and all remaining via recv
            $remaining = $all[$all.IndexOf($f)..($all.Count - 1)]
            Write-Host "Sending $($remaining.Count) remaining files via recv..."
            python tools/send_file.py $cam $recvPort @remaining
            break
        }
    }
} else {
    Write-Host "Sending $($all.Count) files via recv..."
    python tools/send_file.py $cam $recvPort @all
}

# --- Set permissions ---
Write-Host "Setting permissions..."
python tools/cam_cmd.py "cd $targetDir && chmod +x pipeline_test recv reg_dump sensor_test rtsp_run.sh diag_run.sh dropbearmulti"

# --- Create dropbear hard copies (FAT32 has no symlinks) ---
Write-Host "Creating dropbear hard copies..."
python tools/cam_cmd.py "cd $targetDir && cp dropbearmulti dropbear && cp dropbearmulti scp && cp dropbearmulti dropbearkey"

# --- Verify ---
Write-Host ""
Write-Host "=== Deployed files ==="
python tools/cam_cmd.py "ls -la $targetDir/"

Write-Host ""
Write-Host "=== Done. To start RTSP stream: ==="
Write-Host "python tools/cam_cmd.py `"setsid $targetDir/rtsp_run.sh </dev/null &>/dev/null &`""
