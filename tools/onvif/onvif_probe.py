"""Probe ONVIF services on the camera."""
import socket
import re

CAMERA_IP = "192.168.1.153"

def onvif_request(path, body_xml):
    soap = (
        '<?xml version="1.0" encoding="utf-8"?>'
        '<s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope"'
        ' xmlns:tds="http://www.onvif.org/ver10/device/wsdl"'
        ' xmlns:trt="http://www.onvif.org/ver10/media/wsdl"'
        ' xmlns:tt="http://www.onvif.org/ver10/schema">'
        f"<s:Body>{body_xml}</s:Body>"
        "</s:Envelope>"
    )
    http = (
        f"POST {path} HTTP/1.1\r\n"
        f"Host: {CAMERA_IP}\r\n"
        f"Content-Type: application/soap+xml\r\n"
        f"Content-Length: {len(soap)}\r\n"
        f"\r\n{soap}"
    )
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.settimeout(5)
    s.connect((CAMERA_IP, 80))
    s.send(http.encode())
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
    return resp.decode("utf-8", errors="replace")


def main():
    # Device info
    print("=== DEVICE INFO ===")
    r = onvif_request(
        "/onvif/device_service",
        "<tds:GetDeviceInformation/>",
    )
    for tag in ["Manufacturer", "Model", "FirmwareVersion", "SerialNumber", "HardwareId"]:
        m = re.search(rf"<tds:{tag}>(.*?)</tds:{tag}>", r)
        if m:
            print(f"  {tag}: {m.group(1)}")

    # Capabilities
    print("\n=== SERVICE URLS ===")
    r = onvif_request(
        "/onvif/device_service",
        "<tds:GetCapabilities><tds:Category>All</tds:Category></tds:GetCapabilities>",
    )
    for m in re.finditer(r"<tt:XAddr>(.*?)</tt:XAddr>", r):
        print(f"  {m.group(1)}")

    # Scopes (often has device name, location, hardware info)
    print("\n=== SCOPES ===")
    r = onvif_request("/onvif/device_service", "<tds:GetScopes/>")
    for m in re.finditer(r"<tt:ScopeItem>(.*?)</tt:ScopeItem>", r):
        print(f"  {m.group(1)}")

    # Media profiles
    print("\n=== MEDIA PROFILES ===")
    r2 = onvif_request("/onvif/media_service", "<trt:GetProfiles/>")
    tokens = re.findall(r'token="([^"]+)"', r2)
    # Deduplicate while preserving order
    seen = set()
    profile_tokens = []
    for t in tokens:
        if t not in seen:
            seen.add(t)
            profile_tokens.append(t)

    for token in profile_tokens:
        print(f"\n  Profile: {token}")
        # Find resolution for this profile
        # Look for the section containing this token
        pattern = rf'token="{re.escape(token)}".*?(?=token="|$)'
        section = re.search(pattern, r2, re.DOTALL)
        if section:
            text = section.group(0)
            res = re.findall(r"<tt:Width>(\d+)</tt:Width>\s*<tt:Height>(\d+)</tt:Height>", text)
            if res:
                print(f"    Resolution: {res[0][0]}x{res[0][1]}")
            enc = re.search(r"<tt:Encoding>(.*?)</tt:Encoding>", text)
            if enc:
                print(f"    Encoding: {enc.group(1)}")

    # Stream URIs
    print("\n=== STREAM URIs ===")
    for token in profile_tokens:
        r3 = onvif_request(
            "/onvif/media_service",
            f'<trt:GetStreamUri>'
            f'<trt:StreamSetup>'
            f'<tt:Stream>RTP-Unicast</tt:Stream>'
            f'<tt:Transport><tt:Protocol>RTSP</tt:Protocol></tt:Transport>'
            f'</trt:StreamSetup>'
            f'<trt:ProfileToken>{token}</trt:ProfileToken>'
            f'</trt:GetStreamUri>',
        )
        uri = re.search(r"<tt:Uri>(.*?)</tt:Uri>", r3)
        if uri:
            print(f"  {token}: {uri.group(1)}")

    # Snapshot URI
    print("\n=== SNAPSHOT URIs ===")
    for token in profile_tokens:
        r4 = onvif_request(
            "/onvif/media_service",
            f'<trt:GetSnapshotUri>'
            f'<trt:ProfileToken>{token}</trt:ProfileToken>'
            f'</trt:GetSnapshotUri>',
        )
        uri = re.search(r"<tt:Uri>(.*?)</tt:Uri>", r4)
        if uri:
            print(f"  {token}: {uri.group(1)}")

    # Network info
    print("\n=== NETWORK INTERFACES ===")
    r5 = onvif_request("/onvif/device_service", "<tds:GetNetworkInterfaces/>")
    for m in re.finditer(r"<tt:HwAddress>(.*?)</tt:HwAddress>", r5):
        print(f"  MAC: {m.group(1)}")
    for m in re.finditer(r"<tt:Address>(.*?)</tt:Address>", r5):
        print(f"  IP: {m.group(1)}")


if __name__ == "__main__":
    main()
