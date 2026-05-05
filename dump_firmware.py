#!/usr/bin/env python3
"""
dump_firmware.py -- Dump all flash partitions from the camera over WiFi.

Connects to the tcpsvd root shell on port 9999 and reads each MTD partition
using dd + base64, then decodes and verifies with md5sum.

Outputs to firmware/ directory:
  mtd0_boot.bin       (320KB)   U-Boot bootloader
  mtd1_bootargs.bin   (64KB)    U-Boot environment
  mtd2_kernel.bin     (2MB)     Linux kernel (FIT image)
  mtd3_rootfs.bin     (1.25MB)  Root filesystem (squashfs)
  mtd4_appfs.bin      (5MB)     Application filesystem (squashfs)
  mtd5_configfs.bin   (1MB)     Config partition (jffs2, writable)
  mtd6_resfs.bin      (6.375MB) Resource filesystem (squashfs)
  full_flash.bin      (16MB)    Complete SPI NOR flash image

Usage:
  python dump_firmware.py [--ip 192.168.1.153] [--port 9999]
"""
import socket
import base64
import hashlib
import os
import sys
import time
import argparse
import subprocess


# MTD partition layout from /proc/mtd
PARTITIONS = [
    # (mtdblock, name,      hex_size,  bytes)
    (0, 'boot',      0x050000,   327680),
    (1, 'bootargs',  0x010000,    65536),
    (2, 'kernel',    0x200000,  2097152),
    (3, 'rootfs',    0x140000,  1310720),
    (4, 'appfs',     0x500000,  5242880),
    (5, 'configfs',  0x100000,  1048576),
    (6, 'resfs',     0x660000,  6684672),
]

TOTAL_FLASH = 16 * 1024 * 1024  # 16MB


def find_camera_ip():
    """Try to find the camera on the local network."""
    known_mac = '38:77:07:75:97:39'
    known_ip = '192.168.1.153'

    # Quick check: is the known IP responding on port 9999?
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(2)
        s.connect((known_ip, 9999))
        s.close()
        return known_ip
    except (socket.timeout, ConnectionRefusedError, OSError):
        pass

    # Try ARP table first (fast, no scanning needed)
    print('[..] Known IP not responding, searching ARP table...')
    try:
        result = subprocess.run(['arp', '-a'], capture_output=True, text=True, timeout=5)
        for line in result.stdout.splitlines():
            if '38-77-07' in line.lower() or '38:77:07' in line.lower():
                # Extract IP from ARP line
                parts = line.split()
                for part in parts:
                    if part.count('.') == 3 and part[0].isdigit():
                        ip = part.strip('()')
                        print(f'[OK] Found camera at {ip} (from ARP table)')
                        return ip
    except Exception:
        pass

    # Scan common subnet for port 9999
    print('[..] Scanning 192.168.1.0/24 for port 9999...')
    for i in range(1, 255):
        ip = f'192.168.1.{i}'
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(0.3)
            s.connect((ip, 9999))
            s.close()
            print(f'[OK] Found camera at {ip}')
            return ip
        except (socket.timeout, ConnectionRefusedError, OSError):
            continue

    return None


class CameraShell:
    """Manage a TCP connection to the camera's tcpsvd root shell."""

    def __init__(self, ip, port=9999):
        self.ip = ip
        self.port = port
        self.sock = None

    def connect(self):
        self.sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.sock.settimeout(30)
        self.sock.connect((self.ip, self.port))
        time.sleep(0.5)
        self.sock.recv(4096)  # consume banner

    def close(self):
        if self.sock:
            self.sock.close()
            self.sock = None

    def run(self, cmd, timeout=10):
        """Run a command and return the output (stripped of echo and prompt)."""
        # Use a unique end marker to know when output is complete
        marker = f'__END_{int(time.time()*1000)}__'
        full_cmd = f'{cmd}; echo {marker}\n'
        self.sock.sendall(full_cmd.encode())

        output = b''
        deadline = time.time() + timeout
        while time.time() < deadline:
            try:
                self.sock.settimeout(max(0.5, deadline - time.time()))
                chunk = self.sock.recv(65536)
                if not chunk:
                    break
                output += chunk
                if marker.encode() in output:
                    break
            except socket.timeout:
                continue

        text = output.decode('latin-1', errors='replace')
        # Remove the command echo (first line) and the marker line and prompt
        lines = text.split('\n')
        result_lines = []
        for line in lines:
            stripped = line.strip()
            if stripped == cmd or stripped.startswith(cmd[:20]):
                continue
            if marker in stripped:
                continue
            if stripped.endswith('# ') or stripped.endswith('#'):
                # Prompt line -- strip the prompt but keep content before it
                if '# ' in stripped and not stripped.startswith('/'):
                    continue
                # Could be a path line from output, keep it
            result_lines.append(line)

        return '\n'.join(result_lines).strip()

    def get_md5(self, path):
        """Get md5sum of a file/device on the camera."""
        result = self.run(f'md5sum {path}', timeout=60)
        # md5sum output: "hash  filename"
        for line in result.splitlines():
            parts = line.strip().split()
            if len(parts) >= 1 and len(parts[0]) == 32:
                return parts[0]
        return None

    def dump_partition_base64(self, device, size):
        """Dump a device via base64, return raw bytes."""
        # Use a block size that produces clean base64 lines (divisible by 3)
        # and read in chunks to avoid overwhelming the shell buffer
        chunk_size = 65536  # 64KB chunks
        num_chunks = (size + chunk_size - 1) // chunk_size

        all_b64 = b''
        marker = '__DUMP_DONE__'

        # Single command: dd the entire partition, base64 encode it
        cmd = f'dd if={device} bs={size} count=1 2>/dev/null | base64; echo {marker}\n'
        self.sock.sendall(cmd.encode())

        # Collect all output until marker
        output = b''
        deadline = time.time() + 300  # 5 min max per partition
        while time.time() < deadline:
            try:
                self.sock.settimeout(5)
                chunk = self.sock.recv(262144)
                if not chunk:
                    break
                output += chunk
                if marker.encode() in output:
                    break
            except socket.timeout:
                # Check if we already have the marker
                if marker.encode() in output:
                    break
                continue

        # Parse: skip command echo, extract base64 lines, stop at marker
        text = output.decode('ascii', errors='replace')
        lines = text.split('\n')
        b64_lines = []
        capture = False
        for line in lines:
            stripped = line.strip()
            if marker in stripped:
                break
            # Skip command echo
            if 'dd if=' in stripped or stripped.startswith(device):
                capture = True
                continue
            if capture or (stripped and all(c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=' for c in stripped)):
                if stripped and len(stripped) > 0:
                    # Filter out any prompt contamination
                    clean = ''
                    for c in stripped:
                        if c in 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=':
                            clean += c
                    if clean:
                        b64_lines.append(clean)
                        capture = True

        b64_data = ''.join(b64_lines)
        try:
            raw = base64.b64decode(b64_data)
            return raw
        except Exception as e:
            print(f'  base64 decode error: {e}')
            print(f'  got {len(b64_data)} base64 chars, first 200: {b64_data[:200]}')
            return None


def main():
    parser = argparse.ArgumentParser(description='Dump camera firmware over WiFi')
    parser.add_argument('--ip', default=None, help='Camera IP (auto-detect if omitted)')
    parser.add_argument('--port', type=int, default=9999, help='Shell port (default 9999)')
    parser.add_argument('--output', default='firmware', help='Output directory')
    args = parser.parse_args()

    # Find camera
    if args.ip:
        ip = args.ip
    else:
        print('[..] Auto-detecting camera IP...')
        ip = find_camera_ip()
        if not ip:
            print('[!!] Camera not found on network.')
            print('     Make sure the camera is powered on and connected to WiFi.')
            print('     Try: python dump_firmware.py --ip <camera_ip>')
            sys.exit(1)

    print(f'[OK] Camera at {ip}:{args.port}')

    # Create output directory
    os.makedirs(args.output, exist_ok=True)

    # Connect
    print('[..] Connecting to shell...')
    shell = CameraShell(ip, args.port)
    shell.connect()

    # Verify we have root
    whoami = shell.run('id')
    if 'uid=0' not in whoami:
        print(f'[!!] Not root! Got: {whoami}')
        shell.close()
        sys.exit(1)
    print('[OK] Root shell confirmed')

    # Verify MTD layout
    mtd_info = shell.run('cat /proc/mtd')
    print(f'\n{mtd_info}\n')

    results = []
    total_start = time.time()

    # Dump each partition
    for mtd_num, name, hex_size, byte_size in PARTITIONS:
        device = f'/dev/mtdblock{mtd_num}'
        outfile = os.path.join(args.output, f'mtd{mtd_num}_{name}.bin')
        size_kb = byte_size / 1024
        size_str = f'{size_kb:.0f}KB' if size_kb < 1024 else f'{size_kb/1024:.1f}MB'

        print(f'[{mtd_num+1}/7] Dumping {name} ({device}, {size_str})...')

        # Get on-device md5
        print(f'  Computing on-device md5sum...')
        remote_md5 = shell.get_md5(device)
        print(f'  Remote MD5: {remote_md5}')

        # Dump via base64
        print(f'  Transferring...')
        t0 = time.time()
        data = shell.dump_partition_base64(device, byte_size)
        elapsed = time.time() - t0

        if data is None:
            print(f'  [!!] FAILED to dump {name}')
            results.append((name, 'FAILED', None, None, None))
            # Reconnect for next partition
            shell.close()
            time.sleep(1)
            shell.connect()
            continue

        # Verify size
        if len(data) != byte_size:
            print(f'  [!!] Size mismatch: got {len(data)}, expected {byte_size}')
            # Might still be usable, save it anyway
        else:
            print(f'  Size OK: {len(data)} bytes')

        # Save
        with open(outfile, 'wb') as f:
            f.write(data)

        # Local md5
        local_md5 = hashlib.md5(data).hexdigest()
        print(f'  Local MD5:  {local_md5}')

        # Compare
        if remote_md5 and local_md5 == remote_md5:
            status = 'VERIFIED'
            print(f'  [OK] MD5 match -- verified!')
        elif remote_md5:
            status = 'MISMATCH'
            print(f'  [!!] MD5 MISMATCH -- dump may be corrupt!')
        else:
            status = 'UNVERIFIED'
            print(f'  [??] Could not get remote MD5')

        rate = len(data) / elapsed / 1024 if elapsed > 0 else 0
        print(f'  Saved: {outfile} ({elapsed:.1f}s, {rate:.0f} KB/s)')
        results.append((name, status, len(data), remote_md5, local_md5))
        print()

    # Now dump the full flash as a single contiguous image
    print('[..] Assembling full flash image from partitions...')
    full_image = bytearray()
    all_verified = True
    for mtd_num, name, hex_size, byte_size in PARTITIONS:
        part_file = os.path.join(args.output, f'mtd{mtd_num}_{name}.bin')
        if os.path.exists(part_file):
            with open(part_file, 'rb') as f:
                part_data = f.read()
            if len(part_data) == byte_size:
                full_image.extend(part_data)
            else:
                print(f'  [!!] {name}: size {len(part_data)} != expected {byte_size}, padding...')
                full_image.extend(part_data)
                full_image.extend(b'\xff' * (byte_size - len(part_data)))
                all_verified = False
        else:
            print(f'  [!!] {name}: file missing, filling with 0xFF')
            full_image.extend(b'\xff' * byte_size)
            all_verified = False

    full_file = os.path.join(args.output, 'full_flash.bin')
    with open(full_file, 'wb') as f:
        f.write(full_image)
    full_md5 = hashlib.md5(full_image).hexdigest()
    print(f'[OK] Full flash image: {full_file} ({len(full_image)} bytes, md5: {full_md5})')

    shell.close()

    total_elapsed = time.time() - total_start

    # Summary
    print()
    print('=' * 65)
    print('FIRMWARE DUMP SUMMARY')
    print('=' * 65)
    print(f'{"Partition":<12} {"Status":<12} {"Size":>10} {"MD5 Match":>10}')
    print('-' * 65)
    for name, status, size, remote_md5, local_md5 in results:
        size_str = f'{size:,}' if size else 'N/A'
        print(f'{name:<12} {status:<12} {size_str:>10} {"YES" if status == "VERIFIED" else "NO" if status == "MISMATCH" else "??":>10}')

    print('-' * 65)
    print(f'Full image:  {len(full_image):,} bytes ({len(full_image)/1024/1024:.1f} MB)')
    print(f'Total time:  {total_elapsed:.0f}s')
    print(f'Output dir:  {os.path.abspath(args.output)}')
    print()

    # Write checksums file
    checksums_file = os.path.join(args.output, 'checksums.md5')
    with open(checksums_file, 'w') as f:
        for mtd_num, name, hex_size, byte_size in PARTITIONS:
            part_file = f'mtd{mtd_num}_{name}.bin'
            part_path = os.path.join(args.output, part_file)
            if os.path.exists(part_path):
                with open(part_path, 'rb') as pf:
                    md5 = hashlib.md5(pf.read()).hexdigest()
                f.write(f'{md5}  {part_file}\n')
        f.write(f'{full_md5}  full_flash.bin\n')
    print(f'Checksums:   {checksums_file}')

    verified_count = sum(1 for _, s, _, _, _ in results if s == 'VERIFIED')
    total_count = len(results)
    if verified_count == total_count:
        print(f'\n[OK] All {total_count} partitions verified successfully!')
        print('     You now have a complete backup. Safe to proceed with modifications.')
    else:
        failed = [name for name, s, _, _, _ in results if s != 'VERIFIED']
        print(f'\n[!!] {total_count - verified_count} partition(s) need attention: {", ".join(failed)}')
        print('     Consider re-running the dump for failed partitions.')


if __name__ == '__main__':
    main()
