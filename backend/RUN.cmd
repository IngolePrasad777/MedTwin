@echo off
echo ========================================
echo   MedTwin Backend - Starting...
echo ========================================
echo.

REM Find Java installation
echo Checking Java installation...
java -version 2>&1 | findstr /i "version" >nul
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher from:
    echo https://www.oracle.com/java/technologies/downloads/
    echo.
    pause
    exit /b 1
)

REM Set JAVA_HOME if not already set
if "%JAVA_HOME%"=="" (
    echo Setting JAVA_HOME...
    for /f "tokens=*" %%i in ('where java') do set JAVA_PATH=%%i
    for %%i in ("%JAVA_PATH%") do set JAVA_BIN=%%~dpi
    for %%i in ("%JAVA_BIN:~0,-5%") do set JAVA_HOME=%%~dpi
    set JAVA_HOME=%JAVA_HOME:~0,-1%
    echo JAVA_HOME set to: %JAVA_HOME%
)

echo.
echo Java version:
java -version
echo.
echo Starting Spring Boot application...
echo This may take a few minutes on first run (downloading dependencies)...
echo.
echo Once started, access the API at:
echo   http://localhost:8080/api/health
echo.
echo Press Ctrl+C to stop the server
echo.
echo ========================================
echo.

call mvnw.cmd spring-boot:run

if errorlevel 1 (
    echo.
    echo ========================================
    echo ERROR: Failed to start the application
    echo ========================================
    echo.
    echo Common solutions:
    echo 1. Make sure port 8080 is not in use
    echo 2. Check your internet connection
    echo 3. Try running as administrator
    echo.
    echo For detailed help, see HOW_TO_RUN.md
    echo.
    pause
    exit /b 1
)

pause
