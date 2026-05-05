"""Re-dump boot (mtd0) and resfs (mtd6) using xxd hex encoding for reliability."""
import socket
import time
import base64
import hashlib
import sys
import os

IP = '192.168.1.153'
PORT = 9999
OUTPUT_DIR = 'firmware'


def connect():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(30)
    s.connect((IP, PORT))
    time.sleep(0.5)
    s.recv(4096)  # banner
    return s


def dump_partition(device, size, label):
    """Dump a partition using dd + xxd -p (plain hex), in 256KB chunks."""
    chunk_size = 262144  # 256KB
    num_chunks = (size + chunk_size - 1) // chunk_size
    all_data = bytearray()

    s = connect()

    for i in range(num_chunks):
        remaining = min(chunk_size, size - (i * chunk_size))
        print(f'  {label} chunk {i+1}/{num_chunks} ({remaining} bytes)...', end=' ', flush=True)

        marker = f'HEXEND{i:04d}'
        cmd = f'dd if={device} bs={chunk_size} skip={i} count=1 2>/dev/null | xxd -p -c 256; echo {marker}\n'
        s.sendall(cmd.encode())

        out = b''
        deadline = time.time() + 60
        while time.time() < deadline:
            try:
                s.settimeout(5)
                data = s.recv(262144)
                if not data:
                    break
                out += data
                if marker.encode() in out:
                    break
            except socket.timeout:
                if marker.encode() in out:
                    break

        text = out.decode('ascii', errors='replace')
        hex_str = ''
        for line in text.split('\n'):
            stripped = line.strip()
            if marker in stripped:
                break
            cleaned = ''.join(c for c in stripped if c in '0123456789abcdef')
            if cleaned:
                hex_str += cleaned

        try:
            decoded = bytes.fromhex(hex_str)
            all_data.extend(decoded)
            print(f'{len(decoded)} bytes (total: {len(all_data)})')
        except Exception as e:
            print(f'ERROR: {e}, hex_len={len(hex_str)}')
            # Try to salvage: trim to even length
            if len(hex_str) % 2 == 1:
                hex_str = hex_str[:-1]
            try:
                decoded = bytes.fromhex(hex_str)
                all_data.extend(decoded)
                print(f'  salvaged {len(decoded)} bytes')
            except Exception:
                print(f'  could not salvage, aborting')
                break

    s.close()
    return bytes(all_data)


def get_remote_md5(device):
    """Get md5sum from device."""
    s = connect()
    marker = 'MD5ENDMARK'
    s.sendall(f'md5sum {device}; echo {marker}\n'.encode())
    out = b''
    deadline = time.time() + 60
    while time.time() < deadline:
        try:
            s.settimeout(5)
            out += s.recv(4096)
            if marker.encode() in out:
                break
        except socket.timeout:
            if marker.encode() in out:
                break
    s.close()

    text = out.decode('latin-1')
    for line in text.split('\n'):
        parts = line.strip().split()
        if len(parts) >= 1 and len(parts[0]) == 32:
            if all(c in '0123456789abcdef' for c in parts[0]):
                return parts[0]
    return None


def dump_and_verify(mtd_num, name, size):
    device = f'/dev/mtdblock{mtd_num}'
    outfile = os.path.join(OUTPUT_DIR, f'mtd{mtd_num}_{name}.bin')
    size_str = f'{size/1024:.0f}KB' if size < 1048576 else f'{size/1048576:.1f}MB'

    print(f'\n=== Dumping {name} ({device}, {size_str}) ===')

    # Get remote md5 first
    print(f'  Getting remote md5sum...')
    remote_md5 = get_remote_md5(device)
    print(f'  Remote MD5: {remote_md5}')

    # Dump
    data = dump_partition(device, size, name)
    print(f'  Total: {len(data)} bytes (expected {size})')

    if len(data) != size:
        print(f'  [!!] Size mismatch!')
        return False

    local_md5 = hashlib.md5(data).hexdigest()
    print(f'  Local MD5:  {local_md5}')

    with open(outfile, 'wb') as f:
        f.write(data)
    print(f'  Saved: {outfile}')

    if remote_md5 and local_md5 == remote_md5:
        print(f'  [OK] VERIFIED!')
        return True
    elif remote_md5:
        print(f'  [!!] MD5 MISMATCH!')
        return False
    else:
        print(f'  [??] Could not get remote md5 for verification')
        return None


def main():
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    # Test connection
    print('Connecting to camera...')
    s = connect()
    s.sendall(b'id\n')
    time.sleep(0.5)
    resp = s.recv(4096).decode('latin-1')
    if 'uid=0' not in resp:
        print(f'Not root! Got: {resp}')
        sys.exit(1)
    print('Root shell confirmed.')
    s.close()

    # Re-dump boot and resfs
    r1 = dump_and_verify(0, 'boot', 0x050000)
    r2 = dump_and_verify(6, 'resfs', 0x660000)

    # Rebuild full flash image
    print('\n=== Rebuilding full flash image ===')
    partitions = [
        (0, 'boot',     0x050000),
        (1, 'bootargs', 0x010000),
        (2, 'kernel',   0x200000),
        (3, 'rootfs',   0x140000),
        (4, 'appfs',    0x500000),
        (5, 'configfs', 0x100000),
        (6, 'resfs',    0x660000),
    ]

    full_image = bytearray()
    all_ok = True
    for mtd_num, name, size in partitions:
        path = os.path.join(OUTPUT_DIR, f'mtd{mtd_num}_{name}.bin')
        if os.path.exists(path):
            with open(path, 'rb') as f:
                data = f.read()
            if len(data) == size:
                full_image.extend(data)
                print(f'  {name}: {len(data)} bytes OK')
            else:
                print(f'  {name}: SIZE WRONG ({len(data)} != {size})')
                all_ok = False
                full_image.extend(data)
                full_image.extend(b'\xff' * (size - len(data)))
        else:
            print(f'  {name}: FILE MISSING')
            all_ok = False
            full_image.extend(b'\xff' * size)

    full_path = os.path.join(OUTPUT_DIR, 'full_flash.bin')
    with open(full_path, 'wb') as f:
        f.write(full_image)

    full_md5 = hashlib.md5(full_image).hexdigest()
    print(f'\nFull flash: {len(full_image)} bytes, MD5: {full_md5}')
    print(f'Saved: {full_path}')

    # Update checksums
    checksums_path = os.path.join(OUTPUT_DIR, 'checksums.md5')
    with open(checksums_path, 'w') as f:
        for mtd_num, name, size in partitions:
            path = os.path.join(OUTPUT_DIR, f'mtd{mtd_num}_{name}.bin')
            if os.path.exists(path):
                with open(path, 'rb') as pf:
                    md5 = hashlib.md5(pf.read()).hexdigest()
                f.write(f'{md5}  mtd{mtd_num}_{name}.bin\n')
        f.write(f'{full_md5}  full_flash.bin\n')
    print(f'Checksums: {checksums_path}')

    if all_ok:
        print('\n[OK] All partitions present and correct size.')
        print('     Full firmware backup complete!')
    else:
        print('\n[!!] Some partitions had issues. Check output above.')


if __name__ == '__main__':
    main()
