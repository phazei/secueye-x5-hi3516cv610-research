# Bootstrap deployment for cold-start: no recv, no dropbear, just tcpsvd shell.
#
# This script:
#   1. Ensures /progs/rec/00/ipc_drv/ exists on camera
#   2. Deploys recv via slow base64 path (deploy_file.py)
#   3. Starts recv daemon
#   4. Deploys debug.sh to configfs (with .format protection)
#   5. Calls redeploy_all.ps1 for the full file set
#
# Usage:
#   .\tools\bootstrap_deploy.ps1          # Full bootstrap + redeploy
#   .\tools\bootstrap_deploy.ps1 -SkipRedeploy   # Just bootstrap recv + debug.sh
#
# Prerequisites:
#   - Camera reachable at 192.168.1.153 with tcpsvd on port 9999
#   - Build completed in WSL: cd /mnt/e/Projects/ipc_XMeye_camera/driver && make all

param(
    [switch]$SkipRedeploy
)

$ErrorActionPreference = "Stop"
$cam = "192.168.1.153"
$targetDir = "/progs/rec/00/ipc_drv"

# --- Step 0: Verify camera is reachable ---
Write-Host "=== Bootstrap Deploy ==="
Write-Host "Checking camera connectivity..."
$result = python tools/cam_cmd.py "echo ALIVE" 2>&1
if ($result -notmatch "ALIVE") {
    Write-Host "ERROR: Cannot reach camera at ${cam}:9999" -ForegroundColor Red
    exit 1
}
Write-Host "  Camera is reachable."

# --- Step 1: Check if recv or dropbear are already running ---
$recvPid = python tools/cam_cmd.py "pidof recv 2>/dev/null" 2>&1
$scpAvail = $false
try {
    $tcp = New-Object System.Net.Sockets.TcpClient
    $tcp.Connect($cam, 22)
    $tcp.Close()
    $scpAvail = $true
} catch {}

if ($recvPid -match '^\d+$') {
    Write-Host "  recv already running (PID $recvPid) -- skipping bootstrap."
    if (-not $SkipRedeploy) {
        Write-Host ""
        & .\tools\redeploy_all.ps1
    }
    exit 0
}
if ($scpAvail) {
    Write-Host "  SSH available -- skipping bootstrap."
    if (-not $SkipRedeploy) {
        Write-Host ""
        & .\tools\redeploy_all.ps1 -scp
    }
    exit 0
}

Write-Host "  No recv or SSH available -- bootstrapping from scratch."

# --- Step 2: Ensure SD card is mounted and writable ---
Write-Host ""
Write-Host "Checking SD card..."
$mountInfo = python tools/cam_cmd.py "mount | grep mmcblk" 2>&1
if ($mountInfo -match "mmcblk") {
    if ($mountInfo -match "\bro\b") {
        Write-Host "  SD card mounted read-only, remounting rw..."
        python tools/cam_cmd.py "mount -o remount,rw /progs/rec/00"
    } else {
        Write-Host "  SD card mounted rw."
    }
} else {
    Write-Host "  SD card not mounted -- superb may not have started yet, waiting..."
    Start-Sleep -Seconds 10
    $mountInfo = python tools/cam_cmd.py "mount | grep mmcblk" 2>&1
    if ($mountInfo -notmatch "mmcblk") {
        Write-Host "  WARNING: SD card still not mounted. Attempting manual mount..." -ForegroundColor Yellow
        python tools/cam_cmd.py "mkdir -p /progs/rec/00 && mount -t vfat /dev/mmcblk0p1 /progs/rec/00 && echo FAT32=Y > /progs/rec/00/.format"
    }
}

# --- Step 3: Create target directory ---
Write-Host "Creating $targetDir..."
python tools/cam_cmd.py "mkdir -p $targetDir"

# --- Step 4: Deploy recv via base64 (slow but works with just tcpsvd) ---
Write-Host ""
Write-Host "Deploying recv via base64 (slow path)..."
if (-not (Test-Path "driver/build/recv")) {
    Write-Host "ERROR: driver/build/recv not found. Build first in WSL." -ForegroundColor Red
    exit 1
}
python tools/deploy_file.py driver/build/recv "$targetDir/recv"
python tools/cam_cmd.py "chmod +x $targetDir/recv"

# --- Step 5: Start recv daemon ---
Write-Host "Starting recv daemon..."
python tools/cam_cmd.py "killall recv 2>/dev/null; sleep 1; nohup $targetDir/recv 8888 $targetDir -d > /dev/null 2>&1 &"
Start-Sleep -Seconds 2
$recvCheck = python tools/cam_cmd.py "pidof recv" 2>&1
$recvCheck = $recvCheck.Trim()
if ($recvCheck -match '\d+') {
    Write-Host "  recv running (PID $recvCheck)"
} else {
    Write-Host "WARNING: recv may not have started. Retrying..." -ForegroundColor Yellow
    python tools/cam_cmd.py "$targetDir/recv 8888 $targetDir -d > /dev/null 2>&1 &"
    Start-Sleep -Seconds 2
    $recvCheck = (python tools/cam_cmd.py "pidof recv" 2>&1).Trim()
    if ($recvCheck -match '\d+') {
        Write-Host "  recv running (PID $recvCheck)"
    } else {
        Write-Host "ERROR: recv failed to start" -ForegroundColor Red
        exit 1
    }
}

# --- Step 6: Deploy debug.sh to configfs ---
Write-Host ""
Write-Host "Deploying debug.sh to configfs..."
python tools/deploy_file.py tools/debug.sh /etc/conf.d/debug.sh
Write-Host "  debug.sh deployed."

# --- Step 7: Full redeploy via recv ---
if (-not $SkipRedeploy) {
    Write-Host ""
    & .\tools\redeploy_all.ps1
} else {
    Write-Host ""
    Write-Host "=== Bootstrap complete (redeploy skipped) ==="
    Write-Host "Run .\tools\redeploy_all.ps1 to deploy all files."
}
