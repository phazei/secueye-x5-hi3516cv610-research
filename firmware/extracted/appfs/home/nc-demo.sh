#!/bin/sh

IPCID=234
SERVERIP=192.168.11.35
SERVERMAC=8C:89:A5:6D:9D:94

cd /home/
insmod netconsole.ko netconsole=${IPCID}@/,514@$SERVERIP/$SERVERMAC
syslogd -L -R $SERVERIP:514
cd /progs
./bin/sctrl log=$IPCID&
