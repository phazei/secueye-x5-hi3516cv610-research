# SD Card Root Access for SECUEYE X5 (Hi3516CV610)

Get a root shell on the SECUEYE X5 camera without UART or soldering.
The stock firmware runs an arbitrary script from the SD card on every
boot. We use this to start a root shell on TCP port 9999.

## What you need

- The camera, powered on and connected to your WiFi (use the Secueye
  app for initial WiFi setup)
- A microSD card formatted as FAT32
- A computer on the same network
- `nc` (netcat) -- available on Linux/macOS by default, or use
  `ncat` from Nmap on Windows

## Stage 1: Temporary root shell via SD card

1. Format the microSD card as **FAT32**.

2. Copy the `seculinkIdRecycle/` folder from this directory to the
   **root** of the SD card. The card should look like:
   ```
   (SD card root)/
     seculinkIdRecycle/
       recycle_ali.sh
   ```

3. Insert the SD card into the camera and power cycle it (unplug USB-C,
   wait a few seconds, plug back in).

4. Wait about 60 seconds for the camera to boot.

5. Find the camera's IP address (check your router's DHCP lease table,
   or use `arp -a` / a network scanner). The MAC address OUI is
   `38:77:07` (Xiongmai).

6. Connect to the root shell:
   ```
   nc <camera-ip> 9999
   ```
   You should get a `#` prompt. Type `whoami` to confirm you're root.

You now have root access. This shell will persist until the camera
reboots. The SD card vector re-runs on every boot, so the shell comes
back after a reboot as long as the SD card is inserted.

## Stage 2: Persistent root shell (survives SD card removal)

The SD card script runs early in boot but depends on the card being
present. For persistence without the SD card, install a boot hook to
the camera's configfs partition (jffs2, survives reboots and factory
resets).

From the root shell (stage 1), run:

```sh
cat > /etc/conf.d/debug.sh << 'EOF'
#!/bin/sh
tcpsvd 0.0.0.0 9999 /bin/sh -il &
/tmp/appfs/progs/bin/superb &
sleep 15
cd /progs/rec/00/PQtool && ./PQTools.sh -c
EOF
chmod +x /etc/conf.d/debug.sh
```

Reboot the camera (unplug/replug or type `reboot`). The root shell on
port 9999 is now permanent. The SD card is no longer needed for access
(though you can leave it in for recordings).

### What does debug.sh do?

The stock boot chain (`startup.sh`) checks for `/etc/conf.d/debug.sh`.
If it exists, it runs that instead of the default startup path. Our
minimal `debug.sh` does three things:

1. Starts `tcpsvd` on port 9999 (root shell, same as the SD card script)
2. Launches `superb` (the stock camera daemon -- RTSP, recording, cloud)
3. Runs PQTools after 15 seconds (ISP image quality calibration)

The camera works exactly as before, you just have a root shell alongside
it.

## What next

With root access you can:

- View the RTSP streams directly: `rtsp://<camera-ip>/live1` (4K) or
  `/live2` (sub-stream) in VLC or MPV
- Explore the filesystem, dump firmware, inspect running processes
- Follow the main project README for the full custom firmware setup
  (SSH, custom daemon, sensor driver, etc.)

## Security warning

The root shell on port 9999 has **no authentication**. Anyone on your
local network can connect and run commands as root. This is fine for a
home lab but do not expose this camera to the internet.

## Compatibility

Tested on SECUEYE X5 with firmware `MZ0201V160_EN_20251126`. Other
Xiongmai/XMeye-based cameras with the Hi3516CV610 SoC may have the
same `updateID.sh` SD card vector, but this is not guaranteed. A
firmware update from the manufacturer could patch this entry point.
