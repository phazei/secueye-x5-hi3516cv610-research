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
#
# Also deploys PQtool/ (ISP tuning sidecar) to /progs/rec/00/PQtool/:

param(
    [switch]$scp,
    [switch]$recv
)

$ErrorActionPreference = "Stop"
$cam = "192.168.1.153"
$recvPort = 8888
$prebuilt = "driver/prebuilt"
$targetDir = "/progs/rec/00/ipc_drv"
$pqToolDir = "/progs/rec/00/PQtool"
$pqToolSrc = "tools/pq_tune"

# --- Our build artifacts ---
$buildFiles = @(
    "driver/build/pipeline_test",
    "driver/build/libsns_sc635hai.so",
    "driver/build/recv",
    "driver/build/reg_dump",
    "driver/build/sensor_test"
)

# --- SDK MPI libs (linked by pipeline_test at load time via LD_LIBRARY_PATH) ---
$sdkFiles = @(
    "$prebuilt/sdk_mpi/libss_mpi.so",
    "$prebuilt/sdk_mpi/libot_mpi_isp.so",
    "$prebuilt/sdk_mpi/libss_mpi_isp.so",
    "$prebuilt/sdk_mpi/libss_mpi_ae.so",
    "$prebuilt/sdk_mpi/libss_mpi_awb.so",
    "$prebuilt/sdk_mpi/libss_mpi_sysbind.so",
    "$prebuilt/sdk_mpi/libss_mpi_sysmem.so",
    "$prebuilt/sdk_mpi/libsecurec.so",
    "$prebuilt/sdk_mpi/libot_osal.so",
    # ISP algorithm plugins (loaded via LD_PRELOAD in rtsp_run.sh)
    "$prebuilt/isp_plugins/libbnr.so",
    "$prebuilt/isp_plugins/libdrc.so",
    "$prebuilt/isp_plugins/libacs.so",
    "$prebuilt/isp_plugins/libcalcflicker.so",
    "$prebuilt/isp_plugins/libir_auto.so",
    "$prebuilt/isp_plugins/libldci.so",
    "$prebuilt/isp_plugins/libdehaze.so",
    "$prebuilt/isp_plugins/libextend_stats.so"
)

# --- PQ bin loader lib ---
$pqFiles = @(
    "$prebuilt/pq/libbin.so"
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

# --- PQTool ISP tuning sidecar (deployed to /progs/rec/00/PQtool/) ---
# Map of: local source file -> subdirectory under $pqToolDir on camera
# Files with no subdir go to the PQtool root.
$pqToolFiles = [ordered]@{
    # Root files
    "$pqToolSrc/ittb_control"                          = ""
    "$pqToolSrc/config.cfg"                            = ""
    "$pqToolSrc/start_pqcontrol.sh"                    = ""
    # Sensor configs
    "$pqToolSrc/configs/sc635hai/config_entry.ini"     = "configs/sc635hai"
    "$pqToolSrc/configs/sc635hai/sc635hai.ini"          = "configs/sc635hai"
    # Common configs
    "$pqToolSrc/configs/common/config_mt.ini"          = "configs/common"
    "$pqToolSrc/configs/common/config_stream.ini"      = "configs/common"
}
# Libs -- enumerate all .so files in pq_tune/libs/
$pqToolLibs = Get-ChildItem "$pqToolSrc/libs" -Filter "*.so*" | ForEach-Object { $_.FullName }

# --- Pre-flight checks ---
Write-Host "=== Re-deploying to $targetDir ==="

$all = $buildFiles + $dropbearFiles + $sdkFiles + $pqFiles + $scriptFiles
$missing = @()
foreach ($f in $all) {
    if (-not (Test-Path $f)) {
        $missing += $f
    }
}
# Also check PQtool files
foreach ($f in $pqToolFiles.Keys) {
    if (-not (Test-Path $f)) { $missing += $f }
}
if ($pqToolLibs.Count -eq 0) {
    $missing += "$pqToolSrc/libs/*.so (no libs found)"
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

# --- Deploy PQtool (ISP tuning sidecar) ---
Write-Host ""
Write-Host "=== Deploying PQtool to $pqToolDir ==="

if ($useSSH) {
    # Create directory structure
    $pqSubdirs = @("configs/sc635hai", "configs/common", "libs")
    $mkdirCmd = "mkdir -p " + ($pqSubdirs | ForEach-Object { "$pqToolDir/$_" } | Join-String -Separator " ")
    python tools/cam_cmd.py $mkdirCmd

    # SCP: send files grouped by target subdirectory
    foreach ($entry in $pqToolFiles.GetEnumerator()) {
        $src = $entry.Key
        $sub = $entry.Value
        $dest = if ($sub) { "$pqToolDir/$sub/" } else { "$pqToolDir/" }
        $name = Split-Path $src -Leaf
        Write-Host "  $name -> $dest"
        scp -O $src "root@${cam}:${dest}"
        if ($LASTEXITCODE -ne 0) {
            Write-Host "ERROR: SCP failed for $src" -ForegroundColor Red
            exit 1
        }
    }
    # Libs -- batch send all .so files
    Write-Host "  libs/ ($($pqToolLibs.Count) files)"
    foreach ($lib in $pqToolLibs) {
        scp -O $lib "root@${cam}:${pqToolDir}/libs/"
        if ($LASTEXITCODE -ne 0) {
            Write-Host "ERROR: SCP failed for $lib" -ForegroundColor Red
            exit 1
        }
    }
    # Set permissions
    Write-Host "Setting PQtool permissions..."
    python tools/cam_cmd.py "chmod +x $pqToolDir/ittb_control $pqToolDir/start_pqcontrol.sh"
} else {
    # recv daemon only writes to its configured directory -- it can't target
    # subdirectories. PQtool deploy requires SCP (base64 via deploy_file.py
    # is too slow for ~28 libs). Skip PQtool in recv mode.
    Write-Host "WARNING: PQtool deploy requires SCP. Skipping in recv mode." -ForegroundColor Yellow
    Write-Host "  Re-run with -scp once dropbear is available, or deploy manually."
}

# --- Verify ---
Write-Host ""
Write-Host "=== Deployed files (ipc_drv) ==="
python tools/cam_cmd.py "ls -la $targetDir/"
Write-Host ""
Write-Host "=== Deployed files (PQtool) ==="
python tools/cam_cmd.py "ls -laR $pqToolDir/"

Write-Host ""
Write-Host "=== Done. To start RTSP stream: ==="
Write-Host "python tools/cam_cmd.py `"setsid $targetDir/rtsp_run.sh </dev/null &>/dev/null &`""
