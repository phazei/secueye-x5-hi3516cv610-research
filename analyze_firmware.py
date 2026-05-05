"""
analyze_firmware.py -- Comprehensive analysis of extracted firmware partitions.

Produces firmware/analysis_report.txt with:
  - Complete file tree of all three filesystems
  - File sizes and types
  - Strings analysis of the superb binary (URLs, paths, config keys, credentials)
  - Interesting patterns (hardcoded IPs, keys, certificates)
"""
import os
import re
import struct
from collections import defaultdict
from pathlib import Path

BASE = Path(r"E:\Projects\ipc_XMeye_camera\firmware\extracted")
REPORT = Path(r"E:\Projects\ipc_XMeye_camera\firmware\analysis_report.txt")

# Also analyze the raw superb binary from the appfs dump
SUPERB = BASE / "appfs" / "progs" / "bin" / "superb"
MYSYSTEM = BASE / "appfs" / "progs" / "bin" / "mySystem"
UPGRADE = BASE / "appfs" / "progs" / "bin" / "upgrade"


def file_tree(root, prefix=""):
    """Generate a file tree listing with sizes."""
    lines = []
    root = Path(root)
    if not root.exists():
        return [f"  {root} - NOT FOUND"]

    for item in sorted(root.rglob("*")):
        rel = item.relative_to(root)
        if item.is_file():
            size = item.stat().st_size
            if size > 1048576:
                size_str = f"{size / 1048576:.1f}MB"
            elif size > 1024:
                size_str = f"{size / 1024:.1f}KB"
            else:
                size_str = f"{size}B"
            lines.append(f"  {rel}  ({size_str})")
        elif item.is_dir():
            lines.append(f"  {rel}/")
    return lines


def extract_strings(filepath, min_len=6):
    """Extract ASCII strings from a binary file."""
    if not filepath.exists():
        return []
    data = filepath.read_bytes()
    # Match printable ASCII sequences
    pattern = rb'[\x20-\x7e]{' + str(min_len).encode() + rb',}'
    return [m.decode('ascii', errors='replace') for m in re.findall(pattern, data)]


def categorize_strings(strings):
    """Categorize extracted strings into useful groups."""
    categories = {
        'urls': [],
        'ip_addresses': [],
        'file_paths': [],
        'config_keys': [],
        'dvrip_commands': [],
        'onvif_soap': [],
        'cloud_mqtt': [],
        'crypto': [],
        'error_messages': [],
        'shell_commands': [],
        'interesting': [],
    }

    seen = set()
    for s in strings:
        if s in seen:
            continue
        seen.add(s)

        # URLs
        if re.match(r'https?://', s):
            categories['urls'].append(s)
        elif re.match(r'rtsp://', s, re.I):
            categories['urls'].append(s)
        elif re.match(r'mqtt://', s, re.I):
            categories['urls'].append(s)

        # IP addresses
        elif re.search(r'\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b', s) and not s.startswith('0x'):
            categories['ip_addresses'].append(s)

        # File paths
        elif s.startswith('/') and len(s) > 3 and '/' in s[1:]:
            categories['file_paths'].append(s)

        # DVRIP/Sofia protocol
        elif any(k in s for k in ['OPMachine', 'SystemInfo', 'StorageManager', 'NetWork.',
                                    'Simplify.', 'Camera.', 'General.', 'fVideo.',
                                    'CONFIG_', 'MONITOR_', 'GUARD_', 'SYSMANAGER',
                                    'ChannelTitle', 'Alarm.', 'Record.']):
            categories['dvrip_commands'].append(s)

        # ONVIF/SOAP
        elif any(k in s for k in ['onvif', 'ONVIF', 'soap_', 'tds:', 'tptz:', 'timg:', 'trt:']):
            categories['onvif_soap'].append(s)

        # Cloud/MQTT
        elif any(k in s for k in ['aliyun', 'alibaba', 'mqtt', 'iot-as', 'linkvisual',
                                    'iotx_', 'thing.', 'awss_', 'danale']):
            categories['cloud_mqtt'].append(s)

        # Crypto/security
        elif any(k in s for k in ['mbedtls', 'ssl_', 'certificate', 'private_key',
                                    'encrypt', 'decrypt', 'cipher', 'hmac', 'sha256',
                                    'AES', 'RSA', 'token', 'secret']):
            categories['crypto'].append(s)

        # Shell commands embedded in binary
        elif any(k in s for k in ['rm -rf', 'rm -f', 'mkdir', 'mount', 'insmod',
                                    'ifconfig', 'route', 'echo ', 'cat ', 'cp ']):
            categories['shell_commands'].append(s)

        # Config key patterns (key=value or INI-style)
        elif re.match(r'^[a-zA-Z_][a-zA-Z0-9_]*=', s):
            categories['config_keys'].append(s)

        # Error/debug messages
        elif any(k in s.lower() for k in ['error', 'failed', 'invalid', 'warning']):
            if len(s) < 200:  # skip long template strings
                categories['error_messages'].append(s)

    return categories


def analyze_elf_info(filepath):
    """Basic ELF header analysis."""
    if not filepath.exists():
        return "File not found"
    data = filepath.read_bytes()[:64]
    if data[:4] != b'\x7fELF':
        return "Not an ELF file"

    info = []
    # ELF class
    ei_class = data[4]
    info.append(f"Class: {'32-bit' if ei_class == 1 else '64-bit'}")
    # Endianness
    ei_data = data[5]
    info.append(f"Endian: {'Little' if ei_data == 1 else 'Big'}")
    # Machine type
    if ei_class == 1:  # 32-bit
        e_machine = struct.unpack_from('<H', data, 18)[0]
        e_type = struct.unpack_from('<H', data, 16)[0]
        entry = struct.unpack_from('<I', data, 24)[0]
    else:
        e_machine = struct.unpack_from('<H', data, 18)[0]
        e_type = struct.unpack_from('<H', data, 16)[0]
        entry = struct.unpack_from('<Q', data, 24)[0]

    machine_names = {0x28: 'ARM', 0xB7: 'AArch64', 0x03: 'x86', 0x3E: 'x86-64'}
    type_names = {1: 'Relocatable', 2: 'Executable', 3: 'Shared Object', 4: 'Core'}

    info.append(f"Machine: {machine_names.get(e_machine, f'0x{e_machine:x}')}")
    info.append(f"Type: {type_names.get(e_type, f'0x{e_type:x}')}")
    info.append(f"Entry: 0x{entry:x}")
    info.append(f"Size: {filepath.stat().st_size:,} bytes")

    return ', '.join(info)


def main():
    out = []
    out.append("=" * 75)
    out.append("FIRMWARE ANALYSIS REPORT")
    out.append("SECUEYE X5 / Hi3516CV610 Camera")
    out.append("=" * 75)
    out.append("")

    # ===== FILE TREES =====
    for name, desc in [("rootfs", "Root Filesystem (mtd3, squashfs)"),
                       ("appfs", "Application Filesystem (mtd4, squashfs)"),
                       ("resfs", "Resource Filesystem (mtd6, squashfs)")]:
        out.append(f"{'=' * 75}")
        out.append(f"FILE TREE: {desc}")
        out.append(f"{'=' * 75}")
        tree = file_tree(BASE / name)
        out.extend(tree)
        out.append(f"  --- {len([l for l in tree if not l.endswith('/')])} files ---")
        out.append("")

    # ===== BINARY ANALYSIS =====
    out.append("=" * 75)
    out.append("BINARY ANALYSIS")
    out.append("=" * 75)
    out.append("")

    for name, path in [("superb", SUPERB), ("mySystem", MYSYSTEM), ("upgrade", UPGRADE)]:
        out.append(f"--- {name} ---")
        out.append(f"  ELF: {analyze_elf_info(path)}")
        out.append("")

    # ===== SUPERB STRING ANALYSIS =====
    out.append("=" * 75)
    out.append("SUPERB BINARY - STRING ANALYSIS")
    out.append("=" * 75)
    out.append("")

    if SUPERB.exists():
        print("Extracting strings from superb binary...")
        strings = extract_strings(SUPERB, min_len=8)
        out.append(f"Total strings (8+ chars): {len(strings)}")
        out.append("")

        cats = categorize_strings(strings)

        for cat_name, cat_strings in cats.items():
            if cat_strings:
                # Deduplicate and sort
                unique = sorted(set(cat_strings))
                out.append(f"--- {cat_name.upper()} ({len(unique)} unique) ---")
                for s in unique[:200]:  # cap at 200 per category
                    out.append(f"  {s}")
                if len(unique) > 200:
                    out.append(f"  ... ({len(unique) - 200} more)")
                out.append("")

        # Special: extract DVRIP config path names (things superb can get/set)
        out.append("--- DVRIP CONFIG PATHS (SystemCfg.ini keys) ---")
        cfg_keys = set()
        for s in strings:
            # Pattern: semicolon-delimited key=value pairs from SystemCfg.ini
            for match in re.finditer(r'([a-zA-Z_][a-zA-Z0-9_]*(?:\[[0-9]+\])?)=', s):
                key = match.group(1)
                if len(key) > 3 and key not in ('true', 'false', 'null', 'void', 'this'):
                    cfg_keys.add(key)
        for key in sorted(cfg_keys):
            out.append(f"  {key}")
        out.append(f"  --- {len(cfg_keys)} config keys found ---")
        out.append("")

    # ===== RESFS ANALYSIS =====
    out.append("=" * 75)
    out.append("RESFS - RESOURCE ANALYSIS")
    out.append("=" * 75)
    out.append("")

    # WiFi driver files
    wifi_dir = BASE / "resfs" / "wifi"
    if wifi_dir.exists():
        out.append("--- WiFi Driver Files ---")
        for item in sorted(wifi_dir.rglob("*")):
            if item.is_file():
                rel = item.relative_to(BASE / "resfs")
                size = item.stat().st_size
                out.append(f"  {rel}  ({size:,} bytes)")
        out.append("")

    # Voice files
    voice_dir = BASE / "resfs" / "voice"
    if voice_dir.exists():
        out.append("--- Voice Prompt Files ---")
        for item in sorted(voice_dir.rglob("*")):
            if item.is_file():
                rel = item.relative_to(BASE / "resfs")
                out.append(f"  {rel}")
        out.append("")

    # Sensor files
    sensor_dir = BASE / "resfs" / "sensor"
    if sensor_dir.exists():
        out.append("--- Sensor/ISP Files ---")
        for item in sorted(sensor_dir.rglob("*")):
            if item.is_file():
                rel = item.relative_to(BASE / "resfs")
                size = item.stat().st_size
                out.append(f"  {rel}  ({size:,} bytes)")
        out.append("")

    # IVP (intelligent video processing) files
    ivp_dir = BASE / "resfs" / "ivp"
    if ivp_dir.exists():
        out.append("--- IVP (AI/NPU Model) Files ---")
        for item in sorted(ivp_dir.rglob("*")):
            if item.is_file():
                rel = item.relative_to(BASE / "resfs")
                size = item.stat().st_size
                out.append(f"  {rel}  ({size:,} bytes)")
        out.append("")

    # AIISP files
    aiisp_dir = BASE / "resfs" / "aiisp"
    if aiisp_dir.exists():
        out.append("--- AIISP (AI ISP) Files ---")
        for item in sorted(aiisp_dir.rglob("*")):
            if item.is_file():
                rel = item.relative_to(BASE / "resfs")
                size = item.stat().st_size
                out.append(f"  {rel}  ({size:,} bytes)")
        out.append("")

    # ===== APPFS SCRIPTS =====
    out.append("=" * 75)
    out.append("APPFS - SHELL SCRIPTS")
    out.append("=" * 75)
    out.append("")

    for script in sorted((BASE / "appfs").rglob("*.sh")):
        rel = script.relative_to(BASE / "appfs")
        out.append(f"--- {rel} ---")
        try:
            content = script.read_text(encoding='utf-8', errors='replace')
            for line in content.splitlines():
                out.append(f"  {line}")
        except Exception as e:
            out.append(f"  ERROR reading: {e}")
        out.append("")

    # ===== KERNEL MODULE ANALYSIS =====
    out.append("=" * 75)
    out.append("KERNEL MODULES (.ko files)")
    out.append("=" * 75)
    out.append("")

    ko_dir = BASE / "appfs" / "home" / "ipc_drv"
    if ko_dir.exists():
        total_ko_size = 0
        for item in sorted(ko_dir.rglob("*.ko")):
            rel = item.relative_to(BASE / "appfs")
            size = item.stat().st_size
            total_ko_size += size
            out.append(f"  {rel}  ({size:,} bytes)")
        out.append(f"  --- Total: {total_ko_size:,} bytes ({total_ko_size/1024/1024:.1f}MB) ---")
    out.append("")

    # ===== WRITE REPORT =====
    report_text = "\n".join(out)
    REPORT.write_text(report_text, encoding='utf-8')
    print(f"\nReport written to: {REPORT}")
    print(f"Total lines: {len(out)}")
    print(f"File size: {len(report_text):,} bytes")

    # Print summary stats
    print("\n=== QUICK SUMMARY ===")
    rootfs_files = len(list((BASE / "rootfs").rglob("*"))) if (BASE / "rootfs").exists() else 0
    appfs_files = len(list((BASE / "appfs").rglob("*"))) if (BASE / "appfs").exists() else 0
    resfs_files = len(list((BASE / "resfs").rglob("*"))) if (BASE / "resfs").exists() else 0
    print(f"rootfs: {rootfs_files} items")
    print(f"appfs:  {appfs_files} items")
    print(f"resfs:  {resfs_files} items")
    if SUPERB.exists():
        print(f"superb: {SUPERB.stat().st_size:,} bytes")
        print(f"strings: {len(strings)} (8+ chars)")


if __name__ == '__main__':
    main()
