@echo off
echo Starting camera uptime monitor (Ctrl+C to stop)
echo Logging to tools\uptime_log.txt
cd /d "%~dp0\.."
python tools\monitor_uptime.py 15
