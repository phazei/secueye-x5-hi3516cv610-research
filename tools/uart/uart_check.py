"""Quick UART check script - runs a series of read-only commands on the camera."""
import serial
import time
import sys

sys.stdout.reconfigure(encoding='utf-8', errors='replace')

s = serial.Serial('COM3', 115200, timeout=3)
s.read(16384)  # flush

def cmd(c, delay=1.5):
    s.read(16384)
    s.write((c + '\n').encode())
    time.sleep(delay)
    data = s.read(16384)
    text = data.decode('latin-1', errors='replace').strip()
    return text

commands = [
    ('Available network daemons', 'busybox --list | grep -w -e telnet -e telnetd -e nc -e netcat -e tftp -e tftpd -e ftpd -e httpd'),
    ('debug.sh check', 'ls -la /tmp/configfs/debug.sh 2>&1; echo ENDCHECK'),
    ('grep debug in bashrc', 'grep -n debug /tmp/appfs/home/bashrc.sh'),
    ('grep telnet in startup', 'grep -n telnet /tmp/appfs/progs/startup.sh'),
    ('S00devs', 'cat /etc/init.d/S00devs'),
    ('S01udev', 'cat /etc/init.d/S01udev'),
    ('S80network', 'cat /etc/init.d/S80network'),
    ('S90hibernate', 'cat /etc/init.d/S90hibernate'),
    ('configfs contents', 'ls -la /tmp/configfs/'),
    ('proc mounts', 'cat /proc/mounts'),
]

for label, c in commands:
    print(f'=== {label} ===')
    print(cmd(c))
    print()

s.close()
