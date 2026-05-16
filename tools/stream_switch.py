#!/usr/bin/env python3
"""
Switch between custom and stock RTSP streams on the camera.

Usage:
    python stream_switch.py          # Interactive menu
    python stream_switch.py custom   # Switch to custom stream (live0)
    python stream_switch.py stock    # Switch to stock stream (live1)
    python stream_switch.py status   # Show current state

Custom stream: rtsp://192.168.1.153:554/live0  (our ISP pipeline)
Stock stream:  rtsp://192.168.1.153:554/live1  (superb/XMeye firmware)
"""
import socket
import time
import sys
import re

CAMERA_IP = '192.168.1.153'
SHELL_PORT = 9999
RTSP_PORT = 554


def shell_cmd(sock, c, timeout=10):
    """Send a command over the root shell and return output."""
    marker = '__XD0NE__'
    sock.sendall(f'{c}; echo {marker}\n'.encode())
    out = b''
    deadline = time.time() + timeout
    while time.time() < deadline:
        try:
            sock.settimeout(max(0.5, deadline - time.time()))
            chunk = sock.recv(65536)
            if not chunk:
                break
            out += chunk
            if marker.encode() in out:
                break
        except socket.timeout:
            break
        except OSError:
            break
    text = out.decode('latin-1')
    # Extract just the output between the echoed command and the marker
    if marker in text:
        text = text.split(marker)[0]
    # Strip ANSI escapes, prompts, and the echoed command
    text = re.sub(r'\x1b\[[0-9;]*m', '', text)
    # Remove lines that look like shell prompts
    lines = text.split('\n')
    cleaned = []
    for line in lines:
        stripped = line.strip()
        # Skip prompt lines and the echoed command itself
        if stripped.endswith('# ') or stripped.endswith('#'):
            continue
        if marker in stripped:
            continue
        if c[:20] in stripped:
            continue
        cleaned.append(stripped)
    return '\n'.join(cleaned).strip()


def connect_shell():
    """Connect to camera root shell."""
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(5)
    try:
        s.connect((CAMERA_IP, SHELL_PORT))
        time.sleep(0.3)
        s.recv(4096)  # banner
        return s
    except (socket.timeout, ConnectionRefusedError, OSError) as e:
        return None


def check_rtsp(path):
    """Check if an RTSP path responds with valid SDP."""
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(3)
        s.connect((CAMERA_IP, RTSP_PORT))
        req = (f'DESCRIBE rtsp://{CAMERA_IP}:{RTSP_PORT}/{path} RTSP/1.0\r\n'
               f'CSeq: 1\r\nAccept: application/sdp\r\n\r\n')
        s.sendall(req.encode())
        time.sleep(0.5)
        resp = s.recv(4096).decode('latin-1', errors='replace')
        s.close()
        return '200 OK' in resp and 'm=video' in resp
    except:
        return False


def get_pid(sock, name):
    """Get PID of a process by name, returns int or None."""
    out = shell_cmd(sock, f'pidof {name} 2>/dev/null')
    # Extract first number from output
    m = re.search(r'\b(\d+)\b', out)
    return int(m.group(1)) if m else None


def get_status():
    """Get current camera state."""
    s = connect_shell()
    if not s:
        return None

    pipeline_pid = get_pid(s, 'ipc_daemon') or get_pid(s, 'pipeline_test')
    superb_pid = get_pid(s, 'superb')
    uptime = shell_cmd(s, 'cat /proc/uptime').split()[0]
    s.close()

    live0 = check_rtsp('live0')
    live1 = check_rtsp('live1')

    try:
        uptime_secs = float(uptime)
        mins = int(uptime_secs) // 60
        secs = int(uptime_secs) % 60
        uptime_str = f'{mins}m {secs}s'
    except:
        uptime_str = uptime

    return {
        'pipeline_pid': pipeline_pid,
        'superb_pid': superb_pid,
        'uptime': uptime_str,
        'live0': live0,
        'live1': live1,
    }


def print_status(st):
    if not st:
        print('  Could not connect to camera.')
        return

    print(f'  Uptime: {st["uptime"]}')
    print()

    if st['pipeline_pid']:
        print(f'  ipc_daemon:    RUNNING (PID {st["pipeline_pid"]})')
    else:
        print(f'  ipc_daemon:    stopped')

    if st['superb_pid']:
        print(f'  superb:        RUNNING (PID {st["superb_pid"]})')
    else:
        print(f'  superb:        stopped')

    print()
    cust = 'ACTIVE' if st['live0'] else '  off '
    stck = 'ACTIVE' if st['live1'] else '  off '
    print(f'  live0 (custom): [{cust}]  rtsp://{CAMERA_IP}:554/live0')
    print(f'  live1 (stock):  [{stck}]  rtsp://{CAMERA_IP}:554/live1')

    if st['live0']:
        print(f'\n  >>> CUSTOM stream active')
    elif st['live1']:
        print(f'\n  >>> STOCK stream active')
    else:
        print(f'\n  >>> No stream active')


def switch_to_custom():
    """Kill superb, launch ipc_daemon via rtsp_run.sh."""
    print('Switching to CUSTOM stream...')

    st = get_status()
    if st and st['pipeline_pid'] and st['live0']:
        print('  Already active: rtsp://{CAMERA_IP}:554/live0')
        return True

    s = connect_shell()
    if not s:
        print('  Cannot connect to camera.')
        return False

    # Launch rtsp_run.sh via setsid (it SIGSTOPs mySystem, kills superb)
    print('  Launching pipeline...', end='', flush=True)
    s.sendall(b'setsid /progs/rec/00/ipc_drv/rtsp_run.sh </dev/null >/dev/null 2>&1 &\n')
    time.sleep(1)
    try:
        s.recv(4096)
    except:
        pass
    s.close()

    # Wait for RTSP to come up (~20s for init + AE stabilization)
    for i in range(30):
        time.sleep(2)
        print('.', end='', flush=True)
        if check_rtsp('live0'):
            print(' OK!')
            print(f'\n  rtsp://{CAMERA_IP}:554/live0')
            return True

    print(' TIMEOUT')
    print('  Stream did not come up. Check /progs/rec/00/ipc_drv/rtsp_pipeline.log')
    return False


def switch_to_stock():
    """Kill ipc_daemon and reboot to restore stock firmware cleanly."""
    print('Switching to STOCK stream...')

    st = get_status()
    if st and not st['pipeline_pid'] and st['live1']:
        print(f'  Already active: rtsp://{CAMERA_IP}:554/live1')
        return True

    s = connect_shell()
    if not s:
        print('  Cannot connect to camera.')
        return False

    if st and st['pipeline_pid']:
        # Kill ipc_daemon cleanly (SIGINT triggers teardown)
        print(f'  Stopping ipc_daemon (PID {st["pipeline_pid"]})...')
        shell_cmd(s, f'kill -INT {st["pipeline_pid"]}')
        time.sleep(3)

        # Verify it died, force kill if needed
        if get_pid(s, 'ipc_daemon') or get_pid(s, 'pipeline_test'):
            shell_cmd(s, f'kill -9 {st["pipeline_pid"]}')
            time.sleep(1)

    # Reboot to get a clean superb startup.
    # superb often can't recover after our pipeline releases ISP resources
    # because the kernel driver state isn't fully reset without a reboot.
    print('  Syncing filesystem and rebooting...')
    shell_cmd(s, 'sync')
    s.sendall(b'reboot\n')
    time.sleep(1)
    s.close()

    # Wait for camera to come back
    print('  Waiting for reboot...', end='', flush=True)
    time.sleep(10)  # Give it time to actually start rebooting

    for i in range(45):
        time.sleep(2)
        print('.', end='', flush=True)
        if check_rtsp('live1'):
            print(' OK!')
            print(f'\n  rtsp://{CAMERA_IP}:554/live1')
            return True

    print(' TIMEOUT')
    print('  Stock stream did not come up after reboot.')
    return False


def interactive():
    """Interactive menu."""
    while True:
        print()
        print('=' * 50)
        print('  Camera Stream Switcher')
        print('=' * 50)

        st = get_status()
        print()
        print_status(st)

        print()
        print('  [c] Switch to CUSTOM (our pipeline, live0)')
        print('  [s] Switch to STOCK  (XMeye firmware, live1)')
        print('  [r] Refresh status')
        print('  [q] Quit')
        print()

        try:
            choice = input('  > ').strip().lower()
        except (KeyboardInterrupt, EOFError):
            print()
            break

        if choice == 'c':
            switch_to_custom()
        elif choice == 's':
            switch_to_stock()
        elif choice == 'r':
            continue
        elif choice == 'q':
            break
        else:
            print('  Invalid choice.')


if __name__ == '__main__':
    if len(sys.argv) > 1:
        cmd = sys.argv[1].lower()
        if cmd == 'custom':
            switch_to_custom()
        elif cmd == 'stock':
            switch_to_stock()
        elif cmd == 'status':
            st = get_status()
            print_status(st)
        else:
            print(__doc__)
    else:
        interactive()
