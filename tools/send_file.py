#!/usr/bin/env python3
"""
Send a file to the camera's recv daemon over raw TCP.

Usage:
    python send_file.py <host> <port> <local_file> [remote_name]

    # Send one file (recv in one-shot mode):
    python send_file.py 192.168.1.153 8888 build/pipeline_test

    # Send to daemon mode (recv -d):
    python send_file.py 192.168.1.153 8888 build/pipeline_test pipeline_test

    # Batch send multiple files:
    python send_file.py 192.168.1.153 8888 file1.so file2.so file3.bin

If remote_name is provided, daemon protocol is used (filename\n + data).
If multiple files are given, daemon protocol is used with each filename.
"""

import socket
import sys
import os
import time


def send_file_simple(host, port, filepath):
    """Send raw bytes (one-shot mode on receiver)."""
    data = open(filepath, 'rb').read()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(10)
    s.connect((host, port))
    s.sendall(data)
    s.close()
    print(f"  sent {len(data):,} bytes")


def send_file_daemon(host, port, filepath, remote_name):
    """Send with filename header (daemon mode on receiver)."""
    data = open(filepath, 'rb').read()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(10)
    s.connect((host, port))
    # Send filename + newline + raw data
    s.sendall(remote_name.encode() + b'\n')
    s.sendall(data)
    s.close()
    print(f"  sent {remote_name} ({len(data):,} bytes)")


def main():
    if len(sys.argv) < 4:
        print("Usage: send_file.py <host> <port> <file1> [file2] [file3] ...")
        print("   or: send_file.py <host> <port> <file> <remote_name>")
        sys.exit(1)

    host = sys.argv[1]
    port = int(sys.argv[2])
    files = sys.argv[3:]

    # If exactly 2 args after port and second doesn't exist as file,
    # treat as: file + remote_name
    if len(files) == 2 and not os.path.exists(files[1]):
        filepath = files[0]
        remote_name = files[1]
        if not os.path.exists(filepath):
            print(f"Error: {filepath} not found")
            sys.exit(1)
        print(f"Sending {filepath} -> {remote_name}")
        send_file_daemon(host, port, filepath, remote_name)
        return

    # Multiple files or single file: use daemon protocol with basename
    for filepath in files:
        if not os.path.exists(filepath):
            print(f"Error: {filepath} not found")
            sys.exit(1)

    total_bytes = 0
    t0 = time.time()

    for filepath in files:
        remote_name = os.path.basename(filepath)
        print(f"Sending {filepath} -> {remote_name}")
        send_file_daemon(host, port, filepath, remote_name)
        total_bytes += os.path.getsize(filepath)
        time.sleep(0.1)  # Brief pause between files

    elapsed = time.time() - t0
    if elapsed > 0:
        speed = total_bytes / elapsed / 1024
        print(f"\nDone: {len(files)} files, {total_bytes:,} bytes in {elapsed:.1f}s ({speed:.0f} KB/s)")


if __name__ == '__main__':
    main()
