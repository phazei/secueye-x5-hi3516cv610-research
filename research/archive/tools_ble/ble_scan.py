import asyncio
from bleak import BleakScanner

async def scan():
    print("Scanning for BLE devices (10 seconds)...")
    devices_advs = await BleakScanner.discover(timeout=10, return_adv=True)
    print(f"\nFound {len(devices_advs)} total devices.\n")
    print("--- Camera candidates (ipc/xmy/secueye/cam) ---")
    for addr, (d, adv) in devices_advs.items():
        name = d.name or ""
        if any(k in name.lower() for k in ["ipc", "xmy", "cam", "secueye", "seculink"]):
            print(f"  {d.address} | {name} | rssi={adv.rssi}")
            print(f"    service_uuids: {adv.service_uuids}")
            print(f"    manufacturer_data: {adv.manufacturer_data}")
    print("\n--- All named devices ---")
    for addr, (d, adv) in sorted(devices_advs.items(), key=lambda x: x[1][1].rssi or -999, reverse=True):
        if d.name:
            print(f"  {d.address} | {d.name} | rssi={adv.rssi}")

asyncio.run(scan())
