# ==========================================
# MedTwin Backend - Chaos & Safety Test Suite
# Tests error handling, edge cases, and resilience
# ==========================================

$baseUrl = "http://localhost:8080/api"
$results = @()
$testCount = 0
$passCount = 0
$failCount = 0
$warnCount = 0

function Add-Result($name, $status, $message="") {
    $script:results += [PSCustomObject]@{
        Test = $name
        Status = $status
        Message = $message
    }
    $script:testCount++
    switch ($status) {
        "PASS" { $script:passCount++ }
        "FAIL" { $script:failCount++ }
        "WARN" { $script:warnCount++ }
    }
    
    $color = switch ($status) {
        "PASS" { "Green" }
        "FAIL" { "Red" }
        "WARN" { "Yellow" }
        default { "Gray" }
    }
    Write-Host "  [$status] $name" -ForegroundColor $color
    if ($message) {
        Write-Host "      $message" -ForegroundColor DarkGray
    }
}

function Print-Header($text) {
    Write-Host "`n========== $text ==========" -ForegroundColor Cyan
}

Write-Host "===========================================" -ForegroundColor Cyan
Write-Host "  MedTwin Chaos & Safety Test Suite" -ForegroundColor Cyan
Write-Host "===========================================" -ForegroundColor Cyan

# =====================================================
# 1️⃣ BOUNDARY VALUE TESTS
# =====================================================
Print-Header "Boundary Value Tests"

# Test minimum battery capacity
try {
    $minBattery = @{
        deviceType = "Ventilator"
        batteryCapacity = 1000  # Minimum
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($minBattery | ConvertTo-Json) -ContentType "application/json"
    Add-Result "Min Battery (1000)" "PASS"
} catch {
    Add-Result "Min Battery (1000)" "FAIL" $_.Exception.Message
}

# Test maximum battery capacity
try {
    $maxBattery = @{
        deviceType = "Ventilator"
        batteryCapacity = 10000  # Maximum
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($maxBattery | ConvertTo-Json) -ContentType "application/json"
    Add-Result "Max Battery (10000)" "PASS"
} catch {
    Add-Result "Max Battery (10000)" "FAIL" $_.Exception.Message
}

# Test below minimum (should fail)
try {
    $belowMin = @{
        deviceType = "Ventilator"
        batteryCapacity = 500  # Below minimum
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($belowMin | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    # If we get here, backend accepted invalid value
    Add-Result "Below Min Battery (500)" "WARN" "Backend accepted value below minimum"
} catch {
    Add-Result "Below Min Battery (500)" "PASS" "Correctly rejected invalid value"
}

# Test above maximum (should fail)
try {
    $aboveMax = @{
        deviceType = "Ventilator"
        batteryCapacity = 15000  # Above maximum
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($aboveMax | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Add-Result "Above Max Battery (15000)" "WARN" "Backend accepted value above maximum"
} catch {
    Add-Result "Above Max Battery (15000)" "PASS" "Correctly rejected invalid value"
}

# =====================================================
# 2️⃣ MISSING FIELD TESTS
# =====================================================
Print-Header "Missing Field Tests"

# Missing required field
try {
    $missingField = @{
        deviceType = "Ventilator"
        # Missing batteryCapacity
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($missingField | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Add-Result "Missing Required Field" "WARN" "Backend accepted request with missing field"
} catch {
    Add-Result "Missing Required Field" "PASS" "Correctly rejected missing field"
}

# =====================================================
# 3️⃣ INVALID ID TESTS
# =====================================================
Print-Header "Invalid ID Tests"

# Non-existent requirement ID
try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements/999999999999999999999999" -Method GET -ErrorAction Stop
    Add-Result "Non-existent ID" "WARN" "Backend returned data for non-existent ID"
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 404) {
        Add-Result "Non-existent ID" "PASS" "Correctly returned 404"
    } else {
        Add-Result "Non-existent ID" "PASS" "Correctly returned error"
    }
}

# Invalid ID format
try {
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements/invalid-id-format" -Method GET -ErrorAction Stop
    Add-Result "Invalid ID Format" "WARN" "Backend accepted invalid ID format"
} catch {
    if ($_.Exception.Response.StatusCode.value__ -eq 400) {
        Add-Result "Invalid ID Format" "PASS" "Correctly returned 400"
    } else {
        Add-Result "Invalid ID Format" "PASS" "Correctly returned error"
    }
}

# =====================================================
# 4️⃣ EXTREME VALUE TESTS
# =====================================================
Print-Header "Extreme Value Tests"

# Create valid requirement for extreme tests
$validReq = @{
    deviceType = "Ventilator"
    batteryCapacity = 5000
    samplingRate = 100
    targetAirflow = 50
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
    complianceStandards = @("IEC-60601-1")
}
$reqResp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
    -Body ($validReq | ConvertTo-Json) -ContentType "application/json"
$reqId = $reqResp.id

$archResp = Invoke-RestMethod -Uri "$baseUrl/architecture/generate/$reqId" -Method POST
$archId = $archResp.id

# Extreme high power configuration
try {
    $extremeHigh = @{
        scenarioName = "Extreme High"
        batterySize = 10000
        samplingRate = 500
        airflowTarget = 200
        processingPower = 100
        thermalThreshold = 80
        powerMode = "PERFORMANCE"
    }
    $sim = Invoke-RestMethod -Uri "$baseUrl/simulation/run/$archId" -Method POST `
        -Body ($extremeHigh | ConvertTo-Json) -ContentType "application/json"
    
    if ($sim.anomalyDetected) {
        Add-Result "Extreme High Config" "PASS" "Anomalies detected as expected"
    } else {
        Add-Result "Extreme High Config" "WARN" "No anomalies detected"
    }
} catch {
    Add-Result "Extreme High Config" "FAIL" $_.Exception.Message
}

# Extreme low power configuration
try {
    $extremeLow = @{
        scenarioName = "Extreme Low"
        batterySize = 1000
        samplingRate = 10
        airflowTarget = 10
        processingPower = 30
        thermalThreshold = 30
        powerMode = "ECO"
    }
    $sim = Invoke-RestMethod -Uri "$baseUrl/simulation/run/$archId" -Method POST `
        -Body ($extremeLow | ConvertTo-Json) -ContentType "application/json"
    
    Add-Result "Extreme Low Config" "PASS"
} catch {
    Add-Result "Extreme Low Config" "FAIL" $_.Exception.Message
}

# =====================================================
# 5️⃣ MALFORMED JSON TESTS
# =====================================================
Print-Header "Malformed JSON Tests"

# Invalid JSON structure
try {
    $headers = @{"Content-Type"="application/json"}
    $invalidJson = '{"deviceType": "Ventilator", "batteryCapacity": "not-a-number", "complianceStandards": ["IEC-60601-1"]}'
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body $invalidJson -Headers $headers -ErrorAction Stop
    Add-Result "Invalid JSON Type" "WARN" "Backend accepted string for numeric field"
} catch {
    Add-Result "Invalid JSON Type" "PASS" "Correctly rejected invalid type"
}

# =====================================================
# 6️⃣ ENUM VALIDATION TESTS
# =====================================================
Print-Header "Enum Validation Tests"

# Invalid power mode
try {
    $invalidEnum = @{
        deviceType = "Ventilator"
        batteryCapacity = 5000
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "INVALID_MODE"  # Invalid enum
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($invalidEnum | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Add-Result "Invalid Enum Value" "WARN" "Backend accepted invalid enum value"
} catch {
    Add-Result "Invalid Enum Value" "PASS" "Correctly rejected invalid enum"
}

# =====================================================
# 7️⃣ NULL VALUE TESTS
# =====================================================
Print-Header "Null Value Tests"

# Null in required field
try {
    $nullValue = @{
        deviceType = "Ventilator"
        batteryCapacity = $null
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($nullValue | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Add-Result "Null Required Field" "WARN" "Backend accepted null in required field"
} catch {
    Add-Result "Null Required Field" "PASS" "Correctly rejected null value"
}

# =====================================================
# 8️⃣ EMPTY ARRAY TESTS
# =====================================================
Print-Header "Empty Array Tests"

# Empty compliance standards
try {
    $emptyArray = @{
        deviceType = "Ventilator"
        batteryCapacity = 5000
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @()  # Empty array
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($emptyArray | ConvertTo-Json) -ContentType "application/json" -ErrorAction Stop
    Add-Result "Empty Compliance Array" "WARN" "Backend accepted empty compliance standards"
} catch {
    Add-Result "Empty Compliance Array" "PASS" "Correctly rejected empty array"
}

# =====================================================
# 9️⃣ SPECIAL CHARACTER TESTS
# =====================================================
Print-Header "Special Character Tests"

# Special characters in string fields
try {
    $specialChars = @{
        deviceType = "Ventilator<script>alert('xss')</script>"
        batteryCapacity = 5000
        samplingRate = 100
        targetAirflow = 50
        processingPower = 50
        thermalThreshold = 55
        powerMode = "BALANCED"
        complianceStandards = @("IEC-60601-1")
    }
    $resp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
        -Body ($specialChars | ConvertTo-Json) -ContentType "application/json"
    
    # Check if special chars were sanitized or stored as-is
    if ($resp.deviceType -match "<script>") {
        Add-Result "XSS Prevention" "WARN" "Special chars not sanitized"
    } else {
        Add-Result "XSS Prevention" "PASS" "Special chars handled"
    }
} catch {
    Add-Result "XSS Prevention" "PASS" "Request rejected"
}

# =====================================================
# 🔟 CONCURRENT REQUEST TESTS
# =====================================================
Print-Header "Concurrent Request Tests"

# Multiple simultaneous requests
try {
    $jobs = @()
    for ($i = 1; $i -le 5; $i++) {
        $job = Start-Job -ScriptBlock {
            param($url, $data)
            Invoke-RestMethod -Uri $url -Method POST `
                -Body ($data | ConvertTo-Json) -ContentType "application/json"
        } -ArgumentList "$baseUrl/requirements", $validReq
        $jobs += $job
    }
    
    $completed = Wait-Job -Job $jobs -Timeout 30
    $results = Receive-Job -Job $jobs
    Remove-Job -Job $jobs
    
    if ($results.Count -eq 5) {
        Add-Result "Concurrent Requests (5)" "PASS"
    } else {
        Add-Result "Concurrent Requests (5)" "WARN" "Only $($results.Count) completed"
    }
} catch {
    Add-Result "Concurrent Requests (5)" "FAIL" $_.Exception.Message
}

# =====================================================
# 📊 TEST REPORT
# =====================================================

Print-Header "CHAOS TEST SUMMARY"

Write-Host "`nTest Results:" -ForegroundColor White
$results | Format-Table -AutoSize

Write-Host "`nStatistics:" -ForegroundColor White
Write-Host "Total Tests: $testCount"
Write-Host "Passed: $passCount" -ForegroundColor Green
Write-Host "Failed: $failCount" -ForegroundColor $(if ($failCount -gt 0) { "Red" } else { "Green" })
Write-Host "Warnings: $warnCount" -ForegroundColor $(if ($warnCount -gt 0) { "Yellow" } else { "Green" })

$safetyScore = if ($testCount -gt 0) { [math]::Round(($passCount / $testCount) * 100, 2) } else { 0 }
Write-Host "`nSafety Score: $safetyScore%" -ForegroundColor $(if ($safetyScore -ge 80) { "Green" } elseif ($safetyScore -ge 60) { "Yellow" } else { "Red" })

if ($failCount -eq 0 -and $warnCount -eq 0) {
    Write-Host "`n🛡️ EXCELLENT! Backend is resilient and handles errors gracefully!" -ForegroundColor Green
} elseif ($failCount -eq 0) {
    Write-Host "`n✓ Good! Backend handles most edge cases. Review warnings." -ForegroundColor Yellow
} else {
    Write-Host "`n⚠ Some safety issues detected. Review failed tests." -ForegroundColor Red
}
