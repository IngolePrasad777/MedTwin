Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MedTwin Backend - Starting..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Check Java
Write-Host "Checking Java installation..." -ForegroundColor Yellow
try {
    $javaVersion = java -version 2>&1 | Select-String "version"
    Write-Host "✓ Java found: $javaVersion" -ForegroundColor Green
} catch {
    Write-Host "✗ ERROR: Java is not installed or not in PATH" -ForegroundColor Red
    Write-Host "Please install Java 17 or higher from:" -ForegroundColor Yellow
    Write-Host "https://www.oracle.com/java/technologies/downloads/" -ForegroundColor Yellow
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Set JAVA_HOME if not set
if (-not $env:JAVA_HOME) {
    Write-Host "Setting JAVA_HOME..." -ForegroundColor Yellow
    
    # Try to find Java installation
    $javaPath = (Get-Command java -ErrorAction SilentlyContinue).Source
    
    if ($javaPath) {
        # Get the bin directory
        $javaBin = Split-Path $javaPath
        
        # Check if it's in javapath (Oracle's symlink directory)
        if ($javaBin -like "*javapath*") {
            # Try common Java installation paths
            $possiblePaths = @(
                "C:\Program Files\Java\jdk-22",
                "C:\Program Files\Java\jdk-21",
                "C:\Program Files\Java\jdk-17",
                "C:\Program Files\Java\jdk-11",
                "C:\Program Files\Java\jdk1.8.0_*"
            )
            
            foreach ($path in $possiblePaths) {
                $resolved = Get-Item $path -ErrorAction SilentlyContinue | Select-Object -First 1
                if ($resolved) {
                    $env:JAVA_HOME = $resolved.FullName
                    break
                }
            }
        } else {
            # Direct Java installation
            $env:JAVA_HOME = Split-Path $javaBin
        }
        
        if ($env:JAVA_HOME) {
            Write-Host "✓ JAVA_HOME set to: $env:JAVA_HOME" -ForegroundColor Green
        } else {
            Write-Host "⚠ Could not determine JAVA_HOME automatically" -ForegroundColor Yellow
            Write-Host "Trying to run anyway..." -ForegroundColor Gray
        }
    }
}

Write-Host ""
Write-Host "Starting Spring Boot application..." -ForegroundColor Yellow
Write-Host "This may take a few minutes on first run (downloading dependencies)..." -ForegroundColor Gray
Write-Host ""
Write-Host "Once started, access the API at:" -ForegroundColor Cyan
Write-Host "  http://localhost:8080/api/health" -ForegroundColor White
Write-Host ""
Write-Host "Press Ctrl+C to stop the server" -ForegroundColor Gray
Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Run Maven
Write-Host "Running Maven Wrapper..." -ForegroundColor Yellow
Write-Host ""

# Check if mvnw.cmd exists
if (-not (Test-Path ".\mvnw.cmd")) {
    Write-Host "ERROR: mvnw.cmd not found!" -ForegroundColor Red
    Write-Host "Make sure you're running this from the backend folder" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "Current directory: $PWD" -ForegroundColor Gray
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

# Run Maven and capture output
$ErrorActionPreference = "Continue"
& .\mvnw.cmd spring-boot:run

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Red
    Write-Host "ERROR: Failed to start the application" -ForegroundColor Red
    Write-Host "Exit Code: $LASTEXITCODE" -ForegroundColor Red
    Write-Host "========================================" -ForegroundColor Red
    Write-Host ""
    Write-Host "Common solutions:" -ForegroundColor Yellow
    Write-Host "1. Make sure you're in the backend folder" -ForegroundColor White
    Write-Host "2. Make sure port 8080 is not in use" -ForegroundColor White
    Write-Host "3. Check your internet connection" -ForegroundColor White
    Write-Host "4. Try running as administrator" -ForegroundColor White
    Write-Host ""
    Write-Host "For detailed help, see HOW_TO_RUN.md" -ForegroundColor Cyan
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit 1
}

Read-Host "Press Enter to exit"
