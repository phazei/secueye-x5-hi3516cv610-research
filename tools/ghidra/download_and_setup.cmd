@echo off
setlocal enabledelayedexpansion
title Ghidra + JDK Download and Setup
echo ============================================================
echo  Ghidra + JDK 17 Download and Setup
echo ============================================================
echo.

set "BASE=%~dp0"
set "DOWNLOADS=%BASE%downloads"
set "GHIDRA_ZIP=%DOWNLOADS%\ghidra_12.0.4_PUBLIC_20260303.zip"
set "GHIDRA_DIR=%BASE%ghidra_12.0.4_PUBLIC"
set "JDK_MSI=%DOWNLOADS%\microsoft-jdk-17.0.18-windows-x64.msi"

if not exist "%DOWNLOADS%" mkdir "%DOWNLOADS%"

echo --- Step 1: Download JDK 17 (Microsoft OpenJDK, ~160MB) ---
if exist "%JDK_MSI%" (
    echo JDK already downloaded, skipping.
) else (
    echo Downloading JDK 17...
    curl -L -o "%JDK_MSI%" "https://aka.ms/download-JDK/microsoft-JDK-17.0.18-windows-x64.msi"
    if errorlevel 1 (
        echo ERROR: JDK download failed!
        pause
        exit /b 1
    )
)
echo.

echo --- Step 2: Install JDK 17 ---
echo This will open the JDK installer. Follow the prompts.
echo If JDK is already installed, close the installer.
echo.
start /wait msiexec /i "%JDK_MSI%"
echo.

REM Refresh PATH to pick up newly installed JDK
set "JDK_HOME=C:\Program Files\Microsoft\jdk-17.0.18.8-hotspot"
if exist "%JDK_HOME%\bin\java.exe" (
    set "JAVA_HOME=%JDK_HOME%"
    set "PATH=%JDK_HOME%\bin;%PATH%"
    echo JDK found at %JDK_HOME%
) else (
    echo WARNING: JDK not found at expected path. Searching...
    for /d %%D in ("C:\Program Files\Microsoft\jdk-*") do (
        set "JAVA_HOME=%%D"
        set "PATH=%%D\bin;%PATH%"
        echo Found JDK at %%D
    )
)
echo.

echo --- Step 3: Download Ghidra 12.0.4 (~487MB) ---
if exist "%GHIDRA_ZIP%" (
    echo Ghidra already downloaded, skipping.
) else (
    echo Downloading Ghidra 12.0.4...
    curl -L -o "%GHIDRA_ZIP%" "https://github.com/NationalSecurityAgency/ghidra/releases/download/Ghidra_12.0.4_build/ghidra_12.0.4_PUBLIC_20260303.zip"
    if errorlevel 1 (
        echo ERROR: Ghidra download failed!
        pause
        exit /b 1
    )
)
echo.

echo --- Step 4: Verify SHA-256 ---
echo Expected: c3b458661d69e26e203d739c0c82d143cc8a4a29d9e571f099c2cf4bda62a120
for /f "tokens=1" %%H in ('certutil -hashfile "%GHIDRA_ZIP%" SHA256 ^| findstr /v "hash SHA256"') do (
    echo Got:      %%H
)
echo.

echo --- Step 5: Extract Ghidra ---
if exist "%GHIDRA_DIR%" (
    echo Ghidra already extracted, skipping.
) else (
    echo Extracting Ghidra (this takes a minute)...
    powershell -Command "Expand-Archive -Path '%GHIDRA_ZIP%' -DestinationPath '%BASE%' -Force"
    if errorlevel 1 (
        echo ERROR: Extraction failed!
        pause
        exit /b 1
    )
)
echo.

echo --- Step 6: Verify Installation ---
if exist "%GHIDRA_DIR%\support\analyzeHeadless.bat" (
    echo.
    echo ============================================================
    echo  SUCCESS! Ghidra is ready.
    echo  Ghidra dir:  %GHIDRA_DIR%
    echo  Headless:    %GHIDRA_DIR%\support\analyzeHeadless.bat
    echo ============================================================
    echo.
    echo You can now run: analyze_superb.cmd
) else (
    echo.
    echo WARNING: analyzeHeadless.bat not found.
    echo Check that extraction succeeded. The folder may have a
    echo different name - check %BASE% for ghidra_* folders.
    dir /b /ad "%BASE%ghidra_*" 2>nul
)
echo.
pause
