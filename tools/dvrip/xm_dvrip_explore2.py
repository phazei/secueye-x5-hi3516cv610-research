"""
Deeper DVRIP exploration - try all known XMeye message IDs,
different payload formats, and raw binary commands.

The camera returns binary responses to everything. Let's figure out
what the binary format means and whether any command produces JSON.
"""
import socket
import json
import struct
import hashlib
import time

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


def recv_full(sock, timeout=3):
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
    remaining = dlen
    while len(body) < remaining:
        try:
            chunk = sock.recv(min(4096, remaining - len(body)))
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
    if parsed:
        return parsed[2]  # session_id from header
    return None


def main():
    # Connect and login
    sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock.settimeout(5)
    sock.connect((CAMERA_IP, CAMERA_PORT))

    sid = login(sock)
    if not sid:
        print("Login failed")
        return
    print(f"Logged in, session=0x{sid:08x}\n")

    # The login response has 24 bytes with fields (1, 10, 100, 2, session, 0)
    # Field[0]=1 might be Ret (success)
    # Field[1]=10 might be AliveInterval
    # Field[2]=100 (0x64) appears everywhere - might be a status/ret code
    # Field[3]=2 might be channel count or device type
    # Field[4]=session_id
    # Field[5]=0

    # Let's try the full range of known XMeye message IDs
    # and see which ones return something other than the generic 16-byte response

    known_msgids = {
        # Login/session
        1000: "Login_Req",
        1006: "KeepAlive_Req",
        # Config get/set
        1010: "SysManager_Req (upgrade)",
        1012: "LogSearch_Req",
        1014: "StorageManager_Req",
        1020: "SysInfo_Req",
        1022: "SysAbility_Req",
        1040: "ConfigGet_Req",
        1042: "ConfigGet2_Req",
        1044: "ConfigSet_Req",
        1046: "ConfigDefault_Req",
        1048: "ConfigExport_Req",
        1050: "ConfigImport_Req",
        # AV
        1100: "Monitor_Req (start video)",
        1102: "Monitor_Claim",
        1104: "Monitor_Req (start talk)",
        1106: "Monitor_Req (stop talk)",
        # Playback
        1400: "PlayBack_Req",
        1402: "PlayBack_QueryFile",
        1404: "PlayBack_QueryLog",
        1408: "PlayBack_DownloadFile",
        1410: "PlayBack_QueryMonthFile",
        1412: "PlayBack_QueryMonthLog",
        1420: "PlayBack_Control",
        1424: "PlayBack_SnapFile",
        # PTZ
        1440: "PTZ_Req",
        # Guard
        1500: "Guard_Req",
        # Alarm
        1504: "Alarm_Req",
        # Net
        1510: "Net_Req (NetConnect)",
        1512: "Net_Req (NetDisconnect)",
        1514: "Net_Req (NetGetState)",
        1520: "Net_Req (NetKeyboard)",
        1522: "Net_Req (NetSNAP)",
        1530: "Net_Req (NetIPC_Search)",
        # OPMonitor
        1550: "OPMonitor (start)",
        1560: "OPTalk_Req",
        # OPTime
        1450: "OPTimeSetting_Req",
        # Extended
        1452: "OPMachine_Req",
        1454: "OPPTZControl_Req",
        1456: "OPNetAlarm_Req",
        # System operation
        1460: "OPSysOperation_Req (reboot/shutdown)",
        1502: "Guard_Set_Req",
    }

    print("Scanning all known message IDs...\n")
    seq = 1
    interesting = []

    for msgid, name in sorted(known_msgids.items()):
        if msgid == 1000:  # skip login
            continue
        time.sleep(0.05)
        seq += 1

        # Send with minimal JSON payload
        data = b'{"Name":""}\x0a\x00'
        parsed, body = send_recv(sock, sid, seq, msgid, data)

        if not parsed:
            print(f"  {msgid:5d} {name:40s} -> NO RESPONSE")
            continue

        resp_mid = parsed[4]
        resp_len = parsed[5]
        is_generic = (resp_len == 16 and body and body.hex().endswith("0000000000000000"))

        if not is_generic:
            interesting.append((msgid, name, resp_mid, resp_len, body))
            body_preview = body[:64].hex() if body else "(empty)"
            # Try JSON
            try:
                txt = body.rstrip(b"\x00\x0a").decode("utf-8")
                j = json.loads(txt)
                body_preview = f"JSON: {json.dumps(j, ensure_ascii=False)[:200]}"
            except:
                pass
            print(f"  {msgid:5d} {name:40s} -> mid={resp_mid} len={resp_len:5d} ** {body_preview}")
        else:
            print(f"  {msgid:5d} {name:40s} -> mid={resp_mid} len={resp_len:5d} (generic)")

    sock.close()

    # Now try with specific config names for the commands that got generic responses
    print("\n\n" + "=" * 70)
    print("Trying ConfigGet (1042) with specific XMeye config names...")
    print("=" * 70)

    sock2 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock2.settimeout(5)
    sock2.connect((CAMERA_IP, CAMERA_PORT))
    sid2 = login(sock2)
    if not sid2:
        print("Login failed")
        return

    config_names = [
        "SystemInfo", "StorageInfo", "General", "NetWork.NetCommon",
        "Detect.MotionDetect", "Record", "fVideo.OSDInfo",
        "Ability", "ChannelTitle", "OPTimeQuery",
        "AVEnc.VideoColor", "Camera", "AVEnc.SmartH264",
        "NetWork.Wifi", "NetWork.NetDHCP", "NetWork.NetDNS",
        "Simplify.Encode", "Simplify.Camera",
        "AVEnc.Encode", "AVEnc.EncodeStaticParam",
        "fVideo", "fVideo.Tour",
        "NetWork.Upnp", "NetWork.OnvifPwdCheckout",
        "Storage.StoragePosition", "Storage.SDCardInfo",
    ]

    seq2 = 1
    for name in config_names:
        seq2 += 1
        time.sleep(0.05)
        data = json.dumps({"Name": name}).encode("utf-8") + b"\x0a\x00"
        parsed, body = send_recv(sock2, sid2, seq2, 1042, data)

        if not parsed:
            continue

        resp_len = parsed[5]
        is_generic = (resp_len == 16)

        if not is_generic:
            try:
                txt = body.rstrip(b"\x00\x0a").decode("utf-8")
                j = json.loads(txt)
                print(f"\n  {name}: JSON!")
                print(f"    {json.dumps(j, indent=4, ensure_ascii=False)[:500]}")
            except:
                print(f"\n  {name}: len={resp_len} hex={body[:64].hex()}")
        else:
            print(f"  {name}: generic (no data)")

    sock2.close()

    # Try with ConfigGet msgid 1040 instead of 1042
    print("\n\n" + "=" * 70)
    print("Trying ConfigGet (1040) with same names...")
    print("=" * 70)

    sock3 = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    sock3.settimeout(5)
    sock3.connect((CAMERA_IP, CAMERA_PORT))
    sid3 = login(sock3)

    seq3 = 1
    for name in config_names[:10]:
        seq3 += 1
        time.sleep(0.05)
        data = json.dumps({"Name": name}).encode("utf-8") + b"\x0a\x00"
        parsed, body = send_recv(sock3, sid3, seq3, 1040, data)

        if not parsed:
            continue

        resp_len = parsed[5]
        is_generic = (resp_len == 16)

        if not is_generic:
            try:
                txt = body.rstrip(b"\x00\x0a").decode("utf-8")
                j = json.loads(txt)
                print(f"\n  {name}: JSON!")
                print(f"    {json.dumps(j, indent=4, ensure_ascii=False)[:500]}")
            except:
                print(f"\n  {name}: len={resp_len} hex={body[:64].hex()}")
        else:
            print(f"  {name}: generic (no data)")

    sock3.close()


if __name__ == "__main__":
    main()
