"""Deploy a file to the camera via base64 over the root shell (port 9999).

Usage: python tools/deploy_file.py <local_path> <remote_path>
Example: python tools/deploy_file.py driver/build/sensor_test /progs/rec/00/sensor_test
"""
import socket
import time
import base64
import sys
import os

if len(sys.argv) < 3:
    print("Usage: python tools/deploy_file.py <local_path> <remote_path>")
    sys.exit(1)

local_path = sys.argv[1]
remote_path = sys.argv[2]
ip = os.environ.get('CAMERA_IP', '192.168.1.153')
port = int(os.environ.get('CAMERA_PORT', '9999'))

# Read and encode binary
with open(local_path, 'rb') as f:
    data = f.read()
encoded = base64.b64encode(data).decode()
print(f"Binary: {len(data)} bytes, base64: {len(encoded)} bytes")

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.settimeout(30)
s.connect((ip, port))
time.sleep(0.3)
s.recv(4096)  # banner

def send_cmd(cmd, wait=1):
    marker = '__DONE__'
    s.sendall(f'{cmd}; echo {marker}\n'.encode())
    out = b''
    deadline = time.time() + 30
    while time.time() < deadline:
        try:
            s.settimeout(max(0.5, deadline - time.time()))
            chunk = s.recv(65536)
            if not chunk:
                break
            out += chunk
            if marker.encode() in out:
                break
        except Exception:
            break
    return out.decode('latin-1', errors='replace')

# Upload via base64 chunks
send_cmd('rm -f /tmp/_b64')
CHUNK = 4000
chunks = [encoded[i:i+CHUNK] for i in range(0, len(encoded), CHUNK)]
print(f"Sending {len(chunks)} chunks...")
for i, chunk in enumerate(chunks):
    send_cmd(f'echo -n "{chunk}" >> /tmp/_b64', wait=0.3)
    if (i + 1) % 5 == 0 or i == len(chunks) - 1:
        print(f"  {i+1}/{len(chunks)}")

# Decode and deploy
print("Decoding...")
send_cmd(f'base64 -d /tmp/_b64 > {remote_path}', wait=2)
send_cmd(f'chmod +x {remote_path}')
send_cmd('rm -f /tmp/_b64')

# Verify
result = send_cmd(f'ls -la {remote_path} && md5sum {remote_path}')
lines = result.strip().split('__DONE__')[0].strip()
print(lines)

# Local MD5 for comparison
import hashlib
local_md5 = hashlib.md5(data).hexdigest()
print(f"Local MD5:  {local_md5}")

s.close()
print("Deploy complete.")
