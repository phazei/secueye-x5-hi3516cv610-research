"""Monitor camera boot via UART and capture everything.
Run this, then power-cycle the camera. Do NOT press keys during boot.
Captures the full boot log to uart_normal_boot.txt.
"""
import serial
import time
import sys

sys.stdout.reconfigure(encoding='utf-8', errors='replace')

logfile = open('uart_normal_boot.txt', 'w', encoding='latin-1', errors='replace')

s = serial.Serial('COM3', 115200, timeout=1)
s.read(16384)  # flush

print('=== Monitoring UART for normal boot ===')
print('>>> Power-cycle the camera NOW (unplug/replug USB-C) <<<')
print('>>> Do NOT press any keys - let it boot normally <<<')
print('Logging to uart_normal_boot.txt')
print()

start = time.time()
total = b''
saw_tcpsvd = False
saw_superb = False
saw_login = False

while time.time() - start < 120:
    data = s.read(4096)
    if data:
        text = data.decode('latin-1', errors='replace')
        sys.stdout.write(text)
        sys.stdout.flush()
        logfile.write(text)
        logfile.flush()
        total += data

        if b'tcpsvd' in data or b'9999' in data:
            saw_tcpsvd = True
            print('\n*** tcpsvd/port 9999 detected! ***', flush=True)
        if b'superb' in data:
            saw_superb = True
        if b'login:' in data:
            saw_login = True

    # Check if boot seems complete (login prompt appeared)
    if saw_login and (time.time() - start > 30):
        # Wait a bit more for WiFi to connect
        print('\n\n[..] Login prompt seen, waiting 30s for WiFi...', flush=True)
        time.sleep(30)
        # Read any remaining
        data = s.read(16384)
        if data:
            text = data.decode('latin-1', errors='replace')
            sys.stdout.write(text)
            logfile.write(text)
        break

logfile.close()
s.close()

print('\n\n=== Boot monitoring complete ===')
print(f'Total bytes captured: {len(total)}')
print(f'Saw tcpsvd/9999: {saw_tcpsvd}')
print(f'Saw superb: {saw_superb}')
print(f'Saw login prompt: {saw_login}')
print(f'Duration: {time.time() - start:.0f}s')
