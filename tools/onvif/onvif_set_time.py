"""Set camera date/time and timezone via ONVIF."""
import socket
import re
from datetime import datetime, timezone, timedelta

CAMERA_IP = "192.168.1.153"

# Seattle = America/Los_Angeles = PST/PDT
# POSIX TZ string: PST8PDT,M3.2.0,M11.1.0
# UTC offset: -8 standard, -7 daylight
TZ_POSIX = "PST8PDT,M3.2.0/2:00:00,M11.1.0/2:00:00"

def onvif_request(path, body_xml):
    soap = (
        '<?xml version="1.0" encoding="utf-8"?>'
        '<s:Envelope xmlns:s="http://www.w3.org/2003/05/soap-envelope"'
        ' xmlns:tds="http://www.onvif.org/ver10/device/wsdl"'
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
    # First, read current time settings
    print("=== CURRENT TIME SETTINGS ===")
    r = onvif_request("/onvif/device_service", "<tds:GetSystemDateAndTime/>")
    print(re.sub(r"<[^>]+>", lambda m: m.group(0) + "\n" if m.group(0).startswith("</") else m.group(0), r).split("\r\n\r\n", 1)[-1][:2000])

    # Get current time in Seattle
    pacific = timezone(timedelta(hours=-7))  # PDT currently in effect (May 2026)
    now_utc = datetime.now(timezone.utc)
    now_local = now_utc.astimezone(pacific)

    print(f"\n\nCurrent UTC:   {now_utc.strftime('%Y-%m-%d %H:%M:%S')}")
    print(f"Current Local: {now_local.strftime('%Y-%m-%d %H:%M:%S')} (PDT, UTC-7)")

    # Set time via ONVIF
    # SetSystemDateAndTime with NTP or Manual
    # Using Manual with UTC time + timezone info
    print("\n=== SETTING TIME ===")
    set_xml = f"""<tds:SetSystemDateAndTime>
      <tds:DateTimeType>Manual</tds:DateTimeType>
      <tds:DaylightSavings>true</tds:DaylightSavings>
      <tds:TimeZone>
        <tt:TZ>{TZ_POSIX}</tt:TZ>
      </tds:TimeZone>
      <tds:UTCDateTime>
        <tt:Time>
          <tt:Hour>{now_utc.hour}</tt:Hour>
          <tt:Minute>{now_utc.minute}</tt:Minute>
          <tt:Second>{now_utc.second}</tt:Second>
        </tt:Time>
        <tt:Date>
          <tt:Year>{now_utc.year}</tt:Year>
          <tt:Month>{now_utc.month}</tt:Month>
          <tt:Day>{now_utc.day}</tt:Day>
        </tt:Date>
      </tds:UTCDateTime>
    </tds:SetSystemDateAndTime>"""

    r2 = onvif_request("/onvif/device_service", set_xml)

    if "Fault" in r2 or "fault" in r2:
        print("ERROR - ONVIF returned a fault:")
        # Extract fault reason
        reason = re.search(r"<[^>]*Reason[^>]*>.*?<[^>]*Text[^>]*>(.*?)</", r2, re.DOTALL)
        if reason:
            print(f"  {reason.group(1)}")
        else:
            print(r2.split("\r\n\r\n", 1)[-1][:1000])
    else:
        print("Time set successfully.")

    # Verify
    print("\n=== VERIFY ===")
    r3 = onvif_request("/onvif/device_service", "<tds:GetSystemDateAndTime/>")
    # Extract key fields
    for tag in ["Hour", "Minute", "Second", "Year", "Month", "Day", "TZ", "DaylightSavings", "DateTimeType"]:
        m = re.search(rf"<tt:{tag}>(.*?)</tt:{tag}>", r3)
        if not m:
            m = re.search(rf"<tds:{tag}>(.*?)</tds:{tag}>", r3)
        if m:
            print(f"  {tag}: {m.group(1)}")


if __name__ == "__main__":
    main()
