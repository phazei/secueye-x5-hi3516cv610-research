import asyncio
from bleak import BleakClient, BleakScanner

CAMERA_ADDR = "38:77:07:75:97:3A"

async def enumerate():
    print(f"Connecting to {CAMERA_ADDR}...")
    async with BleakClient(CAMERA_ADDR, timeout=15) as client:
        print(f"Connected: {client.is_connected}")
        print(f"MTU: {client.mtu_size}\n")
        
        for service in client.services:
            print(f"Service: {service.uuid} ({service.description})")
            for char in service.characteristics:
                props = ", ".join(char.properties)
                print(f"  Char: {char.uuid} | props: [{props}]")
                # Try to read if readable
                if "read" in char.properties:
                    try:
                        val = await client.read_gatt_char(char)
                        print(f"    Value: {val} | hex: {val.hex()} | ascii: {val.decode('utf-8', errors='replace')}")
                    except Exception as e:
                        print(f"    Read error: {e}")
                for desc in char.descriptors:
                    print(f"    Desc: {desc.uuid} = {desc.handle}")

asyncio.run(enumerate())
