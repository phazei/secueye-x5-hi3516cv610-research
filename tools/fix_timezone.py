"""
Fix OSD timezone on SECUEYE X5 camera.

Changes timezone from UTC+8 (China) to a user-specified timezone,
updates the NTP server from the Chinese university default to a
globally-accessible server, and restarts superb to apply.

Usage:
  python tools/fix_timezone.py                    # Show current config
  python tools/fix_timezone.py --apply            # Apply PST/PDT (GMT-8) fix
  python tools/fix_timezone.py --apply --tz -500  # Apply EST (GMT-5)
  python tools/fix_timezone.py --apply --posix "EST5EDT,M3.2.0,M11.1.0" --region "America/New_York" --tz -500
  python tools/fix_timezone.py --ntp-only         # Only fix NTP server, don't change timezone
  python tools/fix_timezone.py --restore          # Restore original China timezone

The camera must be accessible on 192.168.1.153:9999 (root shell backdoor).

Timezone offset format:
  800  = UTC+8 (China Standard Time)
  -800 = UTC-8 (Pacific Standard Time)
  -500 = UTC-5 (Eastern Standard Time)
  -600 = UTC-6 (Central Standard Time)
  -700 = UTC-7 (Mountain Standard Time)
   100 = UTC+1 (Central European Time)
"""
import socket
import time
import sys
import argparse

CAM_IP = '192.168.1.153'
CAM_PORT = 9999

# Common timezone presets
PRESETS = {
    'PST':  {'tz': '-800', 'posix': 'PST8PDT,M3.2.0,M11.1.0',   'region': 'America/Los_Angeles'},
    'MST':  {'tz': '-700', 'posix': 'MST7MDT,M3.2.0,M11.1.0',   'region': 'America/Denver'},
    'CST':  {'tz': '-600', 'posix': 'CST6CDT,M3.2.0,M11.1.0',   'region': 'America/Chicago'},
    'EST':  {'tz': '-500', 'posix': 'EST5EDT,M3.2.0,M11.1.0',   'region': 'America/New_York'},
    'UTC':  {'tz': '0',    'posix': 'UTC0',                       'region': 'Etc/UTC'},
    'CET':  {'tz': '100',  'posix': 'CET-1CEST,M3.5.0,M10.5.0', 'region': 'Europe/Berlin'},
    'GMT':  {'tz': '0',    'posix': 'GMT0BST,M3.5.0/1,M10.5.0',  'region': 'Europe/London'},
    'JST':  {'tz': '900',  'posix': 'JST-9',                      'region': 'Asia/Tokyo'},
    'AEST': {'tz': '1000', 'posix': 'AEST-10AEDT,M10.1.0,M4.1.0','region': 'Australia/Sydney'},
    'CST8': {'tz': '800',  'posix': 'CST-8',                      'region': 'Asia/Shanghai'},
}

# Config file path on the camera
SYSCFG = '/etc/conf.d/syscfg/SystemCfg.ini'


def cam_exec(cmd, wait=2, timeout=15):
    """Execute a command on the camera via the root shell backdoor."""
    for attempt in range(3):
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(10)
            s.connect((CAM_IP, CAM_PORT))
            time.sleep(0.3)
            s.recv(4096)  # consume banner
            marker = '__DONE_' + str(int(time.time())) + '__'
            s.sendall(f'{cmd}; echo {marker}\n'.encode())
            time.sleep(wait)
            data = b''
            deadline = time.time() + timeout
            while time.time() < deadline:
                try:
                    s.settimeout(max(0.5, deadline - time.time()))
                    chunk = s.recv(65536)
                    if not chunk:
                        break
                    data += chunk
                    if marker.encode() in data:
                        break
                except socket.timeout:
                    if marker.encode() in data:
                        break
                    break
            s.close()
            text = data.decode('latin-1', errors='replace')
            idx = text.find(marker)
            if idx >= 0:
                text = text[:idx]
            lines = text.strip().split('\n')
            # Remove command echo if present
            if lines and cmd[:20] in lines[0]:
                lines = lines[1:]
            return '\n'.join(lines).strip()
        except Exception as e:
            if attempt < 2:
                time.sleep(3)
            else:
                return f'ERROR: {e}'


def get_cfg_values(keys):
    """Read multiple key=value pairs from SystemCfg.ini."""
    raw = cam_exec(f'cat {SYSCFG}', wait=3, timeout=20)
    if raw.startswith('ERROR'):
        print(f'  Failed to read config: {raw}')
        return {}
    results = {}
    # Config uses semicolons as delimiters
    parts = raw.replace('\n', ';').split(';')
    for key in keys:
        for p in parts:
            p = p.strip()
            if p.startswith(f'{key}='):
                results[key] = p.split('=', 1)[1]
                break
    return results


def set_cfg_value(key, value):
    """Set a key=value in SystemCfg.ini using sed."""
    # Escape forward slashes in value for sed
    escaped = value.replace('/', '\\/')
    cmd = f"sed -i 's/{key}=[^;]*/{key}={escaped}/g' {SYSCFG}"
    cam_exec(cmd, wait=2)


def show_current():
    """Display current timezone and NTP configuration."""
    keys = ['timezone', 'posixTZ', 'regionTZ', 'ntpServer', 'sntpInterval', 'bSntp']
    values = get_cfg_values(keys)

    print('Current timezone/NTP configuration:')
    print(f'  timezone    = {values.get("timezone", "?")}')
    print(f'  posixTZ     = {values.get("posixTZ", "?")}')
    print(f'  regionTZ    = {values.get("regionTZ", "?")}')
    print(f'  ntpServer   = {values.get("ntpServer", "?")}')
    print(f'  sntpInterval= {values.get("sntpInterval", "?")}')
    print(f'  bSntp       = {values.get("bSntp", "?")}')

    tz_val = values.get('timezone', '800')
    try:
        tz_num = int(tz_val)
        sign = '+' if tz_num >= 0 else '-'
        hours = abs(tz_num) // 100
        mins = abs(tz_num) % 100
        print(f'  --> UTC{sign}{hours:02d}:{mins:02d}')
    except ValueError:
        print(f'  --> Cannot parse timezone value: {tz_val}')

    # Also check system date
    date_str = cam_exec('date', wait=1)
    print(f'  System date: {date_str}')

    return values


def apply_timezone(tz_offset, posix_tz, region_tz, ntp_server='pool.ntp.org'):
    """Apply timezone and NTP configuration changes."""
    print(f'\nApplying timezone fix:')
    print(f'  timezone    -> {tz_offset}')
    print(f'  posixTZ     -> {posix_tz}')
    print(f'  regionTZ    -> {region_tz}')
    print(f'  ntpServer   -> {ntp_server}')

    # Apply changes
    print('\nWriting config...')
    set_cfg_value('timezone', tz_offset)
    set_cfg_value('posixTZ', posix_tz)
    set_cfg_value('regionTZ', region_tz)
    set_cfg_value('ntpServer', ntp_server)

    # Verify
    print('\nVerifying...')
    keys = ['timezone', 'posixTZ', 'regionTZ', 'ntpServer']
    values = get_cfg_values(keys)
    ok = True
    for key, expected in [('timezone', tz_offset), ('posixTZ', posix_tz),
                           ('regionTZ', region_tz), ('ntpServer', ntp_server)]:
        actual = values.get(key, '?')
        status = 'OK' if actual == expected else 'MISMATCH'
        if status == 'MISMATCH':
            ok = False
        print(f'  {key}: {actual} ({status})')

    if not ok:
        print('\nWARNING: Some values did not write correctly.')
        print('The SystemCfg.ini format may need different handling.')
        return False

    return True


def restart_superb():
    """Kill superb so mySystem watchdog restarts it with new config."""
    print('\nRestarting superb...')
    old_pid = cam_exec('pidof superb', wait=1)
    print(f'  Current PID: {old_pid}')

    cam_exec('kill $(pidof superb)', wait=2)
    print('  Killed. Waiting 15s for mySystem to restart it...')
    time.sleep(15)

    new_pid = cam_exec('pidof superb', wait=1)
    print(f'  New PID: {new_pid}')

    if new_pid and new_pid != old_pid and 'ERROR' not in new_pid:
        print('  superb restarted successfully.')
        return True
    else:
        print('  Waiting 10 more seconds...')
        time.sleep(10)
        new_pid = cam_exec('pidof superb', wait=1)
        print(f'  PID: {new_pid}')
        return new_pid and 'ERROR' not in new_pid


def main():
    parser = argparse.ArgumentParser(
        description='Fix OSD timezone on SECUEYE X5 camera',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Timezone presets:
  PST   UTC-8  America/Los_Angeles  (US Pacific)
  MST   UTC-7  America/Denver       (US Mountain)
  CST   UTC-6  America/Chicago      (US Central)
  EST   UTC-5  America/New_York     (US Eastern)
  UTC   UTC+0  Etc/UTC
  GMT   UTC+0  Europe/London        (with BST)
  CET   UTC+1  Europe/Berlin        (with CEST)
  JST   UTC+9  Asia/Tokyo
  AEST  UTC+10 Australia/Sydney     (with AEDT)
  CST8  UTC+8  Asia/Shanghai        (original/restore)
''')
    parser.add_argument('--apply', action='store_true',
                        help='Apply timezone changes (default: PST/PDT)')
    parser.add_argument('--preset', choices=PRESETS.keys(), default='PST',
                        help='Use a timezone preset (default: PST)')
    parser.add_argument('--tz', type=str,
                        help='Timezone offset (e.g. -800 for UTC-8)')
    parser.add_argument('--posix', type=str,
                        help='POSIX TZ string (e.g. PST8PDT,M3.2.0,M11.1.0)')
    parser.add_argument('--region', type=str,
                        help='Olson region (e.g. America/Los_Angeles)')
    parser.add_argument('--ntp', type=str, default='pool.ntp.org',
                        help='NTP server (default: pool.ntp.org)')
    parser.add_argument('--ntp-only', action='store_true',
                        help='Only fix NTP server, keep current timezone')
    parser.add_argument('--restore', action='store_true',
                        help='Restore original China timezone (UTC+8)')
    parser.add_argument('--no-restart', action='store_true',
                        help='Do not restart superb after applying changes')
    parser.add_argument('--ip', type=str, default='192.168.1.153',
                        help='Camera IP (default: 192.168.1.153)')

    args = parser.parse_args()

    global CAM_IP
    CAM_IP = args.ip

    # Always show current config first
    print('='*60)
    print('SECUEYE X5 Timezone Fix')
    print('='*60)
    current = show_current()

    if args.restore:
        args.apply = True
        args.preset = 'CST8'

    if args.ntp_only:
        print(f'\nFixing NTP server only -> {args.ntp}')
        set_cfg_value('ntpServer', args.ntp)
        # Verify
        values = get_cfg_values(['ntpServer'])
        print(f'  ntpServer: {values.get("ntpServer", "?")}')
        if not args.no_restart:
            restart_superb()
            time.sleep(5)
            print('\nAfter restart:')
            show_current()
        return

    if args.apply:
        # Determine timezone values
        if args.tz or args.posix or args.region:
            # Manual override
            preset = PRESETS.get(args.preset, PRESETS['PST'])
            tz_offset = args.tz or preset['tz']
            posix_tz = args.posix or preset['posix']
            region_tz = args.region or preset['region']
        else:
            # Use preset
            preset = PRESETS[args.preset]
            tz_offset = preset['tz']
            posix_tz = preset['posix']
            region_tz = preset['region']

        success = apply_timezone(tz_offset, posix_tz, region_tz, args.ntp)
        if success and not args.no_restart:
            restart_superb()
            time.sleep(5)
            print('\n' + '='*60)
            print('After restart:')
            print('='*60)
            show_current()
            print('\nCheck your RTSP stream OSD timestamp.')
            print('If the time is now correct but reverts later, the cloud')
            print('may be overwriting it. Block cloud access with:')
            print('  iptables -I FORWARD -s 192.168.1.153 -o $(nvram get wan_iface) -j DROP')
        elif success:
            print('\nConfig written. Restart superb or reboot camera to apply.')
    else:
        print('\nTo apply PST/PDT (UTC-8):')
        print('  python tools/fix_timezone.py --apply')
        print('\nTo apply a different timezone:')
        print('  python tools/fix_timezone.py --apply --preset EST')
        print('\nTo only fix the NTP server:')
        print('  python tools/fix_timezone.py --ntp-only')
        print('\nAvailable presets:', ', '.join(PRESETS.keys()))


if __name__ == '__main__':
    main()
