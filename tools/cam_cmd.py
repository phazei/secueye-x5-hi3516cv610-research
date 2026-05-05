"""Run a command on the camera via WiFi shell (port 9999) and print output."""
import socket
import time
import sys

if len(sys.argv) < 2:
    print("Usage: python cam_cmd.py <command>")
    sys.exit(1)

command = ' '.join(sys.argv[1:])
ip = '192.168.1.153'
port = 9999

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.settimeout(10)
s.connect((ip, port))
time.sleep(0.3)
s.recv(4096)  # banner

marker = '__CMD_DONE__'
s.sendall(f'{command}; echo {marker}\n'.encode())

output = b''
deadline = time.time() + 15
while time.time() < deadline:
    try:
        s.settimeout(max(0.5, deadline - time.time()))
        chunk = s.recv(65536)
        if not chunk:
            break
        output += chunk
        if marker.encode() in output:
            break
    except socket.timeout:
        if marker.encode() in output:
            break

s.close()

text = output.decode('latin-1', errors='replace')
# Remove command echo and marker
lines = text.split('\n')
result = []
for line in lines:
    if marker in line:
        break
    if line.strip() == command:
        continue
    result.append(line)
print('\n'.join(result).strip())
