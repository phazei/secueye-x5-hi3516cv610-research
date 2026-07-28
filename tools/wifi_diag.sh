#!/bin/sh
# wifi_diag.sh -- WiFi boot diagnostics.
#
# Launched in the background by seculinkIdRecycle/recycle_ali.sh, which the
# vendor's /progs/updateID.sh sources as root on every boot (CAMERA.md:912).
#
# Mount-point caveat: at updateID.sh time the SD is at /var/udisk. Later,
# superb unmounts it and remounts at /progs/rec/00. So we log to tmpfs and
# flush to whichever SD mountpoint currently exists (same pattern as
# diag_run.sh -- FAT32 has no journal, so sync after every flush).
#
# Decisive test: can wlan0 SEE the AP?
#   scan lists APs  -> radio is fine, it's an auth/config problem
#   scan empty/fails or no wlan0 -> radio/driver path is likely dead
#
# Read the result by pulling the SD card: wifi_diag.log at the card root.
#
# Credentials: set these before deploying, or export them in the environment.
# Do NOT commit real values -- this file is tracked in a public repo.

TMP=/tmp/wifi_diag.log
IF=wlan0
SSID="${SSID:-YOUR_SSID}"
PSK="${PSK:-YOUR_PSK}"
OLDSSID="${OLDSSID:-YOUR_OLD_SSID}"

flush() {
    for d in /progs/rec/00 /var/udisk; do
        if mountpoint -q "$d" 2>/dev/null || [ -d "$d" ]; then
            cp "$TMP" "$d/wifi_diag.log" 2>/dev/null && sync && return
        fi
    done
}

run() { echo "\$ $*" >> "$TMP"; "$@" >> "$TMP" 2>&1; echo "" >> "$TMP"; }

{
    echo "==================================================================="
    echo "=== wifi_diag boot run: $(date) ==="
    echo "uptime: $(cat /proc/uptime 2>/dev/null)"
    echo "==================================================================="
} > "$TMP"

# ---- Static info ----
run uname -a
run ifconfig -a
echo "--- atbm wifi driver loaded? ---" >> "$TMP"
lsmod 2>/dev/null | grep -i atbm >> "$TMP"
echo "" >> "$TMP"
run lsmod

echo "--- wpa_supplicant.conf (all copies) ---" >> "$TMP"
find / -name "wpa_supplicant*.conf" -not -path "/proc/*" 2>/dev/null | \
while read -r f; do
    echo "### $f" >> "$TMP"
    cat "$f" >> "$TMP" 2>&1
    echo "" >> "$TMP"
done

echo "--- wifi vars in /home/variable ---" >> "$TMP"
grep -i wifi /home/variable >> "$TMP" 2>&1
echo "" >> "$TMP"
run cat /progs/networkcfg.sh
flush   # checkpoint: never lose the static section to a later hang

# ---- Locate the authoritative credential store ----
# Something restores the old SSID over our edit by ~18s. Find where it lives.
#
# NEVER recurse /var: /var/udisk is the mounted SD card (~22 GB of recordings).
# Grepping it from a slow ARM core over FAT32 hangs the script -- that bug ate
# the entire previous boot's log. Only search the small persistent partitions:
# configfs is 1 MB jffs2, /tmp is tmpfs, /home is a 6 MB squashfs bind.
echo "=== hunting stored OLDSSID ($OLDSSID) ===" >> "$TMP"
for d in /etc/conf.d /etc/network /tmp /home /var/run; do
    echo "--- grep -rl in $d ---" >> "$TMP"
    grep -rl "$OLDSSID" "$d" 2>/dev/null >> "$TMP"
    flush
done
echo "" >> "$TMP"
echo "--- /etc/conf.d/syscfg tree ---" >> "$TMP"
ls -laR /etc/conf.d/syscfg >> "$TMP" 2>&1
echo "" >> "$TMP"
echo "--- /etc/conf.d top level ---" >> "$TMP"
ls -la /etc/conf.d >> "$TMP" 2>&1
echo "" >> "$TMP"
flush

# ---- Snapshots: association state evolves over the first ~2 min ----
i=0
while [ $i -lt 8 ]; do
    {
        echo "------------------------------------------------------------------"
        echo "### snapshot $i  $(date)  uptime=$(cut -d' ' -f1 /proc/uptime 2>/dev/null)"
    } >> "$TMP"
    run ifconfig $IF
    run cat /proc/net/wireless
    # BusyBox here has no iwconfig/iwlist/iw/wpa_cli -- the vendor keeps its
    # WiFi tools in /home/wifi/. Use those instead.
    if [ -x /home/wifi/wpa_cli ]; then
        run /home/wifi/wpa_cli -i $IF status
        run /home/wifi/wpa_cli -i $IF list_networks
        run /home/wifi/wpa_cli -i $IF scan_results
    else
        echo "--- /home/wifi contents ---" >> "$TMP"
        ls -la /home/wifi >> "$TMP" 2>&1
        echo "" >> "$TMP"
    fi
    run cat /etc/network/wpa_supplicant.conf
    run route -n
    echo "--- wifi/dhcp processes ---" >> "$TMP"
    ps 2>/dev/null | grep -iE 'wpa|dhcp|udhcp|network' | grep -v grep >> "$TMP"
    echo "" >> "$TMP"
    # Association progress shows up in the atbm driver log.
    echo "--- recent atbm/wlan dmesg ---" >> "$TMP"
    dmesg 2>/dev/null | grep -iE 'atbm|wlan|wpa|assoc|auth' | tail -40 >> "$TMP"
    echo "" >> "$TMP"
    flush
    i=$((i + 1))
    sleep 15
done

# ---- Live fix attempt ----
# Editing wpa_supplicant.conf on disk is futile: superb pushes its stored SSID
# over the control interface and update_config=1 persists it back. So instead
# reconfigure the RUNNING wpa_supplicant directly. By now (~2 min) superb has
# finished its startup push, so our change is the last one to land.
#
# Non-destructive: no kill, no config file edit. A reboot fully reverts this.
WCLI=/home/wifi/wpa_cli
if [ -x "$WCLI" ]; then
    echo "=================================================================" >> "$TMP"
    echo "=== live fix attempt: switch to $SSID at $(date) ===" >> "$TMP"
    run $WCLI -i $IF list_networks
    # Reuse network 0 if present, else add one.
    ID=0
    run $WCLI -i $IF set_network $ID ssid "\"$SSID\""
    run $WCLI -i $IF set_network $ID psk "\"$PSK\""
    run $WCLI -i $IF set_network $ID key_mgmt WPA-PSK
    run $WCLI -i $IF set_network $ID scan_ssid 1
    run $WCLI -i $IF enable_network $ID
    run $WCLI -i $IF select_network $ID
    run $WCLI -i $IF reassociate

    j=0
    while [ $j -lt 6 ]; do
        {
            echo "--------------------------------------------------------------"
            echo "### post-fix check $j  $(date)  uptime=$(cut -d' ' -f1 /proc/uptime 2>/dev/null)"
        } >> "$TMP"
        run $WCLI -i $IF status
        run ifconfig $IF
        # Ask for a lease once associated.
        if ifconfig $IF 2>/dev/null | grep -q "inet addr"; then
            echo "*** ASSOCIATED WITH IP ***" >> "$TMP"
        else
            udhcpc -i $IF -q -n -t 3 >> "$TMP" 2>&1
        fi
        flush
        j=$((j + 1))
        sleep 10
    done
else
    echo "!!! $WCLI not executable -- cannot attempt live fix" >> "$TMP"
    ls -la /home/wifi >> "$TMP" 2>&1
fi

echo "--- dmesg (atbm/wlan/wifi/wpa) ---" >> "$TMP"
dmesg 2>/dev/null | grep -iE 'atbm|wlan|wifi|wpa' >> "$TMP"
echo "" >> "$TMP"
echo "=== wifi_diag done: $(date) ===" >> "$TMP"
flush
