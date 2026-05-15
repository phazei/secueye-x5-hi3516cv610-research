#!/bin/bash
# build_dropbear.sh -- Cross-compile dropbear v2026.91 multi-binary for Hi3516CV610
#
# Produces a single static ARM binary (dropbearmulti) containing:
#   dropbear    - SSH server
#   scp         - SCP file transfer
#   dropbearkey - host key generator
#
# Run from WSL. Source tarball must be extracted alongside this script first.
# Download: https://matt.ucc.asn.au/dropbear/releases/dropbear-2026.91.tar.bz2
#
# Usage:
#   # From WSL:
#   bash /mnt/e/Projects/ipc_XMeye_camera/tools/build_dropbear.sh
#
# Output: tools/dropbearmulti (ARM ELF, static, stripped, ~226KB)
#
# Deployment: copy dropbearmulti to SD card at /progs/rec/00/ipc_drv/
# then make hard copies named 'dropbear', 'scp', 'dropbearkey' (FAT32, no symlinks).

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
REPO_DIR="$(dirname "$SCRIPT_DIR")"

# Toolchain (HiSilicon musl ARM, GCC 10.3.0)
TC_WIN="$REPO_DIR/research/hi3516cv610_toolchain/gcc-20250305-arm-v01c02-linux-musleabi/arm-v01c02-linux-musleabi-gcc"
TC_LINUX='/tmp/arm-toolchain'

# Dropbear source -- extract tarball into /tmp for build speed
DROPBEAR_VER='2026.91'
SRC_TARBALL="$REPO_DIR/tools/dropbear-${DROPBEAR_VER}.tar.bz2"
BUILD="/tmp/dropbear-build"

# --- Step 1: Prepare toolchain (fix NTFS broken symlinks) ---
if [ ! -f "$TC_LINUX/bin/arm-linux-musleabi-gcc" ]; then
    echo "=== Copying toolchain to linux-native fs (one-time, ~1min) ==="
    rm -rf "$TC_LINUX"
    cp -a "$TC_WIN" "$TC_LINUX"

    echo "=== Fixing broken NTFS symlinks ==="
    find "$TC_LINUX" -type f -size -100c \( -name '*.so' -o -name '*.so.0' -o -name '*.a' \) | while read f; do
        target=$(cat "$f" 2>/dev/null)
        dir=$(dirname "$f")
        if [ -f "$dir/$target" ]; then
            echo "  Fix: $(basename "$f") -> $target"
            rm "$f"
            ln -s "$target" "$f"
        fi
    done
    echo "=== Toolchain ready ==="
else
    echo "=== Toolchain already cached at $TC_LINUX ==="
fi

# --- Step 2: Extract and prepare source ---
echo "=== Preparing dropbear source ==="
rm -rf "$BUILD"
if [ -f "$SRC_TARBALL" ]; then
    echo "  Extracting from tarball..."
    mkdir -p "$BUILD"
    tar xjf "$SRC_TARBALL" -C /tmp
    mv "/tmp/dropbear-${DROPBEAR_VER}" "$BUILD"
    # Flatten if nested
    if [ -d "$BUILD/dropbear-${DROPBEAR_VER}" ]; then
        mv "$BUILD/dropbear-${DROPBEAR_VER}"/* "$BUILD/"
        rmdir "$BUILD/dropbear-${DROPBEAR_VER}"
    fi
else
    # Fallback: assume source already extracted at known location
    SRC_ALT="/mnt/c/Users/HomeStar/AppData/Local/Temp/opencode/dropbear-${DROPBEAR_VER}"
    if [ -d "$SRC_ALT" ]; then
        echo "  Copying from $SRC_ALT..."
        cp -a "$SRC_ALT" "$BUILD"
    else
        echo "ERROR: No dropbear source found."
        echo "  Download from: https://matt.ucc.asn.au/dropbear/releases/dropbear-${DROPBEAR_VER}.tar.bz2"
        echo "  Place at: $SRC_TARBALL"
        exit 1
    fi
fi

# --- Step 3: Copy our localoptions.h ---
cp "$SCRIPT_DIR/dropbear_localoptions.h" "$BUILD/localoptions.h"
cd "$BUILD"

# --- Step 4: Configure ---
echo "=== Configuring ==="
export PATH="$TC_LINUX/bin:$PATH"
export CC=arm-linux-musleabi-gcc
export STRIP=arm-linux-musleabi-strip
export CFLAGS='-mcpu=cortex-a7 -mfloat-abi=softfp -mfpu=neon-vfpv4 -Os -ffunction-sections -fdata-sections'
export LDFLAGS='-Wl,--gc-sections'

./configure \
    --host=arm-linux-musleabi \
    --enable-static \
    --disable-zlib \
    --disable-syslog \
    --disable-lastlog \
    --disable-utmp \
    --disable-utmpx \
    --disable-wtmp \
    --disable-wtmpx \
    --disable-loginfunc \
    --disable-pututline \
    --disable-pututxline \
    --enable-bundled-libtom \
    LTM_CFLAGS='-Os'

# --- Step 5: Build ---
echo "=== Building multi-binary ==="
make clean 2>/dev/null || true
make PROGRAMS="dropbear scp dropbearkey" MULTI=1 -j$(nproc)

# --- Step 6: Strip ---
echo "=== Stripping ==="
arm-linux-musleabi-strip dropbearmulti

# --- Step 7: Report ---
echo ""
echo "=== Build complete ==="
file dropbearmulti
ls -lh dropbearmulti

# --- Step 8: Copy to repo ---
cp dropbearmulti "$SCRIPT_DIR/dropbearmulti"
echo ""
echo "Output: $SCRIPT_DIR/dropbearmulti"
echo ""
echo "Deploy to camera:"
echo "  scp -O tools/dropbearmulti root@192.168.1.153:/progs/rec/00/ipc_drv/dropbearmulti"
echo "  ssh root@192.168.1.153 'cd /progs/rec/00/ipc_drv && cp dropbearmulti dropbear && cp dropbearmulti scp && cp dropbearmulti dropbearkey'"
