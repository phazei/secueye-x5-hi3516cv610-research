"""Run a single command on the camera via UART and print the result."""
import serial
import time
import sys

if len(sys.argv) < 2:
    print("Usage: python uart_cmd.py <command>")
    sys.exit(1)

command = ' '.join(sys.argv[1:])

s = serial.Serial('COM3', 115200, timeout=3)
# Flush any pending data
time.sleep(0.3)
s.read(16384)

# Send command
s.write((command + '\n').encode())
time.sleep(2)
data = s.read(16384)
text = data.decode('latin-1', errors='replace')
print(text)

s.close()
