@echo off
setlocal enabledelayedexpansion
title Ghidra Headless Analysis - superb
echo ============================================================
echo  Ghidra Headless Analysis of superb binary
echo  This will take 10-30 minutes depending on your CPU
echo ============================================================
echo.

set "BASE=%~dp0"
set "SUPERB=%BASE%..\..\firmware\extracted\appfs\progs\bin\superb"
set "PROJECT=%BASE%project"
set "SCRIPTS=%BASE%scripts"
set "OUTPUT=%BASE%output"

REM Find Ghidra installation
set "GHIDRA_DIR="
for /d %%D in ("%BASE%ghidra_*") do set "GHIDRA_DIR=%%D"
if not defined GHIDRA_DIR (
    echo ERROR: Ghidra not found in %BASE%
    echo Run download_and_setup.cmd first.
    pause
    exit /b 1
)

set "HEADLESS=%GHIDRA_DIR%\support\analyzeHeadless.bat"
if not exist "%HEADLESS%" (
    echo ERROR: analyzeHeadless.bat not found at %HEADLESS%
    pause
    exit /b 1
)

REM Find JDK
set "JDK_HOME="
for /d %%D in ("C:\Program Files\Microsoft\jdk-*") do set "JDK_HOME=%%D"
if defined JDK_HOME (
    set "JAVA_HOME=!JDK_HOME!"
    set "PATH=!JDK_HOME!\bin;%PATH%"
    echo Using JDK: !JDK_HOME!
) else (
    where java >nul 2>&1
    if errorlevel 1 (
        echo ERROR: Java not found. Install JDK 17 first.
        pause
        exit /b 1
    )
)

if not exist "%SUPERB%" (
    echo ERROR: superb binary not found at %SUPERB%
    pause
    exit /b 1
)

echo Ghidra:  %GHIDRA_DIR%
echo Binary:  %SUPERB%
echo Project: %PROJECT%
echo Output:  %OUTPUT%
echo.

REM Phase 1: Import and auto-analyze
echo --- Phase 1: Import + Auto-Analysis (slow, ~10-20 min) ---
echo This creates the Ghidra project database with full analysis.
echo.

if exist "%PROJECT%\superb_project.rep" (
    echo Project already exists. Skipping import.
    echo To re-import, delete: %PROJECT%\superb_project.*
    echo.
    goto phase2
)

call "%HEADLESS%" "%PROJECT%" superb_project -import "%SUPERB%" -processor "ARM:LE:32:v7" -cspec "default" -analysisTimeoutPerFile 1800 -log "%OUTPUT%\import_log.txt" 2>&1
echo.
echo Import log: %OUTPUT%\import_log.txt
echo.

:phase2
echo --- Phase 2: Run extraction scripts ---
echo.

REM Run each script
for %%S in (
    extract_functions.py
    extract_isp_ioctls.py
    extract_cloud_handlers.py
    extract_strings_xrefs.py
) do (
    if exist "%SCRIPTS%\%%S" (
        echo Running %%S ...
        call "%HEADLESS%" "%PROJECT%" superb_project -process superb -noanalysis -scriptPath "%SCRIPTS%" -postScript "%%S" "%OUTPUT%" -log "%OUTPUT%\%%~nS_log.txt" 2>&1
        echo   Done. Output in %OUTPUT%\
        echo.
    ) else (
        echo SKIP: %SCRIPTS%\%%S not found
    )
)

echo.
echo ============================================================
echo  Analysis complete! Check output in:
echo  %OUTPUT%\
echo ============================================================
echo.
pause
