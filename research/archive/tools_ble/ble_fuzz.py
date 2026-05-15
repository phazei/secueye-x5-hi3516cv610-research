import asyncio
import json
import sys
from bleak import BleakClient

CAMERA_ADDR = "38:77:07:75:97:3A"
WRITE_CHAR = "00002a8a-0000-1000-8000-00805f9b34fb"
NOTIFY_CHAR = "00002a90-0000-1000-8000-00805f9b34fb"

responses = []

def notification_handler(sender, data):
    responses.append(data)
    print(f"[NOTIFY] hex={data.hex()} | len={len(data)}")
    print(f"  ascii: {data.decode('utf-8', errors='replace')}")

async def try_write(client, label, payload_bytes):
    responses.clear()
    print(f"\n{'='*60}")
    print(f"TEST: {label}")
    print(f"  Sending ({len(payload_bytes)} bytes): hex={payload_bytes.hex()}")
    print(f"  ascii: {payload_bytes.decode('utf-8', errors='replace')}")
    try:
        await client.write_gatt_char(WRITE_CHAR, payload_bytes, response=True)
        print("  Write OK")
    except Exception as e:
        print(f"  Write error: {e}")
        return
    await asyncio.sleep(2)
    if not responses:
        print("  No response received")
    print(f"  Got {len(responses)} response(s)")

async def probe():
    print(f"Connecting to {CAMERA_ADDR}...")
    async with BleakClient(CAMERA_ADDR, timeout=15) as client:
        print(f"Connected: {client.is_connected}")
        await client.start_notify(NOTIFY_CHAR, notification_handler)

        # Test 1: Simple ping / empty JSON
        await try_write(client, "empty JSON", b'{}')

        # Test 2: XMeye-style JSON with cmd
        await try_write(client, "XM cmd query", json.dumps({"cmd":"scan"}).encode())

        # Test 3: Simple "get info" type message  
        await try_write(client, "info request", b'\x01')

        # Test 4: Try a binary header that XM protocol often uses
        # XM protocol magic is 0xff, 0x01, 0x00, 0x00
        await try_write(client, "XM magic header", b'\xff\x01\x00\x00')

        # Test 5: Try writing "scan" as plain text
        await try_write(client, "scan plaintext", b'scan')

        # Test 6: Null byte
        await try_write(client, "null byte", b'\x00')

        await client.stop_notify(NOTIFY_CHAR)
        print("\nDone.")

asyncio.run(probe())
