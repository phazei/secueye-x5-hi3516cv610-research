#!/usr/bin/env python3
"""
ble_provision.py — BLE WiFi provisioning for Xiongmai/XMeye-based cameras

Works with cameras that advertise as "ipc_xmy-*" over BLE, including:
  - SECUEYE X5 Smart Window Camera
  - Other Seculink/Xiongmai rebrands using the same BLE provisioning stack

Protocol (reverse-engineered from Secueye APK v2.3.7):
  Service:  0x181C (User Data)
  Write:    0x2A8A — accepts UTF-8 text commands
  Notify:   0x2A90 — returns UTF-8 status strings

  Provisioning sequence:
    1. Connect to camera BLE
    2. Subscribe to notifications on 0x2A90
    3. Send "STATUS?" — camera replies "STATUS=wifi_wait"
    4. Send "SSID:<wifi_name>"
    5. Wait ~200ms
    6. Send "PWD:<wifi_password>"
    7. Poll "STATUS?" — camera replies with connection progress:
         STATUS=wifi_find        — found SSID
         STATUS=wifi_connecting  — connecting
         STATUS=wifi_success     — connected
         STATUS=wifi_failed      — failed

  Other commands:
    "PK&DN?"   — returns "pk=<key>&dn=<name>" (Alibaba IoT product key/device name)
    "UNBIND?"  — unbind device from cloud account

Requirements:
  pip install bleak

Usage:
  python ble_provision.py --ssid "MyWiFi" --password "MyPassword"
  python ble_provision.py --ssid "MyWiFi" --password "MyPassword" --mac 38:77:07:75:97:3A
  python ble_provision.py --scan
  python ble_provision.py --status --mac 38:77:07:75:97:3A
"""

import argparse
import asyncio
import sys
import os

os.environ["PYTHONIOENCODING"] = "utf-8"

from bleak import BleakClient, BleakScanner

# BLE UUIDs (Xiongmai IPC standard)
SERVICE_UUID = "0000181c-0000-1000-8000-00805f9b34fb"
WRITE_UUID = "00002a8a-0000-1000-8000-00805f9b34fb"
NOTIFY_UUID = "00002a90-0000-1000-8000-00805f9b34fb"

# Camera BLE advertisement name prefix
CAMERA_NAME_PREFIX = "ipc_xmy"

# Timing (matches the Secueye app behavior)
DELAY_AFTER_CONNECT_S = 1.5
DELAY_BETWEEN_WRITES_S = 0.5
STATUS_POLL_INTERVAL_S = 2.0
PROVISION_TIMEOUT_S = 120


class CameraProvisioner:
    def __init__(self, mac=None, timeout=15):
        self.mac = mac
        self.timeout = timeout
        self.responses = []
        self._response_event = asyncio.Event()

    def _notification_handler(self, sender, data):
        try:
            text = data.decode("utf-8", errors="replace")
        except Exception:
            text = data.hex()
        print(f"  <- {text}")
        self.responses.append(text)
        self._response_event.set()

    async def _send_command(self, client, command, label=None):
        """Send a UTF-8 text command to the camera."""
        label = label or command
        payload = command.encode("utf-8")
        print(f"  -> {label}")
        await client.write_gatt_char(WRITE_UUID, payload, response=True)

    async def _wait_for_response(self, timeout=5.0):
        """Wait for a notification response, return it or None on timeout."""
        self._response_event.clear()
        self.responses.clear()
        try:
            await asyncio.wait_for(self._response_event.wait(), timeout=timeout)
            return self.responses[-1] if self.responses else None
        except asyncio.TimeoutError:
            return None

    async def _poll_status(self, client, timeout_s=PROVISION_TIMEOUT_S):
        """Poll STATUS? until we get a terminal state or timeout."""
        terminal = {"STATUS=wifi_success", "STATUS=wifi_failed"}
        elapsed = 0.0
        last_status = None

        while elapsed < timeout_s:
            self.responses.clear()
            self._response_event.clear()
            await self._send_command(client, "STATUS?")

            try:
                await asyncio.wait_for(
                    self._response_event.wait(), timeout=STATUS_POLL_INTERVAL_S + 1
                )
            except asyncio.TimeoutError:
                pass

            for resp in self.responses:
                if resp.startswith("STATUS=") and resp != last_status:
                    last_status = resp
                if resp in terminal:
                    return resp

            elapsed += STATUS_POLL_INTERVAL_S
            await asyncio.sleep(STATUS_POLL_INTERVAL_S)

        return last_status

    # ── Public commands ──────────────────────────────────────────────

    async def scan(self, scan_time=10):
        """Scan for XMeye cameras and return a list of (address, name, rssi)."""
        print(f"Scanning for BLE devices ({scan_time}s)...")
        results = await BleakScanner.discover(timeout=scan_time, return_adv=True)

        cameras = []
        for addr, (device, adv) in results.items():
            name = device.name or ""
            if CAMERA_NAME_PREFIX in name.lower():
                cameras.append((device.address, name, adv.rssi))

        if not cameras:
            print("No XMeye cameras found.")
            print("Make sure the camera is powered on and not already connected to WiFi.")
        else:
            print(f"\nFound {len(cameras)} camera(s):\n")
            for addr, name, rssi in sorted(cameras, key=lambda x: x[2], reverse=True):
                print(f"  {addr}  {name}  (RSSI: {rssi})")

        return cameras

    async def get_status(self):
        """Connect and query the current WiFi status."""
        print(f"Connecting to {self.mac}...")
        async with BleakClient(self.mac, timeout=self.timeout) as client:
            print(f"Connected: {client.is_connected}")
            await client.start_notify(NOTIFY_UUID, self._notification_handler)
            await asyncio.sleep(DELAY_AFTER_CONNECT_S)

            await self._send_command(client, "STATUS?")
            resp = await self._wait_for_response(timeout=5)
            if resp:
                print(f"\nCamera status: {resp}")
            else:
                print("\nNo status response (camera may not support this query yet).")

            # Also try PK&DN
            await self._send_command(client, "PK&DN?")
            resp = await self._wait_for_response(timeout=5)
            if resp and ("pk=" in resp or "dn=" in resp):
                print(f"Device identity: {resp}")

            await client.stop_notify(NOTIFY_UUID)

    async def provision(self, ssid, password):
        """Send WiFi credentials and monitor until connected or failed."""
        if not self.mac:
            print("No camera MAC specified. Scanning...")
            cameras = await self.scan()
            if not cameras:
                return False
            if len(cameras) == 1:
                self.mac = cameras[0][0]
                print(f"\nUsing camera: {cameras[0][1]} ({self.mac})")
            else:
                print("\nMultiple cameras found. Please specify --mac.")
                return False

        print(f"\nConnecting to {self.mac}...")
        async with BleakClient(self.mac, timeout=self.timeout) as client:
            print(f"Connected: {client.is_connected}")

            # Subscribe to notifications
            await client.start_notify(NOTIFY_UUID, self._notification_handler)
            await asyncio.sleep(DELAY_AFTER_CONNECT_S)

            # Check current status
            print("\nQuerying camera status...")
            await self._send_command(client, "STATUS?")
            await self._wait_for_response(timeout=3)

            # Send WiFi credentials
            print(f"\nSending WiFi credentials...")
            await self._send_command(client, f"SSID:{ssid}")
            await asyncio.sleep(DELAY_BETWEEN_WRITES_S)
            await self._send_command(client, f"PWD:{password}", label="PWD:********")

            # Poll for result
            print(f"\nWaiting for camera to connect to WiFi (up to {PROVISION_TIMEOUT_S}s)...")
            result = await self._poll_status(client, timeout_s=PROVISION_TIMEOUT_S)

            await client.stop_notify(NOTIFY_UUID)

        # Report result
        print()
        if result == "STATUS=wifi_success":
            print("SUCCESS — Camera connected to WiFi.")
            print()
            print("Next steps:")
            print("  1. Find the camera's IP in your router's DHCP table")
            print("  2. Try RTSP stream:  vlc rtsp://<camera-ip>/live1")
            print("  3. Default login: admin (no password)")
            print("  4. Block the camera from internet at your router/firewall")
            return True
        elif result == "STATUS=wifi_failed":
            print("FAILED — Camera could not connect to WiFi.")
            print("Check SSID and password, and ensure the camera is in range.")
            return False
        else:
            print(f"TIMEOUT — Last status: {result or 'no response'}")
            print("The camera may still be trying. Run with --status to check.")
            return False


def main():
    parser = argparse.ArgumentParser(
        description="BLE WiFi provisioning for Xiongmai/XMeye cameras (ipc_xmy-*)",
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
examples:
  %(prog)s --scan                                      Find cameras
  %(prog)s --ssid "MyWiFi" --password "secret123"      Auto-find & provision
  %(prog)s --ssid "MyWiFi" --password "s" --mac AA:BB:CC:DD:EE:FF
  %(prog)s --status --mac AA:BB:CC:DD:EE:FF            Query camera state
        """,
    )

    parser.add_argument("--scan", action="store_true", help="Scan for XMeye cameras and exit")
    parser.add_argument("--status", action="store_true", help="Query camera WiFi status")
    parser.add_argument("--ssid", type=str, help="WiFi network name")
    parser.add_argument("--password", type=str, help="WiFi password")
    parser.add_argument("--mac", type=str, default=None, help="Camera BLE MAC address (e.g. AA:BB:CC:DD:EE:FF)")
    parser.add_argument("--timeout", type=int, default=15, help="BLE connection timeout in seconds (default: 15)")
    parser.add_argument("--scan-time", type=int, default=10, help="BLE scan duration in seconds (default: 10)")

    args = parser.parse_args()

    provisioner = CameraProvisioner(mac=args.mac, timeout=args.timeout)

    if args.scan:
        asyncio.run(provisioner.scan(scan_time=args.scan_time))
    elif args.status:
        if not args.mac:
            parser.error("--status requires --mac")
        asyncio.run(provisioner.get_status())
    elif args.ssid and args.password:
        success = asyncio.run(provisioner.provision(args.ssid, args.password))
        sys.exit(0 if success else 1)
    else:
        parser.print_help()
        sys.exit(1)


if __name__ == "__main__":
    main()
