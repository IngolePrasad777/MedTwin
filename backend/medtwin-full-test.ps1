# ==========================================
# MedTwin Backend - Full System Test Suite
# Engine Version: 1.2.0
# ==========================================

$baseUrl = "http://localhost:8080/api"
$results = @()
$ErrorActionPreference = "Stop"

function Add-Result($name, $status, $message="") {
    $script:results += [PSCustomObject]@{
        Test = $name
        Status = $status
        Message = $message
    }
}

function Print-Header($text) {
    Write-Host "`n========== $text ==========" -ForegroundColor Cyan
}

try {

# =====================================================
# 1️⃣ Health Check
# =====================================================
Print-Header "Health Check"

$health = Invoke-RestMethod "$baseUrl/health"
if ($health.status -eq "UP") {
    Add-Result "Health Check" "PASS"
} else {
    Add-Result "Health Check" "FAIL" "Server not UP"
}

# =====================================================
# 2️⃣ Create Requirement (Happy Path)
# =====================================================
Print-Header "Create Requirement"

$req = @{
    deviceType = "Ventilator"
    deviceClass = "IIb"
    targetMarket = "EU"
    portability = "Portable"
    batteryCapacity = 5000
    samplingRate = 120
    targetAirflow = 60
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
    accuracyRequirement = "High"
    complianceStandards = @("IEC-60601-1","ISO-14971")
}

$reqResp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
    -Body ($req | ConvertTo-Json -Depth 5) -ContentType "application/json"

Add-Result "Create Requirement" "PASS"
$requirementId = $reqResp.id

# =====================================================
# 3️⃣ Generate Architecture
# =====================================================
Print-Header "Generate Architecture"

$arch = Invoke-RestMethod -Uri "$baseUrl/architecture/generate/$requirementId" -Method POST
$architectureId = $arch.id

if ($arch.components.Count -ge 5) {
    Add-Result "Architecture Generation" "PASS"
} else {
    Add-Result "Architecture Generation" "FAIL" "Too few components"
}

# =====================================================
# 4️⃣ Run Simulation (Normal)
# =====================================================
Print-Header "Run Simulation"

$simParams = @{
    scenarioName = "Balanced Test"
    batterySize = 5000
    samplingRate = 120
    airflowTarget = 60
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
}

$sim = Invoke-RestMethod -Uri "$baseUrl/simulation/run/$architectureId" -Method POST `
    -Body ($simParams | ConvertTo-Json -Depth 5) -ContentType "application/json"

$simulationId = $sim.id

if ($sim.status -eq "COMPLETED") {
    Add-Result "Simulation Run" "PASS"
} else {
    Add-Result "Simulation Run" "FAIL"
}

# =====================================================
# 5️⃣ Test Anomaly Detection (High Thermal)
# =====================================================
Print-Header "Anomaly Detection Test"

$thermalTest = @{
    scenarioName = "High Thermal"
    batterySize = 4000
    samplingRate = 200
    airflowTarget = 100
    processingPower = 95
    thermalThreshold = 45
    powerMode = "PERFORMANCE"
}

$thermalSim = Invoke-RestMethod -Uri "$baseUrl/simulation/run/$architectureId" -Method POST `
    -Body ($thermalTest | ConvertTo-Json -Depth 5) -ContentType "application/json"

if ($thermalSim.anomalyDetected -eq $true) {
    Add-Result "Anomaly Detection" "PASS"
} else {
    Add-Result "Anomaly Detection" "FAIL"
}

# =====================================================
# 6️⃣ Test Optimization
# =====================================================
Print-Header "Optimization Test"

$opt = Invoke-RestMethod "$baseUrl/simulation/optimize-detailed/$architectureId"

if ($opt.optimizedRiskScore -lt $opt.originalRiskScore) {
    Add-Result "Optimization Improvement" "PASS"
} else {
    Add-Result "Optimization Improvement" "FAIL"
}

# =====================================================
# 7️⃣ Generate AI Insights
# =====================================================
Print-Header "AI Insights"

$insights = Invoke-RestMethod -Uri "$baseUrl/insights/simulation/$simulationId" -Method POST

if ($insights.Count -gt 0) {
    Add-Result "AI Insight Generation" "PASS"
} else {
    Add-Result "AI Insight Generation" "FAIL"
}

# =====================================================
# 8️⃣ Compliance Validation
# =====================================================
Print-Header "Compliance Validation"

$compliance = Invoke-RestMethod "$baseUrl/insights/compliance/$simulationId"

if ($compliance.Count -ge 1) {
    Add-Result "Compliance Check" "PASS"
} else {
    Add-Result "Compliance Check" "FAIL"
}

# =====================================================
# 9️⃣ Scenario Comparison
# =====================================================
Print-Header "Scenario Comparison"

$compare = @{
    scenarioA = $simParams
    scenarioB = $thermalTest
}

$compareResp = Invoke-RestMethod -Uri "$baseUrl/simulation/compare/$architectureId" `
    -Method POST -Body ($compare | ConvertTo-Json -Depth 5) -ContentType "application/json"

if ($compareResp.winnerScenario) {
    Add-Result "Scenario Comparison" "PASS"
} else {
    Add-Result "Scenario Comparison" "FAIL"
}

# =====================================================
# 🔟 Executive Summary
# =====================================================
Print-Header "Executive Summary"

$summary = Invoke-RestMethod "$baseUrl/system/executive-summary/$architectureId"

if ($summary.designMaturityScore -ge 0) {
    Add-Result "Executive Summary" "PASS"
} else {
    Add-Result "Executive Summary" "FAIL"
}

} catch {
    Add-Result "Unhandled Exception" "FAIL" $_.Exception.Message
}

# =====================================================
# 📊 TEST REPORT
# =====================================================

Print-Header "TEST SUMMARY"

$passed = ($results | Where-Object {$_.Status -eq "PASS"}).Count
$failed = ($results | Where-Object {$_.Status -eq "FAIL"}).Count

$results | Format-Table -AutoSize

Write-Host "`nTotal Tests: $($results.Count)"
Write-Host "Passed: $passed" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red

if ($failed -eq 0) {
    Write-Host "`n🎉 ALL TESTS PASSED - BACKEND READY FOR DEMO!" -ForegroundColor Green
} else {
    Write-Host "`n⚠ Some tests failed. Review issues before demo." -ForegroundColor Yellow
}
