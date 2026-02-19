@echo off
echo ========================================
echo   MedTwin Backend - Starting...
echo ========================================
echo.

REM Check Java installation
echo Checking Java installation...
java -version 2>nul
if errorlevel 1 (
    echo ERROR: Java is not installed or not in PATH
    echo Please install Java 17 or higher
    echo.
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application...
echo This may take a few minutes on first run...
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
    pause
    exit /b 1
)

pause
