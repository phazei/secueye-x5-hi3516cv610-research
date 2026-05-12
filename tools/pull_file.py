#!/usr/bin/env python3
"""
Pull a file from the camera by having it cat the file to a TCP connection.

PC opens a listening socket, then tells the camera (via shell on port 9999)
to connect back and send the file contents.

Usage:
    python tools/pull_file.py <remote_path> [local_path]

Examples:
    python tools/pull_file.py /progs/rec/00/ipc_drv/capture.h265
    python tools/pull_file.py /progs/rec/00/ipc_drv/capture.h265 my_capture.h265
"""

import socket
import sys
import os
import time
import threading

CAMERA_IP = os.environ.get('CAMERA_IP', '192.168.1.153')
CAMERA_SHELL_PORT = int(os.environ.get('CAMERA_PORT', '9999'))
LISTEN_PORT = 7777  # PC listen port for file transfer


def get_local_ip():
    """Get local IP that the camera can reach."""
    s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    try:
        s.connect((CAMERA_IP, 80))
        return s.getsockname()[0]
    finally:
        s.close()


def cam_cmd(command, timeout=10):
    """Send a command to the camera shell."""
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(timeout)
    s.connect((CAMERA_IP, CAMERA_SHELL_PORT))
    time.sleep(0.3)
    s.recv(4096)  # banner

    s.sendall(f'{command}\n'.encode())
    time.sleep(0.5)
    s.close()


def main():
    if len(sys.argv) < 2:
        print("Usage: python tools/pull_file.py <remote_path> [local_path]")
        sys.exit(1)

    remote_path = sys.argv[1]
    local_path = sys.argv[2] if len(sys.argv) > 2 else os.path.basename(remote_path)

    local_ip = get_local_ip()
    print(f"Pulling {remote_path} -> {local_path}")
    print(f"  PC listening on {local_ip}:{LISTEN_PORT}")

    # Start listening socket
    srv = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    srv.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    srv.settimeout(30)
    srv.bind(('0.0.0.0', LISTEN_PORT))
    srv.listen(1)

    # Tell camera to send the file to us
    # Using /dev/tcp if available, otherwise fall back to busybox
    # The camera has busybox, so we use a shell TCP redirect
    send_cmd = (
        f'(cat {remote_path}) | '
        f'busybox ash -c "exec 3<>/dev/tcp/{local_ip}/{LISTEN_PORT}; '
        f'cat >&3; exec 3>&-" &'
    )

    # Some busybox builds don't have /dev/tcp -- fall back to a tiny
    # inline C program or use dd + /dev/tcp.  Let's try the simple way first,
    # and if the camera's ash doesn't support /dev/tcp, we'll use a helper.
    #
    # Actually, most busybox ash builds DO support /dev/tcp.  Let's try it.

    print(f"  Sending command to camera...")
    cam_cmd(send_cmd)

    # Accept connection from camera
    print(f"  Waiting for camera to connect...")
    try:
        conn, addr = srv.accept()
        print(f"  Connected from {addr[0]}:{addr[1]}")
    except socket.timeout:
        print("  ERROR: Camera did not connect within 30s")
        print("  Camera's ash may not support /dev/tcp")
        print("  Trying alternative method...")
        srv.close()
        # Fall back: use base64 method
        pull_via_base64(remote_path, local_path)
        return

    # Receive file data
    total = 0
    with open(local_path, 'wb') as f:
        while True:
            try:
                conn.settimeout(10)
                data = conn.recv(65536)
                if not data:
                    break
                f.write(data)
                total += len(data)
            except socket.timeout:
                break

    conn.close()
    srv.close()
    print(f"  Received {total:,} bytes -> {local_path}")


def pull_via_base64(remote_path, local_path):
    """Fallback: pull file via base64 over the shell connection."""
    import base64

    print(f"  Pulling via base64 (slower)...")
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(60)
    s.connect((CAMERA_IP, CAMERA_SHELL_PORT))
    time.sleep(0.3)
    s.recv(4096)  # banner

    marker = '__PULL_DONE__'
    s.sendall(f'base64 {remote_path}; echo {marker}\n'.encode())

    output = b''
    deadline = time.time() + 120  # 2 min timeout for large files
    while time.time() < deadline:
        try:
            s.settimeout(max(1, deadline - time.time()))
            chunk = s.recv(65536)
            if not chunk:
                break
            output += chunk
            if marker.encode() in output:
                break
        except socket.timeout:
            break

    s.close()

    # Parse: strip everything after marker, strip command echo
    text = output.decode('ascii', errors='ignore')
    # Find marker and take everything before it
    idx = text.find(marker)
    if idx >= 0:
        text = text[:idx]

    # Remove first line (command echo) and any trailing whitespace
    lines = text.strip().split('\n')
    # Skip lines that look like the command echo
    b64_lines = []
    for line in lines:
        line = line.strip()
        if not line:
            continue
        if 'base64' in line and remote_path in line:
            continue  # command echo
        b64_lines.append(line)

    b64_data = ''.join(b64_lines)
    try:
        raw = base64.b64decode(b64_data)
    except Exception as e:
        print(f"  ERROR: base64 decode failed: {e}")
        print(f"  Got {len(b64_data)} chars of base64")
        sys.exit(1)

    with open(local_path, 'wb') as f:
        f.write(raw)
    print(f"  Decoded {len(raw):,} bytes -> {local_path}")


if __name__ == '__main__':
    main()
