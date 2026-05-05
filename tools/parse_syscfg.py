"""Parse SystemCfg.ini from camera and display in readable format."""
import socket
import time
import sys
import re

def get_camera_file(path, ip='192.168.1.153', port=9999):
    s = socket.socket()
    s.settimeout(10)
    s.connect((ip, port))
    time.sleep(0.5)
    s.recv(4096)  # banner
    marker = '__ENDCMD__'
    s.sendall(f'cat {path}; echo {marker}\n'.encode())
    time.sleep(1)
    data = b''
    while True:
        try:
            s.settimeout(2)
            chunk = s.recv(65536)
            if not chunk:
                break
            data += chunk
            if marker.encode() in data:
                break
        except socket.timeout:
            break
    s.close()
    text = data.decode('latin-1', errors='replace')
    # Remove marker and prompt
    idx = text.find(marker)
    if idx >= 0:
        text = text[:idx]
    # Remove command echo
    lines = text.strip().split('\n')
    if lines and 'cat ' in lines[0]:
        lines = lines[1:]
    return '\n'.join(lines).strip()

def parse_syscfg(raw):
    """Parse the semicolon-separated SystemCfg.ini into sections."""
    sections = {}
    current_section = 'GLOBAL'
    
    # Split by semicolons
    parts = raw.split(';')
    
    for part in parts:
        part = part.strip()
        if not part:
            continue
        
        # Check for section header
        section_match = re.match(r'^\[(.+?)\]$', part)
        if section_match:
            current_section = section_match.group(1)
            if current_section not in sections:
                sections[current_section] = {}
            continue
        
        # Check for key=value (with possible section prefix)
        if '=' in part:
            # Handle section prefix in same token: [CH1]key=value
            prefix_match = re.match(r'^\[(.+?)\](.+)$', part)
            if prefix_match:
                current_section = prefix_match.group(1)
                part = prefix_match.group(2)
                if current_section not in sections:
                    sections[current_section] = {}
            
            key, _, value = part.partition('=')
            if current_section not in sections:
                sections[current_section] = {}
            sections[current_section][key] = value
    
    return sections

def print_sections(sections, filter_key=None):
    for section_name, kvs in sections.items():
        filtered = {}
        for k, v in kvs.items():
            if filter_key and filter_key.lower() not in k.lower():
                continue
            filtered[k] = v
        
        if not filtered:
            continue
            
        print(f"\n[{section_name}]")
        print("-" * 60)
        for k, v in filtered.items():
            print(f"  {k} = {v}")

if __name__ == '__main__':
    filter_key = sys.argv[1] if len(sys.argv) > 1 else None
    
    print("Reading SystemCfg.ini from camera...")
    raw = get_camera_file('/etc/conf.d/syscfg/SystemCfg.ini')
    
    if not raw:
        print("ERROR: Could not read SystemCfg.ini")
        sys.exit(1)
    
    sections = parse_syscfg(raw)
    
    if filter_key:
        print(f"\nFiltering for: {filter_key}")
    
    print_sections(sections, filter_key)
    
    print(f"\n\nTotal sections: {len(sections)}")
    total_keys = sum(len(v) for v in sections.values())
    print(f"Total keys: {total_keys}")
