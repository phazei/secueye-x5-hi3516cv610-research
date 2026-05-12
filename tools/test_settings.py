#!/usr/bin/env python3
"""
Interactive SystemCfg.ini settings tester for SECUEYE X5 camera.

Tests whether editing SystemCfg.ini keys + rebooting actually affects
camera behavior. Runs one test at a time interactively so the user can
verify each change visually/functionally.

Flow per test:
  1. Read current value of the key
  2. Set the test value
  3. Verify the write persisted in the config file
  4. Full reboot the camera
  5. Wait for camera to come back online
  6. Prompt user to verify the change visually
  7. Restore the original value
  8. Reboot again to restore
  9. Record result

Usage:
  python tools/test_settings.py                # Run all tests interactively
  python tools/test_settings.py --list         # List all test cases
  python tools/test_settings.py --test 1       # Run only test #1
  python tools/test_settings.py --test 1,3,5   # Run specific tests
  python tools/test_settings.py --read-only    # Just read current values, no changes
  python tools/test_settings.py --ip 10.0.0.5  # Use different camera IP

The camera must be accessible on port 9999 (root shell backdoor).
"""

import socket
import time
import sys
import argparse
import json
from datetime import datetime

# ── Camera connection ──────────────────────────────────────────────

CAM_IP = '192.168.1.153'
CAM_PORT = 9999
SYSCFG = '/etc/conf.d/syscfg/SystemCfg.ini'

# ── Test definitions ───────────────────────────────────────────────
# Each test: (key, test_value, description, how_to_verify, section_hint)
# section_hint helps locate the key in the right [section] if needed

TESTS = [
    {
        'id': 1,
        'key': 'channelName',
        'test_value': 'TEST_CAM_123',
        'description': 'OSD channel name overlay text',
        'verify': 'Open RTSP stream (rtsp://192.168.1.153/stream0). '
                  'The bottom-right text should change from the old value to "TEST_CAM_123".',
        'category': 'OSD',
    },
    {
        'id': 2,
        'key': 'bShowOSD',
        'test_value': '0',
        'description': 'OSD visibility toggle (1=show, 0=hide)',
        'verify': 'Open RTSP stream. The OSD overlay (channel name + timestamp) should disappear entirely.',
        'category': 'OSD',
    },
    {
        'id': 3,
        'key': 'nightVisionMode',
        'test_value': '2',
        'description': 'Night vision mode (0=auto, 1=color, 2=B&W/IR)',
        'verify': 'Open RTSP stream. Image should switch to black-and-white / IR mode '
                  'regardless of ambient light. If in a lit room, the image should still be B&W.',
        'category': 'Night Vision',
    },
    {
        'id': 4,
        'key': 'IRLBrightness',
        'test_value': '10',
        'description': 'IR LED brightness (0-10, default 5)',
        'verify': 'Best tested in dark/dim conditions. IR LEDs should be visibly brighter. '
                  'Compare to the original brightness level. May need to cover camera partially.',
        'category': 'Night Vision',
    },
    {
        'id': 5,
        'key': 'IVPEnable',
        'test_value': '0',
        'description': 'IVP (AI human/vehicle detection) master switch (1=on, 0=off)',
        'verify': 'Walk in front of camera. The green detection box that normally appears around '
                  'humans should NOT appear. Check if M-prefix recordings stop being created.',
        'category': 'Detection',
    },
    {
        'id': 6,
        'key': 'IVPSensitivity',
        'test_value': '1',
        'description': 'IVP detection sensitivity (0-5, default 3)',
        'verify': 'Walk in front of camera at a distance. Detection should be less responsive '
                  'than at default sensitivity 3. May need to walk further away or move slowly.',
        'category': 'Detection',
    },
    {
        'id': 7,
        'key': 'CrossLineEnable',
        'test_value': '1',
        'description': 'Cross-line / tripwire detection (0=off, 1=on)',
        'verify': 'A tripwire line should appear on the stream (default coords: roughly center). '
                  'Walking across it should trigger an alarm (voice announcement, white LED flash, '
                  'M-prefix recording). The line uses CrossLineStartX/Y and CrossLineEndX/Y coords.',
        'category': 'Detection',
    },
    {
        'id': 8,
        'key': 'RegionDetectEnable',
        'test_value': '1',
        'description': 'Region intrusion detection (0=off, 1=on)',
        'verify': 'Entering the detection region (default: full frame 0,0 to 640,360) should '
                  'trigger an alarm. Check for voice announcement, LED flash, M-prefix recording.',
        'category': 'Detection',
    },
    {
        'id': 9,
        'key': 'bMDEnable',
        'test_value': '1',
        'description': 'Basic motion detection (0=off, 1=on)',
        'verify': 'Any motion in view should trigger detection. This is simpler pixel-diff motion '
                  'detection (not AI). Check for M-prefix recordings.',
        'category': 'Detection',
    },
    {
        'id': 10,
        'key': 'doublelight_bOutVoice',
        'test_value': '0',
        'description': 'Voice announcement on alarm (1=enabled, 0=disabled)',
        'verify': 'Trigger an alarm (walk in front of camera with IVP on). The voice announcement '
                  '"please note that you have entered the monitoring and alert area" should NOT play. '
                  'Note: IVP must be enabled for this test to be meaningful.',
        'category': 'Alarm',
    },
    {
        'id': 11,
        'key': 'AlarmLightSwitch',
        'test_value': '1',
        'description': 'Alarm strobe / white LED flash on detection (0=off, 1=on)',
        'verify': 'Trigger an alarm. The white LED should flash/strobe as a visual deterrent. '
                  'If already enabled, try setting to 0 and verify it stops.',
        'category': 'Alarm',
    },
]


# ── Camera communication ──────────────────────────────────────────

def cam_exec(cmd, wait=2, timeout=15, retries=3):
    """Execute a command on the camera via the root shell backdoor."""
    for attempt in range(retries):
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(10)
            s.connect((CAM_IP, CAM_PORT))
            time.sleep(0.3)
            s.recv(4096)  # consume banner

            marker = f'__DONE_{int(time.time())}_{attempt}__'
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
            if lines and cmd[:30] in lines[0]:
                lines = lines[1:]
            return '\n'.join(lines).strip()

        except Exception as e:
            if attempt < retries - 1:
                time.sleep(3)
            else:
                return f'ERROR: {e}'


def wait_for_camera(timeout=120):
    """Wait for camera to come back online after reboot."""
    print(f'  Waiting for camera to come back online (timeout {timeout}s)...', flush=True)
    start = time.time()
    dots = 0

    # First wait a few seconds for it to actually go down
    time.sleep(5)

    while time.time() - start < timeout:
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(3)
            s.connect((CAM_IP, CAM_PORT))
            banner = s.recv(4096)
            s.close()
            elapsed = int(time.time() - start)
            print(f'\n  Camera is back online after ~{elapsed}s')
            # Give superb a few more seconds to fully initialize
            print('  Waiting 10s for superb to fully initialize...', flush=True)
            time.sleep(10)
            return True
        except (socket.timeout, ConnectionRefusedError, OSError):
            dots += 1
            print('.', end='', flush=True)
            time.sleep(2)

    print(f'\n  TIMEOUT: Camera did not come back within {timeout}s')
    return False


def reboot_camera():
    """Send reboot command and wait for camera to come back."""
    print('  Sending reboot command...')
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        s.settimeout(5)
        s.connect((CAM_IP, CAM_PORT))
        time.sleep(0.3)
        s.recv(4096)
        s.sendall(b'reboot\n')
        time.sleep(1)
        s.close()
    except Exception as e:
        print(f'  Warning: reboot command error (expected): {e}')

    return wait_for_camera()


# ── Config read/write ─────────────────────────────────────────────

def read_all_config():
    """Read entire SystemCfg.ini from camera."""
    raw = cam_exec(f'cat {SYSCFG}', wait=3, timeout=20)
    if raw.startswith('ERROR'):
        print(f'  Failed to read config: {raw}')
        return None
    return raw


def parse_config(raw):
    """Parse semicolon-separated SystemCfg.ini into a flat dict of all key=value pairs.
    Returns list of (section, key, value) tuples to preserve context."""
    entries = []
    current_section = 'GLOBAL'
    import re

    parts = raw.replace('\n', ';').split(';')
    for p in parts:
        p = p.strip()
        if not p:
            continue
        section_match = re.match(r'^\[(.+?)\](.*)$', p)
        if section_match:
            current_section = section_match.group(1)
            remainder = section_match.group(2).strip()
            if remainder and '=' in remainder:
                key, _, value = remainder.partition('=')
                entries.append((current_section, key, value))
            continue
        if '=' in p:
            key, _, value = p.partition('=')
            entries.append((current_section, key, value))

    return entries


def find_key_value(entries, key):
    """Find all occurrences of a key in parsed config. Returns list of (section, value)."""
    results = []
    for section, k, v in entries:
        if k == key:
            results.append((section, v))
    return results


def set_config_value(key, value):
    """Set a key=value in SystemCfg.ini using sed on the camera."""
    escaped = str(value).replace('/', '\\/')
    cmd = f"sed -i 's/{key}=[^;]*/{key}={escaped}/g' {SYSCFG}"
    result = cam_exec(cmd, wait=2)
    return result


def verify_config_value(key, expected):
    """Re-read config and check if key has the expected value."""
    raw = read_all_config()
    if raw is None:
        return False, 'Could not read config'
    entries = parse_config(raw)
    matches = find_key_value(entries, key)
    if not matches:
        return False, f'Key "{key}" not found in config'

    all_match = all(v == str(expected) for _, v in matches)
    actual_values = ', '.join(f'[{s}] {v}' for s, v in matches)
    return all_match, actual_values


# ── Test execution ────────────────────────────────────────────────

def run_test(test, results):
    """Run a single interactive test."""
    test_id = test['id']
    key = test['key']
    test_value = test['test_value']

    print()
    print('=' * 70)
    print(f"  TEST #{test_id}: {test['description']}")
    print(f"  Key: {key}  |  Category: {test['category']}")
    print('=' * 70)

    # Step 1: Read current value
    print(f'\n[Step 1] Reading current value of "{key}"...')
    raw = read_all_config()
    if raw is None:
        print('  FAILED: Could not read config')
        results.append({'test': test_id, 'key': key, 'result': 'ERROR', 'note': 'Could not read config'})
        return

    entries = parse_config(raw)
    matches = find_key_value(entries, key)

    if not matches:
        print(f'  WARNING: Key "{key}" not found in SystemCfg.ini!')
        print(f'  This key may not exist in the config. Skipping test.')
        results.append({'test': test_id, 'key': key, 'result': 'SKIP', 'note': 'Key not found in config'})
        return

    original_values = matches  # list of (section, value)
    original_value = matches[0][1]  # use first match as the "original"

    print(f'  Current value(s):')
    for section, value in matches:
        print(f'    [{section}] {key} = {value}')

    if original_value == test_value:
        print(f'\n  NOTE: Current value already equals test value "{test_value}".')
        print(f'  Will set to a different value for testing, then restore.')
        # Pick an alternate test value
        if test_value in ('0', '1'):
            alt_value = '1' if test_value == '0' else '0'
        else:
            alt_value = '0'
        print(f'  Using alternate test value: {alt_value}')
        test_value = alt_value

    # Step 2: Set test value
    print(f'\n[Step 2] Setting "{key}" to "{test_value}"...')
    set_config_value(key, test_value)
    time.sleep(1)

    # Step 3: Verify write
    print(f'\n[Step 3] Verifying config write...')
    ok, detail = verify_config_value(key, test_value)
    if ok:
        print(f'  Write verified: {detail}')
    else:
        print(f'  WARNING: Value mismatch after write: {detail}')
        print(f'  The sed command may not have matched. Trying alternative approach...')
        # Some keys might have spaces or special chars
        result = cam_exec(f"grep '{key}=' {SYSCFG} | head -3", wait=2)
        print(f'  grep result: {result}')
        resp = input('\n  Continue with reboot anyway? [y/N]: ').strip().lower()
        if resp != 'y':
            # Restore original
            set_config_value(key, original_value)
            results.append({'test': test_id, 'key': key, 'result': 'SKIP', 'note': f'Write failed: {detail}'})
            return

    # Step 4: Reboot
    print(f'\n[Step 4] Rebooting camera to apply "{key}={test_value}"...')
    if not reboot_camera():
        print('  FAILED: Camera did not come back after reboot!')
        results.append({'test': test_id, 'key': key, 'result': 'ERROR', 'note': 'Camera did not reboot'})
        return

    # Step 5: Verify value persisted after reboot
    print(f'\n[Step 5] Verifying value persisted after reboot...')
    ok, detail = verify_config_value(key, test_value)
    if ok:
        print(f'  Value persisted: {detail}')
    else:
        print(f'  WARNING: Value changed after reboot: {detail}')
        print(f'  The camera or superb may have overwritten this key on startup.')

    # Step 6: User verification
    print(f'\n[Step 6] VERIFY THE CHANGE')
    print(f'  ┌─────────────────────────────────────────────────────────────┐')
    for line in test['verify'].split('. '):
        line = line.strip()
        if line:
            if not line.endswith('.'):
                line += '.'
            # Word wrap at ~57 chars
            while len(line) > 57:
                wrap = line[:57].rfind(' ')
                if wrap == -1:
                    wrap = 57
                print(f'  │  {line[:wrap]:<57} │')
                line = line[wrap:].strip()
            print(f'  │  {line:<57} │')
    print(f'  └─────────────────────────────────────────────────────────────┘')

    print(f'\n  Old value: {original_value}')
    print(f'  New value: {test_value}')
    print()

    while True:
        resp = input('  Did the setting take effect? [y]es / [n]o / [u]nsure / [s]kip: ').strip().lower()
        if resp in ('y', 'yes'):
            result = 'WORKS'
            break
        elif resp in ('n', 'no'):
            result = 'NO_EFFECT'
            break
        elif resp in ('u', 'unsure'):
            result = 'UNSURE'
            break
        elif resp in ('s', 'skip'):
            result = 'SKIP'
            break
        else:
            print('  Please enter y, n, u, or s.')

    note = ''
    if result != 'SKIP':
        note = input('  Any notes? (press Enter to skip): ').strip()

    results.append({
        'test': test_id,
        'key': key,
        'original': original_value,
        'test_value': test_value,
        'result': result,
        'note': note,
    })

    # Step 7: Restore original value
    print(f'\n[Step 7] Restoring original value: {key}={original_value}')
    set_config_value(key, original_value)
    time.sleep(1)
    ok, detail = verify_config_value(key, original_value)
    if ok:
        print(f'  Restored: {detail}')
    else:
        print(f'  WARNING: Restore may have failed: {detail}')

    # Step 8: Reboot to restore
    print(f'\n[Step 8] Rebooting to restore original state...')
    if not reboot_camera():
        print('  WARNING: Camera did not come back after restore reboot!')
    else:
        print('  Camera restored to original state.')

    print(f'\n  Result: {key} -> {result}' + (f' ({note})' if note else ''))


def read_only_mode():
    """Just read and display current values for all test keys."""
    print('Reading current values for all test keys...\n')

    raw = read_all_config()
    if raw is None:
        print('ERROR: Could not read SystemCfg.ini')
        return

    entries = parse_config(raw)

    current_category = None
    for test in TESTS:
        if test['category'] != current_category:
            current_category = test['category']
            print(f'\n  [{current_category}]')
            print(f'  {"-" * 60}')

        matches = find_key_value(entries, test['key'])
        if matches:
            # Deduplicate -- show unique section:value pairs
            seen = set()
            unique = []
            for section, value in matches:
                sv = (section, value)
                if sv not in seen:
                    seen.add(sv)
                    unique.append(sv)
            if len(unique) == 1:
                section, value = unique[0]
                print(f'  #{test["id"]:2d}  {test["key"]:30s} = {value:15s}  ({test["description"]})')
            else:
                print(f'  #{test["id"]:2d}  {test["key"]:30s}                  ({test["description"]})')
                for section, value in unique:
                    print(f'       {"":30s}   [{section}] = {value}')
        else:
            print(f'  #{test["id"]:2d}  {test["key"]:30s} = NOT FOUND       ({test["description"]})')


def print_results(results):
    """Print summary of all test results."""
    print()
    print('=' * 70)
    print('  TEST RESULTS SUMMARY')
    print('=' * 70)
    print()
    print(f'  {"#":>3}  {"Key":<30}  {"Result":<12}  Notes')
    print(f'  {"─"*3}  {"─"*30}  {"─"*12}  {"─"*20}')

    works = 0
    no_effect = 0
    for r in results:
        marker = ''
        if r['result'] == 'WORKS':
            marker = '[OK]'
            works += 1
        elif r['result'] == 'NO_EFFECT':
            marker = '[--]'
            no_effect += 1
        elif r['result'] == 'UNSURE':
            marker = '[??]'
        elif r['result'] == 'SKIP':
            marker = '[..]'
        elif r['result'] == 'ERROR':
            marker = '[!!]'

        note = r.get('note', '')
        print(f'  {r["test"]:>3}  {r["key"]:<30}  {marker + " " + r["result"]:<12}  {note}')

    print()
    print(f'  Summary: {works} working, {no_effect} no effect, '
          f'{len(results) - works - no_effect} other')
    print()

    # Save results to file
    timestamp = datetime.now().strftime('%Y%m%d_%H%M%S')
    results_file = f'tools/test_results_{timestamp}.json'
    try:
        with open(results_file, 'w') as f:
            json.dump({
                'timestamp': timestamp,
                'camera_ip': CAM_IP,
                'results': results,
            }, f, indent=2)
        print(f'  Results saved to: {results_file}')
    except Exception as e:
        print(f'  Could not save results file: {e}')


# ── Main ──────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(
        description='Interactive SystemCfg.ini settings tester for SECUEYE X5',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Each test:
  1. Reads the current value
  2. Sets a test value via sed
  3. Reboots the camera (full reboot)
  4. Waits for it to come back
  5. Prompts you to verify visually
  6. Restores the original value
  7. Reboots again to restore

This is slow but thorough -- each test takes ~2-3 minutes due to reboots.
''')
    parser.add_argument('--list', action='store_true',
                        help='List all test cases without running them')
    parser.add_argument('--test', type=str,
                        help='Run specific test(s) by ID, e.g. --test 1 or --test 1,3,5')
    parser.add_argument('--read-only', action='store_true',
                        help='Just read current values, no changes')
    parser.add_argument('--ip', type=str, default='192.168.1.153',
                        help='Camera IP (default: 192.168.1.153)')

    args = parser.parse_args()

    global CAM_IP
    CAM_IP = args.ip

    if args.list:
        print('Available tests:\n')
        current_category = None
        for t in TESTS:
            if t['category'] != current_category:
                current_category = t['category']
                print(f'\n  [{current_category}]')
            print(f'  #{t["id"]:2d}  {t["key"]:<30s}  {t["description"]}')
        print(f'\n  Total: {len(TESTS)} tests')
        return

    # Verify connectivity
    print('SECUEYE X5 Settings Tester')
    print('=' * 70)
    print(f'Camera: {CAM_IP}:{CAM_PORT}')
    print()

    print('Checking camera connectivity...')
    result = cam_exec('echo OK', wait=1, timeout=5)
    if 'OK' not in result:
        print(f'ERROR: Cannot reach camera at {CAM_IP}:{CAM_PORT}')
        print(f'Response: {result}')
        sys.exit(1)
    print('  Camera is reachable.')

    uptime = cam_exec('cat /proc/uptime', wait=1, timeout=5)
    print(f'  Uptime: {uptime}')

    if args.read_only:
        read_only_mode()
        return

    # Determine which tests to run
    if args.test:
        test_ids = [int(x.strip()) for x in args.test.split(',')]
        tests_to_run = [t for t in TESTS if t['id'] in test_ids]
        if not tests_to_run:
            print(f'No tests found with IDs: {test_ids}')
            print(f'Valid IDs: {[t["id"] for t in TESTS]}')
            return
    else:
        tests_to_run = TESTS

    print(f'\nWill run {len(tests_to_run)} test(s).')
    print('Each test involves 2 reboots (~2-3 min per test).')
    print(f'Estimated total time: ~{len(tests_to_run) * 3} minutes.')
    print()

    resp = input('Ready to begin? [Y/n]: ').strip().lower()
    if resp == 'n':
        print('Aborted.')
        return

    results = []
    for i, test in enumerate(tests_to_run):
        print(f'\n{"─" * 70}')
        print(f'  Test {i+1} of {len(tests_to_run)}')
        run_test(test, results)

        if i < len(tests_to_run) - 1:
            print()
            resp = input('  Continue to next test? [Y/n/q]: ').strip().lower()
            if resp in ('n', 'q'):
                print('  Stopping test run.')
                break

    print_results(results)


if __name__ == '__main__':
    main()
