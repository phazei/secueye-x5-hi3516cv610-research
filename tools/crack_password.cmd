@echo off
REM crack_password.cmd -- Crack camera root password DES hash using hashcat
REM
REM Hashes found via UART root shell:
REM   /etc/passwd (rootfs):  04h6XLo9zAfEM  (DES crypt, salt=04)
REM   /home/passwd (appfs):  GIgEh3ZZNHRh2  (DES crypt, salt=GI)
REM   /etc/shadow:           $6$kZOiX1vJ1cPLQ9X9$...  (SHA-512, much slower)
REM
REM DES crypt is hashcat mode 1500. Max password length is 8 characters.
REM On a 5090 this should finish in seconds for any password.
REM
REM Install hashcat: https://hashcat.net/hashcat/
REM   Download, extract, add to PATH or run from its directory.

echo ============================================
echo Camera Root Password Cracker
echo ============================================
echo.

REM Find hashcat and its installation directory
REM hashcat must run from its own directory so it can find OpenCL/ kernels
set HASHCAT=
set HASHCAT_DIR=

REM Check project-local hashcat first
if exist "%~dp0..\hashcat-7.1.2\hashcat.exe" (
    set "HASHCAT=%~dp0..\hashcat-7.1.2\hashcat.exe"
    set "HASHCAT_DIR=%~dp0..\hashcat-7.1.2"
    echo Found hashcat at project dir: %~dp0..\hashcat-7.1.2
    goto :found_hashcat
)

REM Check PATH
where hashcat.exe >nul 2>&1
if %errorlevel% equ 0 (
    for /f "delims=" %%i in ('where hashcat.exe') do (
        set "HASHCAT=%%i"
        set "HASHCAT_DIR=%%~dpi"
    )
    echo Found hashcat in PATH: %HASHCAT%
    goto :found_hashcat
)

echo hashcat not found.
echo.
echo Download from: https://hashcat.net/hashcat/
echo Extract to project dir or add to PATH.
pause
exit /b 1

:found_hashcat
echo Using: %HASHCAT%
echo OpenCL dir: %HASHCAT_DIR%OpenCL\
if not exist "%HASHCAT_DIR%OpenCL\" (
    echo ERROR: OpenCL directory not found at %HASHCAT_DIR%OpenCL\
    echo hashcat needs its OpenCL kernel files. Make sure the full hashcat
    echo package is extracted, not just hashcat.exe.
    pause
    exit /b 1
)
echo.

REM Use absolute paths for hash/output files since we cd into hashcat dir
set "OUTDIR=%~dp0..\firmware"
if not exist "%OUTDIR%" mkdir "%OUTDIR%"

REM Create hash files (use > without space to avoid trailing whitespace)
<nul set /p="04h6XLo9zAfEM"> "%OUTDIR%\hash_des_rootfs.txt"
<nul set /p="GIgEh3ZZNHRh2"> "%OUTDIR%\hash_des_appfs.txt"
<nul set /p="$6$kZOiX1vJ1cPLQ9X9$tOVj31T7yXhl1B1jCmnzznBv3YW1bSK9y16dAWHin0/evOSMs7AURmhvjrbYeW1Cd5TyIQjI8CLYHrZwi8IH7/"> "%OUTDIR%\hash_sha512_shadow.txt"

REM Change to hashcat directory so it can find OpenCL/ kernels
pushd "%HASHCAT_DIR%"

echo.
echo === Phase 1: Cracking DES hash (rootfs /etc/passwd) ===
echo Hash: 04h6XLo9zAfEM
echo Mode: 1500 (descrypt)
echo Attack: brute-force all printable ASCII, 1-8 chars
echo.

"%HASHCAT%" -m 1500 -a 3 -O "%OUTDIR%\hash_des_rootfs.txt" ?a?a?a?a?a?a?a?a --increment --increment-min 1 -o "%OUTDIR%\cracked_rootfs.txt"

echo.
if exist "%OUTDIR%\cracked_rootfs.txt" (
    echo === CRACKED (rootfs) ===
    type "%OUTDIR%\cracked_rootfs.txt"
) else (
    echo DES rootfs hash not cracked. Try with different attack mode.
)

echo.
echo === Phase 2: Cracking DES hash (appfs /home/passwd) ===
echo Hash: GIgEh3ZZNHRh2
echo.

"%HASHCAT%" -m 1500 -a 3 -O "%OUTDIR%\hash_des_appfs.txt" ?a?a?a?a?a?a?a?a --increment --increment-min 1 -o "%OUTDIR%\cracked_appfs.txt"

echo.
if exist "%OUTDIR%\cracked_appfs.txt" (
    echo === CRACKED (appfs) ===
    type "%OUTDIR%\cracked_appfs.txt"
) else (
    echo DES appfs hash not cracked.
)

echo.
echo === Phase 3: Cracking SHA-512 hash (shadow) ===
echo This is much slower than DES. Starting with common passwords...
echo Hash mode: 1800 (sha512crypt)
echo.

"%HASHCAT%" -m 1800 -a 3 -O "%OUTDIR%\hash_sha512_shadow.txt" ?a?a?a?a?a?a?a?a --increment --increment-min 1 -o "%OUTDIR%\cracked_shadow.txt"

echo.
if exist "%OUTDIR%\cracked_shadow.txt" (
    echo === CRACKED (shadow) ===
    type "%OUTDIR%\cracked_shadow.txt"
) else (
    echo SHA-512 shadow hash not cracked in brute-force pass.
    echo Try with a wordlist: hashcat -m 1800 hash_sha512_shadow.txt wordlist.txt
)

popd

echo.
echo ============================================
echo Done. Check firmware\ for results.
echo ============================================
pause
