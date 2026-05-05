"""Brute-force crack DES crypt hash from camera root password.

DES crypt only uses the first 8 characters of a password and has no
key stretching, making it fast to brute-force even on CPU.

Hashes:
  rootfs /etc/passwd:     04h6XLo9zAfEM  (salt: 04)
  appfs  /home/passwd:    GIgEh3ZZNHRh2  (salt: GI)
  shadow /etc/shadow:     $6$kZOiX1vJ1cPLQ9X9$tOVj31T7yXhl1B1jCmnzznBv3YW1bSK9y16dAWHin0/evOSMs7AURmhvjrbYeW1Cd5TyIQjI8CLYHrZwi8IH7/
"""
import itertools
import string
import time
import sys
from passlib.hash import des_crypt, sha512_crypt

HASH1 = '04h6XLo9zAfEM'  # rootfs
HASH2 = 'GIgEh3ZZNHRh2'  # appfs

# First try a larger wordlist of common IoT/embedded passwords
wordlist = [
    # Xiongmai / HiSilicon known
    'xmhdipc', 'xc3511', 'klv123', 'I0TO5Wv9', 'tlJwpbo6',
    'hi3516', 'hi3518', 'hisi', 'hisilicon',
    # Common defaults
    'admin', 'root', 'password', 'pass', '123456', '12345678',
    'default', 'user', 'guest', '1234', 'test', 'toor',
    # Camera/DVR specific
    'ipc', 'camera', 'dvr', 'nvr', 'onvif', 'rtsp',
    'sofia', 'Sofia', 'SOFIA', 'superb', 'Superb',
    # Manufacturer related
    'seculink', 'secueye', 'jovision', 'jvs', 'JVS',
    'haoshiyou', 'mz0201',
    # From config files we found
    'ZKAcmKhE',  # danale_private.cfg
    # Common Chinese passwords
    'abc123', 'abcdef', '888888', '666666', '000000',
    'qwerty', 'letmein', 'welcome', 'monkey', 'dragon',
    # Numbers
    '1', '12', '123', '1234', '12345', '123456', '1234567', '12345678',
    '0', '00', '000', '0000', '00000', '000000',
    '111', '1111', '11111', '111111',
    '222', '2222', '333', '3333', '444', '4444',
    '555', '5555', '666', '6666', '777', '7777',
    '888', '8888', '999', '9999',
    # Embedded device passwords
    'admin1', 'admin123', 'root123', 'pass123',
    'system', 'support', 'service', 'operator',
    'vizxv', 'ikwb', 'dreambox', 'realtek', 'changeme',
    'jvbzd', 'ipc71a', 'hg2x0', 'S2fGqNFs',
    'ivdev', 'antslq', 'cat1029', 'juangsmart',
    'meinsm', 'hunt5759', 'jauntech',
    'hi3518ev', 'hi3516cv', 'hi3516', 'cv610',
    'hikvision', 'Hikvision', 'dahua', 'Dahua',
    'foscam', 'amcrest', 'reolink',
    # Simple patterns
    'a', 'aa', 'aaa', 'aaaa', 'aaaaa',
    'abc', 'abcd', 'abcde', 'abcdefg', 'abcdefgh',
    'qwer', 'qwert', 'qwerty', 'asdf', 'asdfgh',
    'zxcv', 'zxcvbn',
    # Empty
    '',
]

print(f'Phase 1: Trying {len(wordlist)} wordlist entries...')
t0 = time.time()
for pwd in wordlist:
    if des_crypt.verify(pwd, HASH1):
        print(f'*** CRACKED rootfs: [{pwd}] ***')
        sys.exit(0)
    if des_crypt.verify(pwd, HASH2):
        print(f'*** CRACKED appfs: [{pwd}] ***')
        sys.exit(0)

elapsed = time.time() - t0
rate = len(wordlist) / elapsed if elapsed > 0 else 0
print(f'Wordlist exhausted in {elapsed:.1f}s ({rate:.0f}/s)')

# Phase 2: Brute force short passwords (1-4 chars)
# DES crypt is fast enough for short brute force on CPU
charset = string.ascii_lowercase + string.digits
print(f'\nPhase 2: Brute-forcing 1-4 char passwords (charset: {charset})...')
t0 = time.time()
count = 0

for length in range(1, 5):
    print(f'  Length {length} ({len(charset)**length} combinations)...')
    for combo in itertools.product(charset, repeat=length):
        pwd = ''.join(combo)
        count += 1
        if des_crypt.verify(pwd, HASH1):
            print(f'*** CRACKED rootfs: [{pwd}] ***')
            sys.exit(0)
        if des_crypt.verify(pwd, HASH2):
            print(f'*** CRACKED appfs: [{pwd}] ***')
            sys.exit(0)
        if count % 10000 == 0:
            elapsed = time.time() - t0
            rate = count / elapsed if elapsed > 0 else 0
            print(f'    {count} tried, {rate:.0f}/s', flush=True)

elapsed = time.time() - t0
print(f'Phase 2 done: {count} in {elapsed:.1f}s ({count/elapsed:.0f}/s)')

# Phase 3: 5-char lowercase+digits (36^5 = 60M, might take a while)
print(f'\nPhase 3: Brute-forcing 5 char passwords...')
print(f'  This is {36**5} combinations, estimated {36**5/rate:.0f}s at current rate')
print(f'  Press Ctrl+C to stop')
t0 = time.time()
count = 0
for combo in itertools.product(charset, repeat=5):
    pwd = ''.join(combo)
    count += 1
    if des_crypt.verify(pwd, HASH1):
        print(f'*** CRACKED rootfs: [{pwd}] ***')
        sys.exit(0)
    if des_crypt.verify(pwd, HASH2):
        print(f'*** CRACKED appfs: [{pwd}] ***')
        sys.exit(0)
    if count % 50000 == 0:
        elapsed = time.time() - t0
        rate = count / elapsed if elapsed > 0 else 0
        print(f'    {count}/{36**5} ({100*count/36**5:.1f}%), {rate:.0f}/s', flush=True)
