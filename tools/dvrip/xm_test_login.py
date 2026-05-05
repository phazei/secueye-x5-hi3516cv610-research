"""Quick test: DVRIP/Sofia login to Xiongmai camera on port 34567"""
import socket
import json
import struct
import hashlib

CAMERA_IP = "192.168.1.153"
CAMERA_PORT = 34567
USERNAME = "admin"
PASSWORD = ""

def xm_hash(password):
    """Xiongmai password hash (MD5-based, 8-char)"""
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

def send_recv(sock, msgid, payload_dict):
    data_bytes = json.dumps(payload_dict).encode("utf-8")
    # Pad with newline + null as XMeye expects
    data_bytes += b"\x0a\x00"
    header = struct.pack(
        "<BBBBIIBBHI",
        0xFF, 0x01, 0x00, 0x00,
        0,  # session_id (0 for login)
        0,  # sequence
        0,  # total_packets
        0,  # cur_packet
        msgid,
        len(data_bytes),
    )
    sock.send(header + data_bytes)
    
    # Read response header
    resp_hdr = b""
    while len(resp_hdr) < 20:
        chunk = sock.recv(20 - len(resp_hdr))
        if not chunk:
            break
        resp_hdr += chunk
    
    if len(resp_hdr) < 20:
        print(f"Short header: {resp_hdr.hex()}")
        return None, None
    
    hf, ver, r1, r2, sid, seq, tp, cp, mid, dlen = struct.unpack("<BBBBIIBBHI", resp_hdr)
    print(f"  Response header: flag=0x{hf:02x} session=0x{sid:08x} msgid={mid} datalen={dlen}")
    
    # Read response body
    resp_body = b""
    while len(resp_body) < dlen:
        chunk = sock.recv(dlen - len(resp_body))
        if not chunk:
            break
        resp_body += chunk
    
    return mid, resp_body

def main():
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(10)
    print(f"Connecting to {CAMERA_IP}:{CAMERA_PORT}...")
    sock.connect((CAMERA_IP, CAMERA_PORT))
    print("Connected.\n")

    pw_hash = xm_hash(PASSWORD)
    print(f"Login: user={USERNAME} pass_hash={pw_hash}\n")

    # Try login with msgid 1000 (LOGIN_REQ2)
    print("--- LOGIN_REQ2 (msgid=1000) ---")
    mid, body = send_recv(sock, 1000, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": USERNAME,
    })
    if body:
        print(f"  Body hex ({len(body)} bytes): {body.hex()}")
        try:
            text = body.rstrip(b"\x00\x0a").decode("utf-8")
            parsed = json.loads(text)
            print(f"  JSON: {json.dumps(parsed, indent=2)}")
        except:
            # Binary response - parse as struct
            if len(body) >= 24:
                fields = struct.unpack("<IIIIII", body[:24])
                print(f"  Binary fields: {fields}")
                # Field 0 often = ret (0=success), field 4 = session
                ret = fields[0]
                session = fields[4]
                print(f"  ret={ret} session=0x{session:08x}")

    sock.close()
    
    # Try fresh connection with old-style login (msgid 999)
    sock2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock2.settimeout(10)
    sock2.connect((CAMERA_IP, CAMERA_PORT))
    
    print("\n--- LOGIN_REQ (msgid=999, old-style) ---")
    mid2, body2 = send_recv(sock2, 999, {
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": USERNAME,
    })
    if body2:
        print(f"  Body hex ({len(body2)} bytes): {body2.hex()}")
        try:
            text = body2.rstrip(b"\x00\x0a").decode("utf-8")
            parsed = json.loads(text)
            print(f"  JSON: {json.dumps(parsed, indent=2)}")
        except:
            if len(body2) >= 24:
                fields = struct.unpack("<IIIIII", body2[:24])
                print(f"  Binary fields: {fields}")

    sock2.close()

    # Now try with the session we got, query system info
    if body and len(body) >= 24:
        fields = struct.unpack("<IIIIII", body[:24])
        session_id = fields[4]
        
        sock3 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        sock3.settimeout(10)
        sock3.connect((CAMERA_IP, CAMERA_PORT))
        
        # Login again to get a fresh session
        print(f"\n--- Fresh login for SystemInfo query ---")
        login_data = json.dumps({
            "EncryptType": "MD5",
            "LoginType": "DVRIP-Web",
            "PassWord": pw_hash,
            "UserName": USERNAME,
        }).encode("utf-8") + b"\x0a\x00"
        
        hdr = struct.pack("<BBBBIIBBHI", 0xFF, 0x01, 0x00, 0x00, 0, 0, 0, 0, 1000, len(login_data))
        sock3.send(hdr + login_data)
        
        resp_hdr = sock3.recv(20)
        _, _, _, _, sid, _, _, _, rmid, rdlen = struct.unpack("<BBBBIIBBHI", resp_hdr)
        resp_body = sock3.recv(rdlen)
        print(f"  Login response: session=0x{sid:08x}, msgid={rmid}")
        
        if len(resp_body) >= 24:
            ret_fields = struct.unpack("<IIIIII", resp_body[:24])
            session_id = ret_fields[4]
            print(f"  Session from body: 0x{session_id:08x}")
        
        # Query SystemInfo (msgid=1020 = SYSINFO_REQ)
        print(f"\n--- SystemInfo query (msgid=1020) ---")
        sysinfo_data = json.dumps({"Name": "SystemInfo"}).encode("utf-8") + b"\x0a\x00"
        hdr2 = struct.pack("<BBBBIIBBHI", 0xFF, 0x01, 0x00, 0x00, sid, 1, 0, 0, 1020, len(sysinfo_data))
        sock3.send(hdr2 + sysinfo_data)
        
        resp_hdr2 = sock3.recv(20)
        if len(resp_hdr2) >= 20:
            _, _, _, _, sid2, _, _, _, rmid2, rdlen2 = struct.unpack("<BBBBIIBBHI", resp_hdr2)
            print(f"  Response header: session=0x{sid2:08x} msgid={rmid2} datalen={rdlen2}")
            resp_body2 = b""
            while len(resp_body2) < rdlen2:
                chunk = sock3.recv(rdlen2 - len(resp_body2))
                if not chunk:
                    break
                resp_body2 += chunk
            try:
                text2 = resp_body2.rstrip(b"\x00\x0a").decode("utf-8")
                parsed2 = json.loads(text2)
                print(f"  {json.dumps(parsed2, indent=2)}")
            except:
                print(f"  Body hex: {resp_body2.hex()}")
        
        sock3.close()

if __name__ == "__main__":
    main()
