# ==========================================
# MedTwin Backend - Interactive Demo Script
# Showcases all key features with explanations
# ==========================================

$baseUrl = "http://localhost:8080/api"

function Print-Section($title) {
    Write-Host "`n╔══════════════════════════════════════════════════════════╗" -ForegroundColor Cyan
    Write-Host "║  $title" -ForegroundColor Cyan
    Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Cyan
}

function Print-Step($number, $text) {
    Write-Host "`n[$number] $text" -ForegroundColor Yellow
}

function Print-Result($label, $value) {
    Write-Host "  $label" -NoNewline -ForegroundColor Gray
    Write-Host " $value" -ForegroundColor White
}

function Pause-Demo {
    Write-Host "`nPress any key to continue..." -ForegroundColor DarkGray
    $null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
}

Clear-Host
Write-Host "╔══════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "║        MedTwin Backend - Live Demo                       ║" -ForegroundColor Green
Write-Host "║        Medical Device Digital Twin Platform             ║" -ForegroundColor Green
Write-Host "║        Engine Version: 1.2.0                            ║" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Green

Pause-Demo

# =====================================================
# DEMO 1: Health Check
# =====================================================
Print-Section "DEMO 1: System Health Check"

Print-Step "1" "Checking if backend is running..."
$health = Invoke-RestMethod "$baseUrl/health"

Print-Result "Status:" $health.status
Print-Result "Version:" $health.version
Print-Result "Service:" $health.service

Pause-Demo

# =====================================================
# DEMO 2: Create Device Requirement
# =====================================================
Print-Section "DEMO 2: Create Device Requirement"

Print-Step "2" "Creating requirement for a portable medical ventilator..."

$requirement = @{
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
    complianceStandards = @("IEC-60601-1", "ISO-14971", "ISO-13485")
}

$reqResp = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST `
    -Body ($requirement | ConvertTo-Json -Depth 5) -ContentType "application/json"

Print-Result "Requirement ID:" $reqResp.id
Print-Result "Device Type:" $reqResp.deviceType
Print-Result "Battery Capacity:" "$($reqResp.batteryCapacity) mAh"
Print-Result "Target Airflow:" "$($reqResp.targetAirflow) L/min"
Print-Result "Status:" $reqResp.status

$requirementId = $reqResp.id

Pause-Demo

# =====================================================
# DEMO 3: RAG Layer - Knowledge Retrieval
# =====================================================
Print-Section "DEMO 3: RAG Layer - Knowledge Retrieval"

Print-Step "3" "Retrieving compliance clauses from knowledge base..."

$clauses = Invoke-RestMethod "$baseUrl/knowledge/compliance/Ventilator"

Write-Host "`n  Retrieved $($clauses.Count) compliance clauses:" -ForegroundColor White
foreach ($clause in $clauses | Select-Object -First 3) {
    Write-Host "    • $($clause.standard) - $($clause.clause)" -ForegroundColor Gray
    Write-Host "      Relevance: $([math]::Round($clause.relevanceScore * 100, 0))%" -ForegroundColor Cyan
}

Pause-Demo

# =====================================================
# DEMO 4: Architecture Generation with RAG
# =====================================================
Print-Section "DEMO 4: AI-Powered Architecture Generation"

Print-Step "4" "Generating system architecture with RAG integration..."

$arch = Invoke-RestMethod -Uri "$baseUrl/architecture/generate/$requirementId" -Method POST

Print-Result "Architecture ID:" $arch.id
Print-Result "Architecture Name:" $arch.architectureName
Print-Result "Components Generated:" $arch.components.Count
Print-Result "Confidence Score:" "$([math]::Round($arch.confidenceScore * 100, 0))%"
Print-Result "Estimated Cost:" "`$$($arch.estimatedCost)"
Print-Result "Reliability Score:" "$($arch.reliabilityScore)%"

Write-Host "`n  Key Components:" -ForegroundColor White
foreach ($comp in $arch.components | Select-Object -First 5) {
    Write-Host "    • $($comp.componentName) ($($comp.componentType))" -ForegroundColor Gray
    Write-Host "      Part: $($comp.suggestedPart)" -ForegroundColor DarkGray
}

$architectureId = $arch.id

Pause-Demo

# =====================================================
# DEMO 5: Run Simulation with Anomaly Detection
# =====================================================
Print-Section "DEMO 5: Physics-Based Simulation"

Print-Step "5" "Running 24-hour simulation with anomaly detection..."

$simParams = @{
    scenarioName = "Demo Baseline"
    batterySize = 5000
    samplingRate = 120
    airflowTarget = 60
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
}

$sim = Invoke-RestMethod -Uri "$baseUrl/simulation/run/$architectureId" -Method POST `
    -Body ($simParams | ConvertTo-Json -Depth 5) -ContentType "application/json"

Print-Result "Simulation ID:" $sim.id
Print-Result "Status:" $sim.status
Print-Result "Battery Life:" "$([math]::Round($sim.batteryLife, 2)) hours"
Print-Result "Thermal Load:" "$([math]::Round($sim.thermalLoad, 1))°C"
Print-Result "Efficiency:" "$([math]::Round($sim.efficiency, 1))%"
Print-Result "Risk Score:" "$([math]::Round($sim.riskScore, 1))%"
Print-Result "Risk Level:" $sim.riskLevel
Print-Result "Design Maturity:" "$([math]::Round($sim.designMaturityScore, 1))/100"

Write-Host "`n  Anomaly Detection:" -ForegroundColor White
Print-Result "  Detected:" $sim.anomalyDetected
if ($sim.anomalyDetected) {
    Print-Result "  Type:" $sim.anomalyType
    Print-Result "  Severity:" $sim.anomalySeverity
    Print-Result "  Count:" $sim.anomalyCount
}

$simulationId = $sim.id

Pause-Demo

# =====================================================
# DEMO 6: Constraint Validation
# =====================================================
Print-Section "DEMO 6: Constraint Validation"

Print-Step "6" "Validating simulation against IEC/ISO standards..."

$validation = Invoke-RestMethod -Uri "$baseUrl/validation/simulation/$simulationId" -Method POST

Print-Result "Validation Status:" $(if ($validation.passed) { "PASSED" } else { "FAILED" })
Print-Result "Overall Score:" "$([math]::Round($validation.overallScore, 1))%"
Print-Result "Violations Found:" $validation.violations.Count

if ($validation.violations.Count -gt 0) {
    Write-Host "`n  Violations:" -ForegroundColor White
    foreach ($violation in $validation.violations | Select-Object -First 3) {
        Write-Host "    • $($violation.constraintName)" -ForegroundColor Yellow
        Write-Host "      Severity: $($violation.severity)" -ForegroundColor $(if ($violation.severity -eq "CRITICAL") { "Red" } else { "Yellow" })
        Write-Host "      Recommendation: $($violation.recommendation)" -ForegroundColor Gray
    }
}

Pause-Demo

# =====================================================
# DEMO 7: AI Insights Generation
# =====================================================
Print-Section "DEMO 7: AI-Powered Insights"

Print-Step "7" "Generating intelligent recommendations..."

$insights = Invoke-RestMethod -Uri "$baseUrl/insights/simulation/$simulationId" -Method POST

Print-Result "Insights Generated:" $insights.Count

Write-Host "`n  Top Insights:" -ForegroundColor White
foreach ($insight in $insights | Select-Object -First 3) {
    Write-Host "    • $($insight.title)" -ForegroundColor Cyan
    Write-Host "      Type: $($insight.type) | Severity: $($insight.severity)" -ForegroundColor Gray
    Write-Host "      Confidence: $([math]::Round($insight.confidenceScore * 100, 0))%" -ForegroundColor DarkGray
    if ($insight.improvementIfApplied) {
        Write-Host "      Impact: $($insight.improvementIfApplied)" -ForegroundColor Green
    }
}

Pause-Demo

# =====================================================
# DEMO 8: Compliance Traceability
# =====================================================
Print-Section "DEMO 8: Compliance Traceability"

Print-Step "8" "Checking compliance against 5 medical device standards..."

$compliance = Invoke-RestMethod "$baseUrl/insights/compliance/$simulationId"

Print-Result "Standards Checked:" $compliance.Count

Write-Host "`n  Compliance Results:" -ForegroundColor White
foreach ($check in $compliance) {
    $statusColor = switch ($check.status) {
        "PASS" { "Green" }
        "WARNING" { "Yellow" }
        "FAIL" { "Red" }
        default { "Gray" }
    }
    Write-Host "    • $($check.standard) - $($check.clauseReference)" -ForegroundColor White
    Write-Host "      Status: $($check.status)" -ForegroundColor $statusColor
    Write-Host "      Current: $([math]::Round($check.currentValue, 1)) | Required: $([math]::Round($check.requiredValue, 1)) $($check.unit)" -ForegroundColor Gray
}

Pause-Demo

# =====================================================
# DEMO 9: Iterative Optimization
# =====================================================
Print-Section "DEMO 9: Iterative Optimization (3-5 iterations)"

Print-Step "9" "Running AI-powered optimization with convergence..."

$opt = Invoke-RestMethod "$baseUrl/simulation/optimize-detailed/$architectureId"

Print-Result "Strategy:" $opt.strategyApplied

Write-Host "`n  Before Optimization:" -ForegroundColor White
Print-Result "    Battery Life:" "$([math]::Round($opt.originalBatteryLife, 2))h"
Print-Result "    Risk Score:" "$([math]::Round($opt.originalRiskScore, 1))%"
Print-Result "    Design Maturity:" "$([math]::Round($opt.originalDesignMaturityScore, 1))/100"

Write-Host "`n  After Optimization:" -ForegroundColor White
Print-Result "    Battery Life:" "$([math]::Round($opt.optimizedBatteryLife, 2))h"
Print-Result "    Risk Score:" "$([math]::Round($opt.optimizedRiskScore, 1))%"
Print-Result "    Design Maturity:" "$([math]::Round($opt.optimizedDesignMaturityScore, 1))/100"

Write-Host "`n  Improvements:" -ForegroundColor Green
Print-Result "    Battery:" "+$([math]::Round($opt.batteryLifeImprovement, 2))h"
Print-Result "    Risk Reduction:" "-$([math]::Round($opt.riskReduction, 1))%"
Print-Result "    Maturity Gain:" "+$([math]::Round($opt.designMaturityImprovement, 1))"

Pause-Demo

# =====================================================
# DEMO 10: Master Pipeline
# =====================================================
Print-Section "DEMO 10: Master Pipeline (Full Analysis)"

Print-Step "10" "Running complete analysis in one API call..."

$fullAnalysis = Invoke-RestMethod -Uri "$baseUrl/system/full-analysis/$architectureId" -Method POST `
    -Body ($simParams | ConvertTo-Json -Depth 5) -ContentType "application/json"

Print-Result "Engine Version:" $fullAnalysis.engineVersion
Print-Result "Design Maturity:" "$([math]::Round($fullAnalysis.designMaturityScore, 1))/100"
Print-Result "Risk Level:" $fullAnalysis.riskLevel
Print-Result "Constraint Status:" $fullAnalysis.constraintStatus
Print-Result "Anomaly Severity:" $fullAnalysis.anomalySeverity
Print-Result "Optimized:" $fullAnalysis.optimized
Print-Result "Risk Reduction:" "$([math]::Round($fullAnalysis.riskReduction, 1))%"
Print-Result "Battery Improvement:" "+$([math]::Round($fullAnalysis.batteryImprovement, 2))h"
Print-Result "Iterations:" $fullAnalysis.iterations

Pause-Demo

# =====================================================
# DEMO 11: Executive Summary
# =====================================================
Print-Section "DEMO 11: Executive Summary (Dashboard)"

Print-Step "11" "Generating executive summary for stakeholders..."

$summary = Invoke-RestMethod "$baseUrl/system/executive-summary/$architectureId"

Print-Result "Design Maturity Score:" "$([math]::Round($summary.designMaturityScore, 1))/100"
Print-Result "Risk Level:" $summary.riskLevel
Print-Result "Compliance Status:" $summary.complianceStatus
Print-Result "Active Anomalies:" $summary.activeAnomalies
Print-Result "Total Simulations:" $summary.totalSimulations
Print-Result "Critical Insights:" $summary.criticalInsights

Write-Host "`n  Top Risks:" -ForegroundColor Yellow
foreach ($risk in $summary.topRisks | Select-Object -First 3) {
    Write-Host "    • $risk" -ForegroundColor Gray
}

Write-Host "`n  Top Recommendations:" -ForegroundColor Green
foreach ($rec in $summary.topRecommendations | Select-Object -First 3) {
    Write-Host "    • $rec" -ForegroundColor Gray
}

Pause-Demo

# =====================================================
# DEMO COMPLETE
# =====================================================

Write-Host "`n╔══════════════════════════════════════════════════════════╗" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "║              🎉 DEMO COMPLETE! 🎉                        ║" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "║  MedTwin Backend Features Demonstrated:                 ║" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "║  ✓ Requirement Processing                               ║" -ForegroundColor Green
Write-Host "║  ✓ RAG Layer (Knowledge Retrieval)                      ║" -ForegroundColor Green
Write-Host "║  ✓ AI-Powered Architecture Generation                   ║" -ForegroundColor Green
Write-Host "║  ✓ Physics-Based Simulation                             ║" -ForegroundColor Green
Write-Host "║  ✓ Anomaly Detection (4 types)                          ║" -ForegroundColor Green
Write-Host "║  ✓ Constraint Validation                                ║" -ForegroundColor Green
Write-Host "║  ✓ AI Insights Generation                               ║" -ForegroundColor Green
Write-Host "║  ✓ Compliance Traceability (5 standards)                ║" -ForegroundColor Green
Write-Host "║  ✓ Iterative Optimization (3-5 iterations)              ║" -ForegroundColor Green
Write-Host "║  ✓ Master Pipeline                                      ║" -ForegroundColor Green
Write-Host "║  ✓ Executive Summary                                    ║" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "║  Backend is production-ready! 🚀                        ║" -ForegroundColor Green
Write-Host "║                                                          ║" -ForegroundColor Green
Write-Host "╚══════════════════════════════════════════════════════════╝" -ForegroundColor Green

Write-Host "`nThank you for watching the MedTwin Backend demo!" -ForegroundColor Cyan
