@echo off
chcp 65001 > nul
echo ========================================
echo      VitaConverter - FFmpeg Installer
echo ========================================
echo.

REM ⭐ CEK JAVA/JDK
echo Checking Java installation...
java -version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Java/JDK not found!
    echo.
    echo Please install Java JDK first.
    echo Download from: https://adoptium.net/
    echo.
    echo Or run 'Install_JDK.bat' in this folder.
    echo.
    pause
    exit /b 1
)

echo ✓ Java detected. Continuing FFmpeg installation...
echo.

REM Download FFmpeg dari official build
echo Downloading FFmpeg from official source...
powershell -Command "[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://www.gyan.dev/ffmpeg/builds/ffmpeg-release-essentials.zip' -OutFile 'ffmpeg.zip'"

if not exist "ffmpeg.zip" (
    echo ERROR: Failed to download FFmpeg.
    echo Please check your internet connection.
    pause
    exit /b 1
)

echo Extracting FFmpeg...
powershell -Command "Expand-Archive -Path 'ffmpeg.zip' -DestinationPath '.' -Force"

REM Cari folder ffmpeg yang diextract
set "FFMPEG_DIR="
for /d %%i in (ffmpeg-*-essentials_build) do (
    set "FFMPEG_DIR=%%i"
)

if not defined FFMPEG_DIR (
    echo ERROR: Could not find extracted FFmpeg folder.
    pause
    exit /b 1
)

echo Adding FFmpeg to system PATH...
setx PATH "%%PATH%%;%%CD%%\%FFMPEG_DIR%\bin"

echo.
echo ========================================
echo Installation Summary:
echo.
echo FFmpeg installed to: %%CD%%\%FFMPEG_DIR%
echo Added to PATH: %%CD%%\%FFMPEG_DIR%\bin
echo.
echo Verifying installation...
ffmpeg -version
echo.
echo If you see FFmpeg version above, installation was successful.
echo.
echo ⚠️  Note: You may need to restart your computer or CMD/PowerShell
echo     for PATH changes to take effect.
echo ========================================
echo.
pause