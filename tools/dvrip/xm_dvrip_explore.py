"""
Explore the camera's DVRIP binary protocol on port 34567.
Try multiple login formats and message IDs to find what works.
"""
import socket
import json
import struct
import hashlib
import time

CAMERA_IP = "192.168.1.153"
CAMERA_PORT = 34567

def xm_hash(password):
    """Xiongmai password hash (MD5-based, 8-char)."""
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
    """Build a 20-byte DVRIP header."""
    return struct.pack(
        "<BBBBIIBBHI",
        0xFF, 0x01, 0x00, 0x00,  # head_flag, version, reserved
        session_id,
        seq,
        0,  # total_packets
        0,  # cur_packet
        msgid,
        data_len,
    )


def recv_full(sock, timeout=5):
    """Receive a complete DVRIP response (header + body)."""
    sock.settimeout(timeout)
    # Read 20-byte header
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
        return None, None, None

    hf, ver, r1, r2, sid, seq, tp, cp, mid, dlen = struct.unpack("<BBBBIIBBHI", hdr)

    body = b""
    while len(body) < dlen:
        try:
            chunk = sock.recv(dlen - len(body))
            if not chunk:
                break
            body += chunk
        except socket.timeout:
            break

    return (hf, ver, sid, seq, mid, dlen), body, hdr


def send_and_recv(sock, session_id, seq, msgid, payload_dict=None, raw_payload=None):
    """Send a DVRIP message and receive response."""
    if raw_payload is not None:
        data = raw_payload
    elif payload_dict is not None:
        data = json.dumps(payload_dict).encode("utf-8") + b"\x0a\x00"
    else:
        data = b"\x0a\x00"

    header = make_header(session_id, seq, msgid, len(data))
    sock.send(header + data)

    parsed, body, raw_hdr = recv_full(sock)
    return parsed, body


def try_parse_body(body):
    """Try to parse response body as JSON or binary."""
    if not body:
        return "(empty)"

    # Try JSON (strip trailing null/newline)
    stripped = body.rstrip(b"\x00\x0a\x0d ")
    if stripped:
        try:
            return json.loads(stripped.decode("utf-8"))
        except (json.JSONDecodeError, UnicodeDecodeError):
            pass

    # Binary -- show hex and try to parse as fields
    result = f"hex({len(body)}): {body.hex()}"
    if len(body) >= 4:
        # Try interpreting as little-endian uint32 fields
        n_fields = len(body) // 4
        fields = struct.unpack(f"<{n_fields}I", body[: n_fields * 4])
        result += f"\n    uint32 fields: {fields}"
    return result


def main():
    pw_hash = xm_hash("")

    # ================================================================
    # Test 1: Standard JSON login (msgid 1000)
    # ================================================================
    print("=" * 70)
    print("TEST 1: JSON login (msgid=1000, DVRIP-Web)")
    print("=" * 70)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    parsed, body = send_and_recv(sock, 0, 0, 1000, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": "admin",
    })
    if parsed:
        print(f"  Response: msgid={parsed[4]} session=0x{parsed[2]:08x} datalen={parsed[5]}")
        print(f"  Body: {try_parse_body(body)}")
        session_id = parsed[2]
    sock.close()

    # ================================================================
    # Test 2: Login with empty password string (not hashed)
    # ================================================================
    print("\n" + "=" * 70)
    print("TEST 2: JSON login with empty password literal")
    print("=" * 70)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    parsed, body = send_and_recv(sock, 0, 0, 1000, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": "",
        "UserName": "admin",
    })
    if parsed:
        print(f"  Response: msgid={parsed[4]} session=0x{parsed[2]:08x} datalen={parsed[5]}")
        print(f"  Body: {try_parse_body(body)}")
    sock.close()

    # ================================================================
    # Test 3: Try "DVRIP-Net" login type
    # ================================================================
    print("\n" + "=" * 70)
    print("TEST 3: JSON login (LoginType=DVRIP-Net)")
    print("=" * 70)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    parsed, body = send_and_recv(sock, 0, 0, 1000, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Net",
        "PassWord": pw_hash,
        "UserName": "admin",
    })
    if parsed:
        print(f"  Response: msgid={parsed[4]} session=0x{parsed[2]:08x} datalen={parsed[5]}")
        print(f"  Body: {try_parse_body(body)}")
    sock.close()

    # ================================================================
    # Test 4: Use session from test 1, try JSON commands
    # ================================================================
    print("\n" + "=" * 70)
    print("TEST 4: Fresh login + JSON commands with session")
    print("=" * 70)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    # Login
    parsed, body = send_and_recv(sock, 0, 0, 1000, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": "admin",
    })
    if not parsed:
        print("  Login failed")
        sock.close()
        return

    sid = parsed[2]
    print(f"  Logged in, session=0x{sid:08x}")

    # Try various commands using the session
    commands = [
        (1020, {"Name": "SystemInfo"}, "SystemInfo"),
        (1020, {"Name": "StorageInfo"}, "StorageInfo"),
        (1042, {"Name": "OPTimeQuery"}, "OPTimeQuery"),
        (1040, {"Name": "General"}, "General config"),
        (1042, {"Name": "ChannelTitle"}, "ChannelTitle"),
        (1042, {"Name": "NetWork.NetCommon"}, "NetCommon"),
        (1042, {"Name": "Detect.MotionDetect"}, "MotionDetect"),
        (1042, {"Name": "Record"}, "Record config"),
        (1042, {"Name": "fVideo.OSDInfo"}, "OSD Info"),
        (1042, {"Name": "Ability"}, "Ability"),
    ]

    for seq_num, (msgid, payload, label) in enumerate(commands, 1):
        time.sleep(0.1)
        print(f"\n  --- {label} (msgid={msgid}) ---")
        parsed2, body2 = send_and_recv(sock, sid, seq_num, msgid, payload)
        if parsed2:
            print(f"    Response: msgid={parsed2[4]} datalen={parsed2[5]}")
            result = try_parse_body(body2)
            if isinstance(result, dict):
                print(f"    {json.dumps(result, indent=4, ensure_ascii=False)}")
            else:
                print(f"    {result}")
        else:
            print("    No response")

    # ================================================================
    # Test 5: Try KeepAlive then commands
    # ================================================================
    print("\n" + "=" * 70)
    print("TEST 5: KeepAlive (msgid=1006) then SystemInfo")
    print("=" * 70)

    time.sleep(0.1)
    parsed3, body3 = send_and_recv(sock, sid, 100, 1006, {"Name": "KeepAlive"})
    if parsed3:
        print(f"  KeepAlive response: msgid={parsed3[4]} datalen={parsed3[5]}")
        print(f"  Body: {try_parse_body(body3)}")

    time.sleep(0.1)
    parsed4, body4 = send_and_recv(sock, sid, 101, 1020, {"Name": "SystemInfo"})
    if parsed4:
        print(f"  SystemInfo response: msgid={parsed4[4]} datalen={parsed4[5]}")
        result = try_parse_body(body4)
        if isinstance(result, dict):
            print(f"  {json.dumps(result, indent=4, ensure_ascii=False)}")
        else:
            print(f"  {result}")

    sock.close()

    # ================================================================
    # Test 6: Try completely different approach - raw binary login
    # Some newer XM cameras use a binary login packet
    # ================================================================
    print("\n" + "=" * 70)
    print("TEST 6: Probe - send minimal payloads")
    print("=" * 70)
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    # Try with no JSON terminator
    login_json = json.dumps({
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": "admin",
    }).encode("utf-8")

    # Without trailing \x0a\x00
    header = make_header(0, 0, 1000, len(login_json))
    sock.send(header + login_json)
    parsed5, body5, _ = recv_full(sock)
    if parsed5:
        print(f"  No-terminator login: msgid={parsed5[4]} session=0x{parsed5[2]:08x}")
        print(f"  Body: {try_parse_body(body5)}")
    else:
        print("  No response without terminator")

    sock.close()


if __name__ == "__main__":
    main()
