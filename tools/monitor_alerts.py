#!/usr/bin/env python3
"""
Real-time alarm/detection event monitor for SECUEYE X5 camera.

Monitors superb.log via root shell for alarm events. When an alarm fires,
the camera logs "start maudio_speaker" (voice prompt), creates a snapshot,
and reconfigures IVP tracking. This script detects those patterns in
real-time and can optionally fire a webhook.

Confirmed alarm indicators (from testing 2026-05-04):
  - "start maudio_speaker" -- voice alarm plays (PRIMARY indicator)
  - "Create snap" -- alarm snapshot captured
  - "goto preset" -- PTZ preset triggered (alarm-linked)
  - "mivp_set_param" after alarm -- IVP reconfiguration burst

ONVIF PullPoint events were tested and confirmed NON-FUNCTIONAL for
alarm detection on this firmware (events never fire).

Usage:
  python tools/monitor_alerts.py                    # Monitor indefinitely
  python tools/monitor_alerts.py --duration 300     # Monitor for 5 minutes
  python tools/monitor_alerts.py --show-all         # Show all log lines
  python tools/monitor_alerts.py --webhook URL      # POST to webhook on alarm
  python tools/monitor_alerts.py --webhook-json '{"text":"alarm!"}'
                                                    # Custom JSON body

The camera must be accessible on port 9999 (root shell backdoor).
Press Ctrl+C to stop.
"""

import socket
import time
import sys
import re
import argparse
import threading
import json
from datetime import datetime
from urllib.parse import urlparse

try:
    import urllib.request
    HAS_URLLIB = True
except ImportError:
    HAS_URLLIB = False


CAM_IP = '192.168.1.153'
CAM_PORT = 9999

# ── Alarm detection patterns ──────────────────────────────────────
# Organized by confidence level

# PRIMARY: Unambiguous alarm indicators -- these ONLY fire during an alarm
PRIMARY_PATTERNS = {
    'start maudio_speaker': 'ALARM_VOICE',      # Voice announcement started
    'Create snap': 'ALARM_SNAPSHOT',             # Alarm snapshot captured
}

# SECONDARY: Strong alarm correlation -- fire near alarm time
SECONDARY_PATTERNS = {
    'goto preset': 'ALARM_PTZ_PRESET',           # PTZ preset triggered by alarm
    '_ivp_delay_timer_process': 'IVP_TIMER',     # IVP delay timer (alarm cooldown)
}

# CONTEXT: Useful for understanding what happened, but not alarm triggers
CONTEXT_PATTERNS = {
    'mivp_set_param': 'IVP_RECONFIG',            # IVP reconfiguration
    'IVP_ABILITY_TYPE_': 'IVP_ABILITY',           # IVP ability flags
    '_speak_process exit': 'VOICE_DONE',          # Voice prompt finished
    'end maudio_speaker': 'VOICE_END',            # Speaker stopped
    'ao start success': 'AUDIO_START',            # Audio output restarted
    'ao stop success': 'AUDIO_STOP',              # Audio output stopped
}


class AlarmDetector:
    """Tracks alarm state using log patterns with debouncing."""

    def __init__(self, cooldown=10):
        self.cooldown = cooldown  # seconds between distinct alarm events
        self.last_alarm_time = 0
        self.alarm_count = 0
        self.events = []
        self.lock = threading.Lock()
        self.callbacks = []

    def on_alarm(self, callback):
        """Register a callback for alarm events: callback(alarm_num, timestamp, trigger)."""
        self.callbacks.append(callback)

    def process_line(self, line, timestamp=None):
        """Process a log line. Returns alarm event dict if this is a new alarm, else None."""
        if timestamp is None:
            timestamp = time.time()

        ts_str = datetime.fromtimestamp(timestamp).strftime('%H:%M:%S.%f')[:-3]

        # Check primary patterns first
        for pattern, event_type in PRIMARY_PATTERNS.items():
            if pattern in line:
                with self.lock:
                    elapsed = timestamp - self.last_alarm_time
                    if elapsed >= self.cooldown:
                        # New alarm event
                        self.alarm_count += 1
                        self.last_alarm_time = timestamp
                        event = {
                            'num': self.alarm_count,
                            'time': ts_str,
                            'timestamp': timestamp,
                            'trigger': event_type,
                            'line': line.strip(),
                        }
                        self.events.append(event)
                        for cb in self.callbacks:
                            try:
                                cb(event)
                            except Exception as e:
                                print(f'  [WARN] Callback error: {e}')
                        return event
                    else:
                        # Same alarm, additional signal
                        return None

        # Check secondary patterns
        for pattern, event_type in SECONDARY_PATTERNS.items():
            if pattern in line:
                with self.lock:
                    elapsed = timestamp - self.last_alarm_time
                    if elapsed < self.cooldown and self.alarm_count > 0:
                        # Part of current alarm -- just note it
                        return None
                    elif elapsed >= self.cooldown:
                        # Secondary pattern as primary trigger (unusual)
                        self.alarm_count += 1
                        self.last_alarm_time = timestamp
                        event = {
                            'num': self.alarm_count,
                            'time': ts_str,
                            'timestamp': timestamp,
                            'trigger': event_type,
                            'line': line.strip(),
                        }
                        self.events.append(event)
                        for cb in self.callbacks:
                            try:
                                cb(event)
                            except Exception as e:
                                print(f'  [WARN] Callback error: {e}')
                        return event

        return None


# ── Webhook support ───────────────────────────────────────────────

def fire_webhook(url, body_template=None, event=None):
    """POST to a webhook URL when an alarm fires."""
    if not HAS_URLLIB:
        print('  [WEBHOOK] urllib not available')
        return

    try:
        if body_template:
            # Substitute placeholders in the template
            body = body_template.replace('{{alarm_num}}', str(event['num']))
            body = body.replace('{{time}}', event['time'])
            body = body.replace('{{trigger}}', event['trigger'])
            body = body.replace('{{line}}', event['line'])
            data = body.encode('utf-8')
            content_type = 'application/json'
        else:
            # Default JSON body
            payload = {
                'alarm': event['num'],
                'time': event['time'],
                'trigger': event['trigger'],
                'camera': CAM_IP,
                'message': f"Camera alarm #{event['num']} - {event['trigger']}",
            }
            data = json.dumps(payload).encode('utf-8')
            content_type = 'application/json'

        req = urllib.request.Request(
            url,
            data=data,
            headers={'Content-Type': content_type},
            method='POST',
        )
        resp = urllib.request.urlopen(req, timeout=10)
        print(f'  [WEBHOOK] POST {url} -> {resp.status}')

    except Exception as e:
        print(f'  [WEBHOOK] Error: {e}')


# ── Log monitor ───────────────────────────────────────────────────

def monitor_superb_log(stop_event, detector, show_all=False):
    """Monitor superb.log via root shell using tail -f."""
    print('  [LOG] Connecting to root shell...')

    while not stop_event.is_set():
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(10)
            s.connect((CAM_IP, CAM_PORT))
            time.sleep(0.3)
            s.recv(4096)  # banner

            # Start tail -f on the log
            s.sendall(b'tail -f /tmp/superb.log\n')
            time.sleep(0.5)

            print('  [LOG] Monitoring superb.log...')
            buffer = b''

            while not stop_event.is_set():
                try:
                    s.settimeout(2)
                    chunk = s.recv(65536)
                    if not chunk:
                        break
                    buffer += chunk

                    while b'\n' in buffer:
                        line_bytes, buffer = buffer.split(b'\n', 1)
                        line = line_bytes.decode('latin-1', errors='replace').strip()
                        if not line:
                            continue

                        # Strip ANSI color codes
                        clean = re.sub(r'\x1b\[[0-9;]*m', '', line)

                        # Show all mode
                        if show_all:
                            ts = datetime.now().strftime('%H:%M:%S')
                            print(f'  [{ts}] [ALL] {clean[:150]}')
                            sys.stdout.flush()

                        # Run through alarm detector
                        event = detector.process_line(clean)
                        if event:
                            print()
                            print(f'  *** ALARM #{event["num"]} at {event["time"]} ***')
                            print(f'  *** Trigger: {event["trigger"]}')
                            print(f'  *** Log: {event["line"][:120]}')
                            print()
                            sys.stdout.flush()

                except socket.timeout:
                    continue

        except Exception as e:
            if not stop_event.is_set():
                print(f'  [LOG] Connection lost: {e}. Reconnecting in 5s...')
                time.sleep(5)

        try:
            s.close()
        except:
            pass


# ── Main ──────────────────────────────────────────────────────────

def main():
    parser = argparse.ArgumentParser(
        description='Real-time alarm monitor for SECUEYE X5 camera',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog='''
Examples:
  # Monitor for 5 minutes, print alarms
  python tools/monitor_alerts.py --duration 300

  # Monitor with all log lines visible (debug mode)
  python tools/monitor_alerts.py --show-all --duration 60

  # Fire a webhook on each alarm (default JSON body)
  python tools/monitor_alerts.py --webhook https://ntfy.sh/my-camera

  # Fire a webhook with custom JSON body
  python tools/monitor_alerts.py --webhook https://discord.com/api/webhooks/XXX \\
    --webhook-json '{"content": "Camera alarm #{{alarm_num}} at {{time}} ({{trigger}})"}'

Webhook body placeholders:
  {{alarm_num}}  -- sequential alarm number
  {{time}}       -- timestamp (HH:MM:SS.mmm)
  {{trigger}}    -- event type (ALARM_VOICE, ALARM_SNAPSHOT, etc.)
  {{line}}       -- raw log line that triggered the alarm
''')
    parser.add_argument('--duration', type=int, default=0,
                        help='Monitor duration in seconds (0=indefinite, default: 0)')
    parser.add_argument('--show-all', action='store_true',
                        help='Show ALL log lines, not just alarms')
    parser.add_argument('--cooldown', type=int, default=10,
                        help='Seconds between distinct alarm events (default: 10)')
    parser.add_argument('--webhook', type=str,
                        help='Webhook URL to POST on alarm')
    parser.add_argument('--webhook-json', type=str,
                        help='Custom JSON body for webhook (supports {{placeholders}})')
    parser.add_argument('--ip', type=str, default='192.168.1.153',
                        help='Camera IP (default: 192.168.1.153)')

    args = parser.parse_args()

    global CAM_IP
    CAM_IP = args.ip

    print('=' * 60)
    print('SECUEYE X5 Alarm Monitor')
    print('=' * 60)
    print(f'  Camera:   {CAM_IP}:{CAM_PORT}')
    print(f'  Duration: {"indefinite" if args.duration == 0 else f"{args.duration}s"}')
    print(f'  Cooldown: {args.cooldown}s between alarms')
    if args.webhook:
        print(f'  Webhook:  {args.webhook}')
    print()

    # Verify camera is reachable
    print('  Checking camera...', end='', flush=True)
    try:
        s = socket.socket()
        s.settimeout(5)
        s.connect((CAM_IP, CAM_PORT))
        s.recv(4096)
        s.close()
        print(' OK')
    except Exception as e:
        print(f' FAILED: {e}')
        sys.exit(1)

    # Set up alarm detector
    detector = AlarmDetector(cooldown=args.cooldown)

    # Register webhook callback if configured
    if args.webhook:
        def webhook_cb(event):
            fire_webhook(args.webhook, args.webhook_json, event)
        detector.on_alarm(webhook_cb)

    # Always print alarm to console
    print()
    print('  Monitoring for alarms. Walk in front of the camera to trigger.')
    print('  Press Ctrl+C to stop.')
    print()

    stop_event = threading.Event()

    log_thread = threading.Thread(
        target=monitor_superb_log,
        args=(stop_event, detector, args.show_all),
        daemon=True,
    )
    log_thread.start()

    try:
        start = time.time()
        while True:
            time.sleep(1)
            if args.duration > 0:
                elapsed = int(time.time() - start)
                if elapsed >= args.duration:
                    break
                remaining = args.duration - elapsed
                if remaining % 30 == 0 and remaining > 0:
                    print(f'  --- {remaining}s remaining, {detector.alarm_count} alarm(s) ---')
    except KeyboardInterrupt:
        print('\n  Stopping...')

    stop_event.set()
    time.sleep(1)

    # Summary
    print()
    print('=' * 60)
    print('  SUMMARY')
    print('=' * 60)
    print(f'  Total alarms detected: {detector.alarm_count}')

    if detector.events:
        print()
        for e in detector.events:
            print(f'  #{e["num"]:3d}  [{e["time"]}]  {e["trigger"]:<20s}  {e["line"][:80]}')

    print()


if __name__ == '__main__':
    main()
