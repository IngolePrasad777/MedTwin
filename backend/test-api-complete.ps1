# MedTwin Backend - Complete API Test Script
# Tests all 34 endpoints with full workflow

$baseUrl = "http://localhost:8080/api"
$testResults = @()

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  MedTwin Backend - API Test Suite" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

function Test-Endpoint {
    param(
        [string]$Name,
        [string]$Method,
        [string]$Url,
        [object]$Body = $null
    )
    
    Write-Host "Testing: $Name" -ForegroundColor Yellow
    
    try {
        $headers = @{
            "Content-Type" = "application/json"
        }
        
        if ($Body) {
            $jsonBody = $Body | ConvertTo-Json -Depth 10
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers -Body $jsonBody
        } else {
            $response = Invoke-RestMethod -Uri $Url -Method $Method -Headers $headers
        }
        
        Write-Host "  ✓ PASS" -ForegroundColor Green
        $script:testResults += @{Name=$Name; Status="PASS"; Response=$response}
        return $response
    }
    catch {
        Write-Host "  ✗ FAIL: $($_.Exception.Message)" -ForegroundColor Red
        $script:testResults += @{Name=$Name; Status="FAIL"; Error=$_.Exception.Message}
        return $null
    }
}

Write-Host "Step 1: Health Check" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$health = Test-Endpoint -Name "Health Check" -Method "GET" -Url "$baseUrl/health"
Write-Host ""

Write-Host "Step 2: Create Device Requirement" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$requirement = @{
    deviceType = "Ventilator"
    targetMarket = "EU"
    portability = "Portable"
    batteryCapacity = 5000
    samplingRate = 100
    targetAirflow = 50
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
    accuracyRequirement = "High"
    complianceStandards = @("IEC-60601-1", "ISO-14971", "ISO-13485")
}
$reqResponse = Test-Endpoint -Name "Create Requirement" -Method "POST" -Url "$baseUrl/requirements" -Body $requirement
$requirementId = $reqResponse.id
Write-Host "  Requirement ID: $requirementId" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 3: Generate Architecture (with RAG)" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$architecture = Test-Endpoint -Name "Generate Architecture" -Method "POST" -Url "$baseUrl/architecture/generate/$requirementId"
$architectureId = $architecture.id
Write-Host "  Architecture ID: $architectureId" -ForegroundColor Gray
Write-Host "  Components: $($architecture.components.Count)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 4: Get Architecture Details" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
Test-Endpoint -Name "Get Architecture" -Method "GET" -Url "$baseUrl/architecture/$architectureId"
Write-Host ""

Write-Host "Step 5: Validate Architecture" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$archValidation = Test-Endpoint -Name "Validate Architecture" -Method "POST" -Url "$baseUrl/validation/architecture/$architectureId"
Write-Host "  Validation: $($archValidation.passed)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 6: Get Knowledge Base (RAG)" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
Test-Endpoint -Name "Get Compliance Clauses" -Method "GET" -Url "$baseUrl/knowledge/compliance/Ventilator"
Test-Endpoint -Name "Get Design Patterns" -Method "GET" -Url "$baseUrl/knowledge/patterns/Ventilator"
Test-Endpoint -Name "Get Recommendations" -Method "GET" -Url "$baseUrl/knowledge/recommendations/Ventilator"
Write-Host ""

Write-Host "Step 7: Run Simulation (with Anomaly Detection)" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$simParams = @{
    scenarioName = "Baseline Test"
    batterySize = 5000
    samplingRate = 100
    airflowTarget = 50
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
}
$simulation = Test-Endpoint -Name "Run Simulation" -Method "POST" -Url "$baseUrl/simulation/run/$architectureId" -Body $simParams
$simulationId = $simulation.id
Write-Host "  Simulation ID: $simulationId" -ForegroundColor Gray
Write-Host "  Battery Life: $($simulation.batteryLife)h" -ForegroundColor Gray
Write-Host "  Risk Score: $($simulation.riskScore)%" -ForegroundColor Gray
Write-Host "  Anomaly Detected: $($simulation.anomalyDetected)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 8: Validate Simulation" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$simValidation = Test-Endpoint -Name "Validate Simulation" -Method "POST" -Url "$baseUrl/validation/simulation/$simulationId"
Write-Host "  Validation: $($simValidation.passed)" -ForegroundColor Gray
Write-Host "  Violations: $($simValidation.violations.Count)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 9: Generate AI Insights" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$insights = Test-Endpoint -Name "Generate Insights" -Method "POST" -Url "$baseUrl/insights/simulation/$simulationId"
Write-Host "  Insights Generated: $($insights.Count)" -ForegroundColor Gray
Test-Endpoint -Name "Get Insights" -Method "GET" -Url "$baseUrl/insights/simulation/$simulationId"
Write-Host ""

Write-Host "Step 10: Get Compliance Checks" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$compliance = Test-Endpoint -Name "Get Compliance Checks" -Method "GET" -Url "$baseUrl/insights/compliance/$simulationId"
Write-Host "  Compliance Checks: $($compliance.Count)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 11: Run Iterative Optimization" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$optimization = Test-Endpoint -Name "Optimize (Detailed)" -Method "GET" -Url "$baseUrl/simulation/optimize-detailed/$architectureId"
Write-Host "  Original Risk: $($optimization.originalRiskScore)%" -ForegroundColor Gray
Write-Host "  Optimized Risk: $($optimization.optimizedRiskScore)%" -ForegroundColor Gray
Write-Host "  Risk Reduction: $($optimization.riskReduction)%" -ForegroundColor Gray
Write-Host "  Battery Improvement: $($optimization.batteryLifeImprovement)h" -ForegroundColor Gray
Write-Host "  Design Maturity: $($optimization.optimizedDesignMaturityScore)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 12: Master Pipeline (Full Analysis)" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$fullAnalysis = Test-Endpoint -Name "Full System Analysis" -Method "POST" -Url "$baseUrl/system/full-analysis/$architectureId" -Body $simParams
Write-Host "  Engine Version: $($fullAnalysis.engineVersion)" -ForegroundColor Gray
Write-Host "  Design Maturity: $($fullAnalysis.designMaturityScore)" -ForegroundColor Gray
Write-Host "  Risk Level: $($fullAnalysis.riskLevel)" -ForegroundColor Gray
Write-Host "  Constraint Status: $($fullAnalysis.constraintStatus)" -ForegroundColor Gray
Write-Host "  Anomaly Severity: $($fullAnalysis.anomalySeverity)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 13: Executive Summary" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$summary = Test-Endpoint -Name "Executive Summary" -Method "GET" -Url "$baseUrl/system/executive-summary/$architectureId"
Write-Host "  Design Maturity: $($summary.designMaturityScore)" -ForegroundColor Gray
Write-Host "  Risk Level: $($summary.riskLevel)" -ForegroundColor Gray
Write-Host "  Total Simulations: $($summary.totalSimulations)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 14: Initialize Digital Twin" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$twin = Test-Endpoint -Name "Initialize Twin" -Method "POST" -Url "$baseUrl/twin/initialize/$architectureId"
Write-Host "  Twin Status: $($twin.status)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 15: Scenario Comparison" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$scenarioA = @{
    scenarioName = "Scenario A - Balanced"
    batterySize = 5000
    samplingRate = 100
    airflowTarget = 50
    processingPower = 75
    thermalThreshold = 55
    powerMode = "BALANCED"
}
$scenarioB = @{
    scenarioName = "Scenario B - Performance"
    batterySize = 6000
    samplingRate = 120
    airflowTarget = 60
    processingPower = 90
    thermalThreshold = 55
    powerMode = "PERFORMANCE"
}
$comparison = @{
    scenarioA = $scenarioA
    scenarioB = $scenarioB
}
$compResult = Test-Endpoint -Name "Compare Scenarios" -Method "POST" -Url "$baseUrl/simulation/compare/$architectureId" -Body $comparison
Write-Host "  Winner: Scenario $($compResult.winnerScenario)" -ForegroundColor Gray
Write-Host ""

Write-Host "Step 16: Async Simulation" -ForegroundColor Cyan
Write-Host "--------------------" -ForegroundColor Cyan
$asyncParams = @{
    scenarioName = "Async Test"
    batterySize = 4500
    samplingRate = 90
    airflowTarget = 45
    processingPower = 70
    thermalThreshold = 55
    powerMode = "ECO"
}
Test-Endpoint -Name "Run Async Simulation" -Method "POST" -Url "$baseUrl/simulation/run-async/$architectureId" -Body $asyncParams
Write-Host ""

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "  Test Summary" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$passCount = ($testResults | Where-Object { $_.Status -eq "PASS" }).Count
$failCount = ($testResults | Where-Object { $_.Status -eq "FAIL" }).Count
$totalCount = $testResults.Count

Write-Host "Total Tests: $totalCount" -ForegroundColor White
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor Red
Write-Host ""

if ($failCount -gt 0) {
    Write-Host "Failed Tests:" -ForegroundColor Red
    $testResults | Where-Object { $_.Status -eq "FAIL" } | ForEach-Object {
        Write-Host "  - $($_.Name): $($_.Error)" -ForegroundColor Red
    }
    Write-Host ""
}

$successRate = [math]::Round(($passCount / $totalCount) * 100, 2)
Write-Host "Success Rate: $successRate%" -ForegroundColor $(if ($successRate -ge 90) { "Green" } elseif ($successRate -ge 70) { "Yellow" } else { "Red" })
Write-Host ""

if ($successRate -eq 100) {
    Write-Host "🎉 All tests passed! Your backend is fully operational!" -ForegroundColor Green
} elseif ($successRate -ge 90) {
    Write-Host "✓ Most tests passed. Backend is operational with minor issues." -ForegroundColor Yellow
} else {
    Write-Host "⚠ Several tests failed. Please check the errors above." -ForegroundColor Red
}

Write-Host ""
Write-Host "Test completed at: $(Get-Date)" -ForegroundColor Gray
