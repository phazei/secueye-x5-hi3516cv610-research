#!/bin/bash
# Deploy a built ARM binary to the camera via the root shell (port 9999)
#
# Usage: ./deploy.sh <binary_name>
# Example: ./deploy.sh httppost
#
# This uploads the binary via base64 encoding over the TCP shell.
# Works for binaries up to ~500KB. Larger binaries should use the SD card directly.

set -e

CAMERA_IP="${CAMERA_IP:-192.168.1.153}"
CAMERA_PORT="${CAMERA_PORT:-9999}"
DEPLOY_DIR="/progs/rec/00/custom"
BUILD_DIR="build"

if [ -z "$1" ]; then
    echo "Usage: $0 <binary_name>"
    echo "Available binaries:"
    ls -la "$BUILD_DIR"/ 2>/dev/null || echo "  (none built yet -- run 'make' first)"
    exit 1
fi

BINARY="$BUILD_DIR/$1"
if [ ! -f "$BINARY" ]; then
    echo "Error: $BINARY not found. Run 'make $1' first."
    exit 1
fi

SIZE=$(stat -f%z "$BINARY" 2>/dev/null || stat -c%s "$BINARY" 2>/dev/null)
echo "Deploying $1 ($SIZE bytes) to $CAMERA_IP:$DEPLOY_DIR/"

# Check if binary is too large for base64 upload
if [ "$SIZE" -gt 524288 ]; then
    echo "Warning: Binary is >512KB. Base64 upload may be unreliable."
    echo "Consider copying directly to the SD card instead."
    read -p "Continue anyway? [y/N] " -r
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        exit 0
    fi
fi

# Encode binary as base64
B64=$(base64 "$BINARY")
B64_SIZE=${#B64}
echo "Base64 encoded: $B64_SIZE bytes"

# Use Python to upload (it handles the socket comms reliably)
python3 -c "
import socket, time, sys

ip = '$CAMERA_IP'
port = $CAMERA_PORT
deploy_dir = '$DEPLOY_DIR'
binary_name = '$1'

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.settimeout(30)
s.connect((ip, port))
time.sleep(0.3)
s.recv(4096)  # banner

def send_cmd(cmd, wait=2):
    marker = '__DEPLOY_DONE__'
    s.sendall(f'{cmd}; echo {marker}\n'.encode())
    time.sleep(wait)
    data = b''
    deadline = time.time() + 30
    while time.time() < deadline:
        try:
            s.settimeout(max(0.5, deadline - time.time()))
            chunk = s.recv(65536)
            if not chunk: break
            data += chunk
            if marker.encode() in data: break
        except: break
    return data.decode('latin-1', errors='replace')

# Create deploy directory
print('Creating deploy directory...')
send_cmd(f'mkdir -p {deploy_dir}')

# Upload via base64
print('Uploading binary via base64...')
b64 = open('$BINARY', 'rb').read()
import base64
encoded = base64.b64encode(b64).decode()

# Split into chunks (shell line length limit)
CHUNK_SIZE = 4000
chunks = [encoded[i:i+CHUNK_SIZE] for i in range(0, len(encoded), CHUNK_SIZE)]
print(f'Sending {len(chunks)} chunks...')

# Write to temp file
send_cmd(f'rm -f /tmp/_deploy_b64')
for i, chunk in enumerate(chunks):
    send_cmd(f'echo -n \"{chunk}\" >> /tmp/_deploy_b64', wait=0.5)
    if (i + 1) % 10 == 0:
        print(f'  chunk {i+1}/{len(chunks)}')

# Decode and deploy
print('Decoding and installing...')
send_cmd(f'base64 -d /tmp/_deploy_b64 > {deploy_dir}/{binary_name}', wait=2)
send_cmd(f'chmod +x {deploy_dir}/{binary_name}')
send_cmd(f'rm -f /tmp/_deploy_b64')

# Verify
result = send_cmd(f'ls -la {deploy_dir}/{binary_name}; md5sum {deploy_dir}/{binary_name}')
print(f'Deployed: {result.strip().split(chr(10))[-2] if chr(10) in result else result.strip()}')

s.close()
print('Done.')
"

echo ""
echo "To run on camera:"
echo "  python tools/cam_cmd.py '$DEPLOY_DIR/$1'"
