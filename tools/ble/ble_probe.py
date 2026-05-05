import asyncio
import json
from bleak import BleakClient

CAMERA_ADDR = "38:77:07:75:97:3A"
WRITE_CHAR = "00002a8a-0000-1000-8000-00805f9b34fb"  # User Data - read/write
NOTIFY_CHAR = "00002a90-0000-1000-8000-00805f9b34fb"  # User Data - notify

def notification_handler(sender, data):
    print(f"[NOTIFY] handle={sender} | hex={data.hex()} | len={len(data)}")
    try:
        print(f"  ascii: {data.decode('utf-8', errors='replace')}")
    except:
        pass
    try:
        decoded = json.loads(data.decode('utf-8', errors='replace'))
        print(f"  json: {json.dumps(decoded, indent=2)}")
    except:
        pass

async def probe():
    print(f"Connecting to {CAMERA_ADDR}...")
    async with BleakClient(CAMERA_ADDR, timeout=15) as client:
        print(f"Connected: {client.is_connected}")
        
        # Subscribe to notifications first
        await client.start_notify(NOTIFY_CHAR, notification_handler)
        print("Subscribed to notifications on 0x2A90")
        
        # Read current value of writable char
        val = await client.read_gatt_char(WRITE_CHAR)
        print(f"\nCurrent value of 0x2A8A: hex={val.hex()} ascii={val.decode('utf-8', errors='replace')}")
        
        # Also check the other custom services we saw advertised
        # 0000bbb0, 0000bbb1, 0000ccc0, 0000ddd0
        for svc_uuid in ["0000bbb0-0000-1000-8000-00805f9b34fb",
                          "0000bbb1-0000-1000-8000-00805f9b34fb",
                          "0000ccc0-0000-1000-8000-00805f9b34fb",
                          "0000ddd0-0000-1000-8000-00805f9b34fb"]:
            print(f"\n--- Checking advertised service {svc_uuid[:8]} ---")
            for service in client.services:
                if service.uuid == svc_uuid:
                    for char in service.characteristics:
                        props = ", ".join(char.properties)
                        print(f"  Char: {char.uuid} | props: [{props}]")
                        if "read" in char.properties:
                            try:
                                v = await client.read_gatt_char(char)
                                print(f"    Value: hex={v.hex()} | ascii={v.decode('utf-8', errors='replace')}")
                            except Exception as e:
                                print(f"    Read error: {e}")
        
        # Wait a moment for any notifications
        print("\nWaiting 5s for any unsolicited notifications...")
        await asyncio.sleep(5)
        
        await client.stop_notify(NOTIFY_CHAR)
        print("\nDone.")

asyncio.run(probe())
