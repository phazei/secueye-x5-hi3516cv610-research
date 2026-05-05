"""
DVRIP session - parse the binary responses we're getting.
Focus on what works: OPMachine (time), ConfigExport, PTZ response.
Try to set time via OPTimeSetting.
"""
import socket
import json
import struct
import hashlib
import time
from datetime import datetime, timezone, timedelta

CAMERA_IP = "192.168.1.153"
CAMERA_PORT = 34567


def xm_hash(password):
    m = hashlib.md5(password.encode()).digest()
    s = ""
    for i in range(8):
        n = (m[2 * i] + m[2 * i + 1]) % 62
        if n > 35:
            s += chr(n + 61)
        elif n > 9:
            s += chr(n + 55)
        else:
            s += chr(n + 48)
    return s


def make_header(session_id, seq, msgid, data_len):
    return struct.pack(
        "<BBBBIIBBHI",
        0xFF, 0x01, 0x00, 0x00,
        session_id, seq, 0, 0, msgid, data_len,
    )


def recv_full(sock, timeout=5):
    sock.settimeout(timeout)
    hdr = b""
    while len(hdr) < 20:
        try:
            chunk = sock.recv(20 - len(hdr))
            if not chunk:
                break
            hdr += chunk
        except socket.timeout:
            break
    if len(hdr) < 20:
        return None, None
    hf, ver, r1, r2, sid, seq, tp, cp, mid, dlen = struct.unpack("<BBBBIIBBHI", hdr)
    body = b""
    while len(body) < dlen:
        try:
            chunk = sock.recv(min(4096, dlen - len(body)))
            if not chunk:
                break
            body += chunk
        except socket.timeout:
            break
    return (hf, ver, sid, seq, mid, dlen), body


def send_recv(sock, sid, seq, msgid, payload_bytes):
    header = make_header(sid, seq, msgid, len(payload_bytes))
    sock.send(header + payload_bytes)
    return recv_full(sock)


def login(sock):
    pw_hash = xm_hash("")
    data = json.dumps({
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": "admin",
    }).encode("utf-8") + b"\x0a\x00"
    parsed, body = send_recv(sock, 0, 0, 1000, data)
    if parsed and body and len(body) >= 24:
        fields = struct.unpack("<IIIIII", body[:24])
        print(f"Login response fields: ret={fields[0]} alive_interval={fields[1]}"
              f" status={fields[2]} type={fields[3]} session_body=0x{fields[4]:x}")
        return parsed[2]  # session from header
    return None


def main():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(10)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    sid = login(sock)
    if not sid:
        print("Login failed")
        return
    print(f"Session from header: 0x{sid:08x}\n")

    # ================================================================
    # 1. OPMachine (msgid=1452) - returns time
    # ================================================================
    print("=== OPMachine (current time from camera) ===")
    data = json.dumps({"Name": "OPMachine", "OPMachine": {"Action": "QuerySystemTime"}}).encode("utf-8") + b"\x0a\x00"
    parsed, body = send_recv(sock, sid, 1, 1452, data)
    if parsed and body:
        print(f"  Raw hex ({len(body)} bytes): {body.hex()}")
        # First 16 bytes are the standard response header
        # Bytes 16+ contain the time data
        if len(body) >= 48:
            # Parse time from offset 16
            time_data = body[16:]
            fields = struct.unpack("<" + "I" * (len(time_data) // 4), time_data[:len(time_data) // 4 * 4])
            print(f"  Time fields: {fields}")
            if len(fields) >= 7:
                year, month, day, hour, minute, second = fields[0], fields[1], fields[2], fields[3], fields[4], fields[5]
                print(f"  Camera time: {year}-{month:02d}-{day:02d} {hour:02d}:{minute:02d}:{second:02d}")

    # ================================================================
    # 2. Try OPTimeSetting (msgid=1450) - set time
    # ================================================================
    print("\n=== Setting time via OPTimeSetting (msgid=1450) ===")
    # Seattle time = UTC-7 (PDT)
    now_utc = datetime.now(timezone.utc)
    pacific = timezone(timedelta(hours=-7))
    now_local = now_utc.astimezone(pacific)
    print(f"  Setting to: {now_local.strftime('%Y-%m-%d %H:%M:%S')} (PDT)")

    # Standard XMeye time format
    time_str = now_local.strftime("%Y-%m-%d %H:%M:%S")
    time_payload = json.dumps({
        "Name": "OPTimeSetting",
        "OPTimeSetting": time_str,
    }).encode("utf-8") + b"\x0a\x00"

    parsed, body = send_recv(sock, sid, 2, 1450, time_payload)
    if parsed:
        print(f"  Response: msgid={parsed[4]} datalen={parsed[5]}")
        print(f"  Body hex: {body.hex() if body else '(empty)'}")

    # ================================================================
    # 3. Query time again to see if it changed
    # ================================================================
    time.sleep(1)
    print("\n=== Querying time again ===")
    data = json.dumps({"Name": "OPMachine", "OPMachine": {"Action": "QuerySystemTime"}}).encode("utf-8") + b"\x0a\x00"
    parsed, body = send_recv(sock, sid, 3, 1452, data)
    if parsed and body and len(body) >= 48:
        time_data = body[16:]
        fields = struct.unpack("<" + "I" * (min(8, len(time_data) // 4)), time_data[:min(32, len(time_data))])
        print(f"  Time fields: {fields}")
        if len(fields) >= 6:
            print(f"  Camera time: {fields[0]}-{fields[1]:02d}-{fields[2]:02d} {fields[3]:02d}:{fields[4]:02d}:{fields[5]:02d}")

    # ================================================================
    # 4. Parse ConfigExport (msgid=1048) - might have full config dump
    # ================================================================
    print("\n=== ConfigExport (msgid=1048) ===")
    data = json.dumps({"Name": ""}).encode("utf-8") + b"\x0a\x00"
    parsed, body = send_recv(sock, sid, 4, 1048, data)
    if parsed:
        print(f"  Response: msgid={parsed[4]} datalen={parsed[5]}")
        # Check if there's any readable text in the body
        if body:
            # Skip the 16-byte header prefix
            payload = body[16:] if len(body) > 16 else body
            # Look for JSON or readable text
            try:
                # Try to find JSON start
                for i in range(min(100, len(payload))):
                    if payload[i:i+1] == b'{':
                        try:
                            txt = payload[i:].rstrip(b"\x00\x0a").decode("utf-8")
                            j = json.loads(txt)
                            print(f"  Found JSON at offset {i}!")
                            print(f"  {json.dumps(j, indent=2, ensure_ascii=False)[:2000]}")
                            break
                        except:
                            continue
                else:
                    # Show hex dump of first 256 bytes
                    print(f"  First 256 bytes hex: {payload[:256].hex()}")
                    # Try to find any readable strings
                    readable = []
                    current = b""
                    for b in payload:
                        if 32 <= b < 127:
                            current += bytes([b])
                        else:
                            if len(current) >= 4:
                                readable.append(current.decode("ascii"))
                            current = b""
                    if readable:
                        print(f"  Readable strings: {readable[:20]}")
            except Exception as e:
                print(f"  Parse error: {e}")

    # ================================================================
    # 5. PTZ response was large (11796 bytes) - let's look at it
    # ================================================================
    print("\n=== PTZ query (msgid=1440) ===")
    data = json.dumps({"Name": "OPPTZControl", "OPPTZControl": {"Command": "QueryPreset", "Parameter": {}}}).encode("utf-8") + b"\x0a\x00"
    parsed, body = send_recv(sock, sid, 5, 1440, data)
    if parsed:
        print(f"  Response: msgid={parsed[4]} datalen={parsed[5]}")
        if body:
            payload = body[16:] if len(body) > 16 else body
            # Check for readable content
            readable = []
            current = b""
            for b in payload[:1000]:
                if 32 <= b < 127:
                    current += bytes([b])
                else:
                    if len(current) >= 3:
                        readable.append(current.decode("ascii"))
                    current = b""
            if readable:
                print(f"  Readable strings in first 1000 bytes: {readable[:30]}")
            print(f"  First 128 bytes hex: {payload[:128].hex()}")

    sock.close()


if __name__ == "__main__":
    main()
