import asyncio
import json
import sys
import os
os.environ['PYTHONIOENCODING'] = 'utf-8'
from bleak import BleakClient

CAMERA_ADDR = "38:77:07:75:97:3A"
WRITE_CHAR = "00002a8a-0000-1000-8000-00805f9b34fb"
NOTIFY_CHAR = "00002a90-0000-1000-8000-00805f9b34fb"

responses = []

def notification_handler(sender, data):
    responses.append(data)
    print(f"[NOTIFY] hex={data.hex()} | len={len(data)}")
    try:
        text = data.decode('utf-8', errors='backslashreplace')
        print(f"  text: {text}")
    except:
        pass

async def try_write(client, label, payload_bytes):
    responses.clear()
    print(f"\n{'='*60}")
    print(f"TEST: {label}")
    print(f"  hex={payload_bytes.hex()} ({len(payload_bytes)} bytes)")
    try:
        await client.write_gatt_char(WRITE_CHAR, payload_bytes, response=True)
        print("  Write: OK")
    except Exception as e:
        print(f"  Write error: {e}")
        return
    await asyncio.sleep(3)
    if not responses:
        print("  Response: none")
    else:
        print(f"  Responses: {len(responses)}")

async def probe():
    print(f"Connecting to {CAMERA_ADDR}...")
    async with BleakClient(CAMERA_ADDR, timeout=15) as client:
        print(f"Connected: {client.is_connected}")
        await client.start_notify(NOTIFY_CHAR, notification_handler)

        # Binary probes
        await try_write(client, "XM magic 0xff010000", bytes([0xff, 0x01, 0x00, 0x00]))
        await try_write(client, "single null", bytes([0x00]))
        await try_write(client, "0x01 0x00", bytes([0x01, 0x00]))
        
        # Common XMeye BLE JSON formats
        await try_write(client, "JSON wifi_config", 
            json.dumps({"ssid":"TEST_PROBE","password":"fake12345"}).encode('utf-8'))
        
        await try_write(client, "JSON with cmd=wifi_config",
            json.dumps({"cmd":"wifi_config","ssid":"TEST_PROBE","password":"fake12345"}).encode('utf-8'))
        
        await try_write(client, "JSON with cmd=get_wifi",
            json.dumps({"cmd":"get_wifi"}).encode('utf-8'))
        
        await try_write(client, "JSON with cmd=get_info",
            json.dumps({"cmd":"get_info"}).encode('utf-8'))

        # XMeye Sofia protocol style - semicolon delimited
        await try_write(client, "ssid;password delimited",
            b'TEST_PROBE;fake12345')

        await client.stop_notify(NOTIFY_CHAR)
        print("\nDone.")

asyncio.run(probe())
