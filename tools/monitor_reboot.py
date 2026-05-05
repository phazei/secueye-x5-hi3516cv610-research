"""
Monitor camera uptime. When a reboot is detected, immediately
read the SD card log from the previous boot (superb_prev.log)
and the tail of the current boot log.

Run this in a separate terminal:
  python tools/monitor_reboot.py
"""
import socket
import time
from datetime import datetime

INTERVAL = 20  # seconds between checks

def cam(cmd, wait=2):
    try:
        s = socket.socket()
        s.settimeout(10)
        s.connect(('192.168.1.153', 9999))
        time.sleep(0.3)
        s.recv(4096)
        marker = '__END__'
        s.sendall(f'{cmd}; echo {marker}\n'.encode())
        time.sleep(wait)
        data = b''
        while True:
            try:
                s.settimeout(2)
                chunk = s.recv(65536)
                if not chunk:
                    break
                data += chunk
                if marker.encode() in data:
                    break
            except socket.timeout:
                break
        s.close()
        text = data.decode('latin-1', errors='replace')
        idx = text.find(marker)
        if idx >= 0:
            text = text[:idx]
        lines = text.strip().split('\n')
        if lines and cmd[:15] in lines[0]:
            lines = lines[1:]
        return '\n'.join(lines).strip()
    except Exception as e:
        return None

def get_uptime():
    result = cam("cat /proc/uptime", wait=1)
    if result:
        try:
            return float(result.split()[0])
        except:
            pass
    return None

def main():
    print(f"Monitoring camera uptime every {INTERVAL}s...")
    print(f"Will capture logs when reboot is detected.")
    print(f"Press Ctrl+C to stop.\n")

    last_uptime = None
    reboot_count = 0

    while True:
        now = datetime.now().strftime('%H:%M:%S')
        uptime = get_uptime()

        if uptime is None:
            print(f"[{now}] UNREACHABLE")
        elif last_uptime is not None and uptime < last_uptime - 5:
            reboot_count += 1
            print(f"\n[{now}] *** REBOOT #{reboot_count} DETECTED ***")
            print(f"  Previous uptime: {last_uptime:.0f}s ({last_uptime/60:.1f}m)")
            print(f"  Current uptime:  {uptime:.0f}s")
            print()

            # Wait a moment for SD sync to happen
            time.sleep(10)

            # Read the previous boot's log (last 100 lines)
            print("=== PREVIOUS BOOT LOG (last 100 lines) ===")
            prev = cam("tail -100 /progs/rec/00/superb_prev.log 2>&1", wait=5)
            if prev:
                print(prev)
                # Also save to local file
                fname = f"tools/reboot_{reboot_count}_prev.log"
                with open(fname, 'w') as f:
                    full = cam("cat /progs/rec/00/superb_prev.log 2>&1", wait=10)
                    f.write(full or "no data")
                print(f"\nFull previous log saved to: {fname}")
            else:
                print("(no previous log found)")
            print()

            # Read current boot log
            print("=== CURRENT BOOT LOG (first 30 lines) ===")
            curr = cam("head -30 /tmp/superb.log 2>&1", wait=3)
            if curr:
                print(curr)
            print()

            last_uptime = uptime
        else:
            mins = uptime / 60
            hrs = uptime / 3600
            status = f"uptime={uptime:.0f}s ({mins:.1f}m)"
            if hrs >= 1:
                status += f" ({hrs:.2f}h)"
            print(f"[{now}] {status}")
            last_uptime = uptime

        time.sleep(INTERVAL)

if __name__ == '__main__':
    main()
