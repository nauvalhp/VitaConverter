@echo off
chcp 65001 > nul
title VitaConverter - JDK Installer
color 0A

echo ========================================
echo     VitaConverter - JDK Installer
echo ========================================
echo.
echo This will install Java Development Kit (JDK)
echo Required for VitaConverter to run
echo.

echo Available JDK versions:
echo 1. JDK 8 (Recommended for compatibility)
echo 2. JDK 11 (LTS)
echo 3. JDK 17 (LTS)
echo 4. JDK 21 (Latest LTS)
echo.
set /p choice="Choose version (1-4, default=1): "

if "%choice%"=="1" (
    set "JDK_URL=https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u412-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u412b08.zip"
    set "JDK_VERSION=8"
) else if "%choice%"=="2" (
    set "JDK_URL=https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.23%2B9/OpenJDK11U-jdk_x64_windows_hotspot_11.0.23_9.zip"
    set "JDK_VERSION=11"
) else if "%choice%"=="3" (
    set "JDK_URL=https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.11%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.11_9.zip"
    set "JDK_VERSION=17"
) else if "%choice%"=="4" (
    set "JDK_URL=https://github.com/adoptium/temurin21-binaries/releases/download/jdk-21.0.3%2B9/OpenJDK21U-jdk_x64_windows_hotspot_21.0.3_9.zip"
    set "JDK_VERSION=21"
) else (
    set "JDK_URL=https://github.com/adoptium/temurin8-binaries/releases/download/jdk8u412-b08/OpenJDK8U-jdk_x64_windows_hotspot_8u412b08.zip"
    set "JDK_VERSION=8"
    echo Using default: JDK 8
)

echo.
echo Installing JDK %JDK_VERSION%...
echo Downloading from: %JDK_URL%
echo.

REM Download JDK
set "JDK_FILE=jdk%JDK_VERSION%.zip"
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri '%JDK_URL%' -OutFile '%JDK_FILE%'"

if not exist "%JDK_FILE%" (
    echo ERROR: Failed to download JDK.
    echo Please check your internet connection.
    pause
    exit /b 1
)

echo Extracting JDK...
powershell -Command "Expand-Archive -Path '%JDK_FILE%' -DestinationPath 'jdk%JDK_VERSION%' -Force"

if not exist "jdk%JDK_VERSION%" (
    echo ERROR: Failed to extract JDK.
    pause
    exit /b 1
)

echo Setting environment variables...

REM Set JAVA_HOME
set "JAVA_PATH=%CD%\jdk%JDK_VERSION%"
setx JAVA_HOME "%JAVA_PATH%"

REM Add to PATH
for /f "tokens=2*" %%a in ('reg query "HKCU\\Environment" /v PATH 2^>nul') do set "USER_PATH=%%b"
if "%USER_PATH%"=="" (
    setx PATH "%%JAVA_HOME%%\\bin"
) else (
    setx PATH "%%USER_PATH%%;%%JAVA_HOME%%\\bin"
)

echo.
echo ========================================
echo Installation Complete!
echo.
echo JAVA_HOME: %JAVA_PATH%
echo Added to PATH: %JAVA_PATH%\bin
echo.
echo Testing installation...
echo.
%JAVA_PATH%\bin\java -version
echo.
%JAVA_PATH%\bin\javac -version
echo.
echo ⚠️  Important: Please RESTART your computer
echo     for environment changes to take effect.
echo.
echo After restart, you can run VitaConverter normally.
echo ========================================
echo.
pause
pause