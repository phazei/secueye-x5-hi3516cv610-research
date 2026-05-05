"""Monitor camera uptime to detect reboots. Runs continuously, logs timestamps."""
import socket
import time
import sys
from datetime import datetime

def cam_uptime(ip='192.168.1.153', port=9999):
    """Get camera uptime in seconds. Returns None if unreachable."""
    try:
        s = socket.socket()
        s.settimeout(5)
        s.connect((ip, port))
        time.sleep(0.2)
        s.recv(4096)
        marker = '__END__'
        s.sendall(f'cat /proc/uptime; echo {marker}\n'.encode())
        time.sleep(0.5)
        data = b''
        while True:
            try:
                s.settimeout(1)
                chunk = s.recv(4096)
                if not chunk:
                    break
                data += chunk
                if marker.encode() in data:
                    break
            except socket.timeout:
                break
        s.close()
        text = data.decode('latin-1', errors='replace')
        # Parse uptime from first number
        for line in text.split('\n'):
            line = line.strip()
            parts = line.split()
            if len(parts) >= 1:
                try:
                    return float(parts[0])
                except ValueError:
                    continue
        return None
    except Exception:
        return None

def main():
    interval = int(sys.argv[1]) if len(sys.argv) > 1 else 30  # seconds between checks
    log_file = 'tools/uptime_log.txt'

    print(f"Monitoring camera uptime every {interval}s")
    print(f"Logging to {log_file}")
    print(f"Press Ctrl+C to stop")
    print()

    last_uptime = None
    reboot_count = 0

    with open(log_file, 'a') as f:
        f.write(f"\n--- Monitor started at {datetime.now().isoformat()} ---\n")

    while True:
        now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        uptime = cam_uptime()

        if uptime is None:
            msg = f"[{now}] UNREACHABLE (camera down or rebooting)"
        else:
            uptime_min = uptime / 60
            uptime_hr = uptime / 3600

            if last_uptime is not None and uptime < last_uptime - 5:
                reboot_count += 1
                msg = f"[{now}] *** REBOOT DETECTED *** uptime={uptime:.0f}s ({uptime_min:.1f}m) -- previous was {last_uptime:.0f}s ({last_uptime/60:.1f}m) -- reboot #{reboot_count}"
            else:
                msg = f"[{now}] uptime={uptime:.0f}s ({uptime_min:.1f}m / {uptime_hr:.2f}h)"

            last_uptime = uptime

        print(msg)
        with open(log_file, 'a') as f:
            f.write(msg + '\n')

        time.sleep(interval)

if __name__ == '__main__':
    main()
