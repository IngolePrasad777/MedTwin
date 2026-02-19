# MedTwin Backend - API Testing Guide for AI Test Generation

## Overview
This document provides complete API specifications for the MedTwin Backend to enable AI-powered test case generation. Use this to create custom, complex test scenarios.

**Base URL**: `http://localhost:8080/api`  
**Engine Version**: 1.2.0  
**Total Endpoints**: 34

---

## Table of Contents
1. [System Architecture](#system-architecture)
2. [Data Models](#data-models)
3. [API Endpoints](#api-endpoints)
4. [Test Scenarios](#test-scenarios)
5. [Validation Rules](#validation-rules)
6. [Expected Behaviors](#expected-behaviors)

---

## System Architecture

### Core Capabilities
1. **Requirement Processing** - Capture and validate device requirements
2. **Architecture Generation** - AI-powered system architecture with RAG
3. **Digital Twin State** - Real-time state management
4. **Simulation Engine** - Physics-based what-if analysis
5. **AI Insight Layer** - Intelligent recommendations

### Intelligence Features
- **RAG Layer** - Knowledge retrieval with ISO standards
- **Constraint Validation** - Safety/regulatory compliance
- **Graph Modeling** - Component dependency tracking
- **Iterative Optimization** - 3-5 iteration convergence
- **Anomaly Detection** - 4 types with severity classification
- **Async Execution** - Non-blocking simulation pipeline

---

## Data Models

### 1. DeviceRequirement
```json
{
  "id": "string (MongoDB ObjectId)",
  "deviceType": "string (e.g., Ventilator, Spirometer)",
  "deviceClass": "string (I, IIa, IIb, III)",
  "targetMarket": "string (US, EU, Global)",
  "portability": "string (Portable, Fixed, Mobile)",
  "complianceStandards": ["string array (IEC-60601-1, ISO-14971, etc.)"],
  "batteryCapacity": "integer (1000-10000 mAh)",
  "samplingRate": "integer (10-500 Hz)",
  "targetAirflow": "integer (10-200 L/min)",
  "processingPower": "integer (30-100 %)",
  "thermalThreshold": "integer (30-80 °C)",
  "powerMode": "enum (ECO, BALANCED, PERFORMANCE)",
  "accuracyRequirement": "string (High, Medium, Low)",
  "status": "enum (DRAFT, VALIDATED, ARCHITECTURE_GENERATED)"
}
```

### 2. SystemArchitecture
```json
{
  "id": "string",
  "requirement": "DeviceRequirement object",
  "architectureName": "string",
  "description": "string (includes RAG modifications)",
  "components": [
    {
      "componentName": "string",
      "componentType": "string (Controller, Sensor, Power, etc.)",
      "description": "string",
      "suggestedPart": "string",
      "specifications": [
        {
          "name": "string",
          "value": "string",
          "unit": "string"
        }
      ],
      "interfaces": ["string array"],
      "powerConsumption": "double (Watts)",
      "thermalOutput": "double (Watts)",
      "reliabilityScore": "integer (0-100)",
      "cost": "double (USD)"
    }
  ],
  "dependencyGraph": "object (component dependencies)",
  "confidenceScore": "double (0-1)",
  "complexityScore": "integer (0-100)",
  "estimatedCost": "double (USD)",
  "reliabilityScore": "integer (0-100)",
  "status": "enum (GENERATED, VALIDATED, OPTIMIZED)"
}
```

### 3. SimulationRun
```json
{
  "id": "string",
  "architecture": "SystemArchitecture reference",
  "scenarioName": "string",
  "batterySize": "integer (mAh)",
  "samplingRate": "integer (Hz)",
  "airflowTarget": "integer (L/min)",
  "processingPower": "integer (%)",
  "thermalThreshold": "integer (°C)",
  "powerMode": "enum",
  "batteryLife": "double (hours)",
  "thermalLoad": "double (°C)",
  "efficiency": "double (%)",
  "reliability": "double (%)",
  "riskScore": "double (%)",
  "riskLevel": "enum (LOW, MEDIUM, HIGH, CRITICAL)",
  "designMaturityScore": "double (0-100)",
  "anomalyDetected": "boolean",
  "anomalyType": "string",
  "anomalyDetails": "string",
  "anomalyCount": "integer",
  "anomalySeverity": "enum (NONE, LOW, MEDIUM, HIGH)",
  "dataPoints": [
    {
      "timeStep": "integer (hour)",
      "batteryLevel": "double (%)",
      "thermalLoad": "double (°C)",
      "efficiency": "double (%)",
      "powerConsumption": "double (W)",
      "riskScore": "double (%)"
    }
  ],
  "status": "enum (RUNNING, COMPLETED, FAILED)"
}
```

### 4. AIInsight
```json
{
  "id": "string",
  "simulationRun": "SimulationRun reference",
  "type": "enum (RISK_ALERT, OPTIMIZATION, PERFORMANCE_IMPROVEMENT, COMPLIANCE_WARNING, COST_REDUCTION)",
  "severity": "enum (INFO, WARNING, CRITICAL)",
  "title": "string",
  "description": "string",
  "recommendations": ["string array"],
  "confidenceScore": "double (0-1)",
  "impactScore": "double (0-100)",
  "reasoning": "string",
  "affectedSubsystem": "string",
  "improvementIfApplied": "string",
  "currentValue": "double",
  "projectedValue": "double",
  "improvementDelta": "double"
}
```

### 5. OptimizationResult
```json
{
  "originalBatteryLife": "double",
  "originalRiskScore": "double",
  "optimizedBatteryLife": "double",
  "optimizedRiskScore": "double",
  "batteryLifeImprovement": "double",
  "riskReduction": "double",
  "originalDesignMaturityScore": "double",
  "optimizedDesignMaturityScore": "double",
  "designMaturityImprovement": "double",
  "strategyApplied": "string (includes iteration count)",
  "reasoning": "string (includes iteration log)",
  "confidenceScore": "double"
}
```

---

## API Endpoints

### Health & System


#### 1. Health Check
- **Endpoint**: `GET /api/health`
- **Description**: Check if server is running
- **Response**: `{ "status": "UP", "version": "1.2.0", "timestamp": "ISO-8601", "service": "MedTwin Backend" }`

#### 2. Full System Analysis (Master Pipeline)
- **Endpoint**: `POST /api/system/full-analysis/{architectureId}`
- **Description**: Run complete analysis pipeline in one call
- **Body**: SimulationParameters (see below)
- **Response**: SystemAnalysisResult with simulation, optimization, insights
- **Features**: Runs simulation → validation → insights → optimization

#### 3. Executive Summary
- **Endpoint**: `GET /api/system/executive-summary/{architectureId}`
- **Description**: Dashboard-ready overview
- **Response**: High-level metrics, top risks, recommendations

---

### Requirements API (5 endpoints)

#### 4. Create Requirement
- **Endpoint**: `POST /api/requirements`
- **Body**: DeviceRequirement object
- **Validation**:
  - `batteryCapacity`: 1000-10000
  - `samplingRate`: 10-500
  - `targetAirflow`: 10-200
  - `processingPower`: 30-100
  - `thermalThreshold`: 30-80
  - `complianceStandards`: required array

#### 5. Get Requirement
- **Endpoint**: `GET /api/requirements/{id}`
- **Response**: DeviceRequirement object

#### 6. Get All Requirements
- **Endpoint**: `GET /api/requirements`
- **Response**: Array of DeviceRequirement

#### 7. Get Requirements by Type
- **Endpoint**: `GET /api/requirements/type/{deviceType}`
- **Response**: Filtered array

#### 8. Update Requirement
- **Endpoint**: `PUT /api/requirements/{id}`
- **Body**: Updated DeviceRequirement

---

### Architecture API (3 endpoints)

#### 9. Generate Architecture
- **Endpoint**: `POST /api/architecture/generate/{requirementId}`
- **Description**: AI-powered architecture generation with RAG
- **Process**:
  1. Retrieves compliance clauses from knowledge base
  2. Retrieves design patterns
  3. Generates base components (7 typical)
  4. Applies RAG-driven modifications
  5. Builds dependency graph
  6. Validates against constraints
- **Response**: SystemArchitecture with components and dependency graph

#### 10. Get Architecture
- **Endpoint**: `GET /api/architecture/{id}`
- **Response**: Complete SystemArchitecture

#### 11. Get Architecture by Requirement
- **Endpoint**: `GET /api/architecture/requirement/{requirementId}`
- **Response**: SystemArchitecture

---

### Digital Twin API (5 endpoints)

#### 12. Initialize Twin
- **Endpoint**: `POST /api/twin/initialize/{architectureId}`
- **Description**: Create digital twin state
- **Response**: DigitalTwinState with initial metrics

#### 13. Get Current State
- **Endpoint**: `GET /api/twin/state/{architectureId}`
- **Response**: Current DigitalTwinState

#### 14. Update State
- **Endpoint**: `PUT /api/twin/state/{architectureId}`
- **Body**: Updated DigitalTwinState

#### 15. Get State History
- **Endpoint**: `GET /api/twin/history/{architectureId}?limit=10`
- **Response**: Array of historical states

#### 16. Deactivate Twin
- **Endpoint**: `POST /api/twin/deactivate/{architectureId}`
- **Response**: 200 OK

---

### Simulation API (7 endpoints)

#### 17. Run Simulation
- **Endpoint**: `POST /api/simulation/run/{architectureId}`
- **Body**: SimulationParameters
```json
{
  "scenarioName": "string",
  "batterySize": "integer (1000-10000)",
  "samplingRate": "integer (10-500)",
  "airflowTarget": "integer (10-200)",
  "processingPower": "integer (30-100)",
  "thermalThreshold": "integer (30-80)",
  "powerMode": "ECO|BALANCED|PERFORMANCE"
}
```
- **Process**:
  1. Runs physics-based simulation (24 hours)
  2. Generates time-series data points
  3. Detects anomalies (4 types)
  4. Validates against constraints
  5. Calculates design maturity score
- **Response**: SimulationRun with anomaly detection

#### 18. Run Async Simulation
- **Endpoint**: `POST /api/simulation/run-async/{architectureId}`
- **Body**: SimulationParameters
- **Response**: `{ "status": "ACCEPTED", "message": "..." }`

#### 19. Get Simulation
- **Endpoint**: `GET /api/simulation/{id}`
- **Response**: SimulationRun

#### 20. Get Simulations by Architecture
- **Endpoint**: `GET /api/simulation/architecture/{architectureId}`
- **Response**: Array of SimulationRun (sorted by date desc)

#### 21. Get Optimized Parameters
- **Endpoint**: `GET /api/simulation/optimize/{architectureId}`
- **Response**: Optimized SimulationParameters

#### 22. Get Detailed Optimization
- **Endpoint**: `GET /api/simulation/optimize-detailed/{architectureId}`
- **Description**: Iterative optimization with before/after comparison
- **Process**:
  1. Runs original simulation
  2. Iterates 3-5 times adjusting parameters
  3. Converges when risk < 25% AND battery > 10h
  4. Stops if improvement < 2% (diminishing returns)
- **Response**: OptimizationResult with iteration log

#### 23. Compare Scenarios
- **Endpoint**: `POST /api/simulation/compare/{architectureId}`
- **Body**:
```json
{
  "scenarioA": SimulationParameters,
  "scenarioB": SimulationParameters
}
```
- **Response**: ScenarioComparison with winner and deltas

---

### AI Insights API (6 endpoints)

#### 24. Generate Simulation Insights
- **Endpoint**: `POST /api/insights/simulation/{simulationId}`
- **Description**: Generate AI recommendations
- **Types Generated**:
  - Risk alerts (if risk > 30%)
  - Battery optimization (if < 8h)
  - Thermal warnings (if exceeds threshold)
  - Efficiency improvements (if < 85%)
  - Compliance warnings (if < 95%)
  - Cost optimization (if impact > $20)
- **Response**: Array of AIInsight

#### 25. Generate Architecture Insights
- **Endpoint**: `POST /api/insights/architecture/{architectureId}`
- **Response**: Array of AIInsight

#### 26. Get Simulation Insights
- **Endpoint**: `GET /api/insights/simulation/{simulationId}`
- **Response**: Array of AIInsight

#### 27. Get Architecture Insights
- **Endpoint**: `GET /api/insights/architecture/{architectureId}`
- **Response**: Array of AIInsight

#### 28. Get Critical Insights
- **Endpoint**: `GET /api/insights/critical`
- **Response**: Array of CRITICAL severity insights

#### 29. Get Compliance Checks
- **Endpoint**: `GET /api/insights/compliance/{simulationId}`
- **Description**: Generate compliance traceability
- **Standards Checked**:
  - IEC 60601-1 (Thermal Safety)
  - ISO 13485 (Reliability)
  - ISO 14971 (Risk Management)
  - IEC 60601-1-6 (Battery Life)
  - IEC 62304 (Software Safety)
- **Response**: Array of ComplianceCheck

---

### Knowledge Service API (RAG Layer) (3 endpoints)

#### 30. Get Compliance Clauses
- **Endpoint**: `GET /api/knowledge/compliance/{deviceType}`
- **Description**: Retrieve relevant ISO/IEC clauses
- **Response**: Array of ComplianceClause with relevance scores

#### 31. Get Design Patterns
- **Endpoint**: `GET /api/knowledge/patterns/{deviceType}`
- **Description**: Retrieve applicable design patterns
- **Response**: Array of DesignPattern with applicability scores

#### 32. Get Recommendations
- **Endpoint**: `GET /api/knowledge/recommendations/{deviceType}`
- **Description**: Context-aware recommendations
- **Response**: Array of strings

---

### Constraint Validation API (2 endpoints)

#### 33. Validate Architecture
- **Endpoint**: `POST /api/validation/architecture/{id}`
- **Description**: Validate against IEC/ISO standards
- **Response**: ValidationResult with violations

#### 34. Validate Simulation
- **Endpoint**: `POST /api/validation/simulation/{id}`
- **Description**: Validate simulation results
- **Response**: ValidationResult with violations

---

## Test Scenarios

### Scenario Categories

#### 1. Happy Path Tests
- Create requirement → Generate architecture → Run simulation → Get insights
- Full pipeline with valid data
- Expected: All operations succeed

#### 2. Edge Case Tests
- **Minimum Values**: batteryCapacity=1000, samplingRate=10, etc.
- **Maximum Values**: batteryCapacity=10000, samplingRate=500, etc.
- **Boundary Testing**: Values at exact limits

#### 3. Anomaly Detection Tests
- **High Thermal**: processingPower=95, thermalThreshold=45
- **Battery Drain**: batterySize=2000, processingPower=90
- **Efficiency Drop**: samplingRate=200, powerMode=PERFORMANCE
- **Risk Spike**: Multiple high-risk parameters

#### 4. Optimization Tests
- **Poor Configuration**: Low battery, high power → Should optimize
- **Good Configuration**: Balanced parameters → Minimal changes
- **Extreme Configuration**: Test convergence behavior

#### 5. Validation Tests
- **Constraint Violations**: Thermal > threshold, battery < 8h
- **Compliance Failures**: Risk > 30%, reliability < 95%
- **Multiple Violations**: Test severity classification

#### 6. Concurrent Tests
- Multiple async simulations
- Parallel scenario comparisons
- Race condition testing

#### 7. Error Handling Tests
- Invalid IDs (non-existent)
- Missing required fields
- Out-of-range values
- Malformed JSON

#### 8. Integration Tests
- RAG → Architecture → Simulation → Insights (full flow)
- Optimization → Validation → Compliance
- Digital Twin state updates during simulation

---

## Validation Rules

### Input Validation
```
batteryCapacity: 1000 ≤ x ≤ 10000 (mAh)
samplingRate: 10 ≤ x ≤ 500 (Hz)
airflowTarget: 10 ≤ x ≤ 200 (L/min)
processingPower: 30 ≤ x ≤ 100 (%)
thermalThreshold: 30 ≤ x ≤ 80 (°C)
powerMode: ECO | BALANCED | PERFORMANCE
complianceStandards: non-empty array
```

### Business Rules
```
Battery Life Requirement: ≥ 8 hours
Risk Score Target: < 30%
Thermal Safety: ≤ thermalThreshold
Reliability Target: ≥ 95%
Compliance Score: ≥ 95%
Design Maturity: 0-100 (higher is better)
```

### Anomaly Detection Thresholds
```
Thermal Spike:
  - HIGH: >20°C increase
  - MEDIUM: >10°C increase
  - LOW: >5°C increase

Battery Drain:
  - HIGH: >25% decrease
  - MEDIUM: >15% decrease
  - LOW: >10% decrease

Efficiency Drop:
  - HIGH: >20% decrease
  - MEDIUM: >10% decrease
  - LOW: >5% decrease

Risk Spike:
  - HIGH: >30 point increase
  - MEDIUM: >20 point increase
  - LOW: >10 point increase
```

---

## Expected Behaviors

### Architecture Generation
- **Components**: Typically generates 7 base components
- **RAG Modifications**: May add 0-3 additional components based on compliance
- **Dependency Graph**: All components linked to Power Management Unit (root)
- **Confidence Score**: 0.85-0.95 typical range

### Simulation Results
- **Battery Life**: Inversely proportional to power consumption
- **Thermal Load**: Increases with processing power and airflow
- **Efficiency**: Decreases with thermal load and high power
- **Risk Score**: Composite of thermal, power, and battery risks
- **Time Series**: 24 data points (hourly)

### Optimization Behavior
- **Iterations**: 3-5 typical, stops when:
  - Risk < 25% AND battery > 10h
  - Improvement < 2% (diminishing returns)
  - No improvement for 2 consecutive iterations
- **Strategy**: Adjusts battery, sampling, processing, power mode
- **Improvement**: 10-30% typical risk reduction

### Anomaly Detection
- **Frequency**: Checks every time step (24 checks)
- **Types**: Can detect multiple anomaly types simultaneously
- **Severity**: Overall severity = highest individual anomaly
- **Details**: Provides specific time step and magnitude

---

## Example Test Cases to Generate

### Test Case Template
```
Test Name: [Descriptive name]
Category: [Happy Path | Edge Case | Error | etc.]
Objective: [What are we testing]
Steps:
  1. [Action]
  2. [Action]
  3. [Action]
Expected Results:
  - [Assertion]
  - [Assertion]
Validation:
  - [Check]
  - [Check]
```

### Suggested Complex Test Cases

1. **Multi-Stage Optimization Test**
   - Create requirement with poor parameters
   - Generate architecture
   - Run simulation (expect high risk)
   - Run optimization (expect 3+ iterations)
   - Validate optimized results (risk < 25%)

2. **Anomaly Cascade Test**
   - Create high-risk configuration
   - Run simulation
   - Verify multiple anomaly types detected
   - Check severity classification
   - Validate insights generated for each anomaly

3. **RAG Integration Test**
   - Create requirement for specific device type
   - Generate architecture
   - Verify RAG-driven components added
   - Check compliance clauses referenced
   - Validate dependency graph includes RAG components

4. **Concurrent Simulation Test**
   - Start 5 async simulations with different parameters
   - Poll for completion
   - Compare results
   - Verify no data corruption

5. **Compliance Traceability Test**
   - Run simulation with violations
   - Get compliance checks
   - Verify all 5 standards checked
   - Validate violation details include standard references
   - Check recommendations are actionable

6. **Scenario Comparison Matrix**
   - Create 3 different scenarios (ECO, BALANCED, PERFORMANCE)
   - Run pairwise comparisons
   - Verify winner determination logic
   - Check metric deltas are accurate

7. **Digital Twin State Evolution**
   - Initialize twin
   - Run multiple simulations
   - Check state updates
   - Verify history tracking
   - Test deactivation

8. **Boundary Value Analysis**
   - Test all parameters at min, max, min+1, max-1
   - Verify validation errors at boundaries
   - Check calculation accuracy at extremes

---

## AI Test Generation Instructions

When generating test cases, consider:

1. **Coverage**: Test all 34 endpoints
2. **Combinations**: Test parameter interactions
3. **Sequences**: Test multi-step workflows
4. **Failures**: Test error conditions
5. **Performance**: Test with large data sets
6. **Concurrency**: Test parallel operations
7. **Data Integrity**: Verify calculations
8. **Business Logic**: Validate domain rules

### Output Format
Generate tests in PowerShell, Python, or JavaScript format with:
- Clear test names
- Setup/teardown
- Assertions
- Error handling
- Logging

---

## Notes for AI

- All IDs are MongoDB ObjectIds (24-character hex strings)
- Timestamps are ISO-8601 format
- All numeric calculations are deterministic (no randomness)
- Async operations return immediately with 202 ACCEPTED
- Validation errors return 400 with detailed messages
- Server errors return 500 (should be rare)
- All responses include engineVersion: "1.2.0"

---

## Quick Start Example

```powershell
# 1. Create requirement
$req = @{
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
    complianceStandards = @("IEC-60601-1", "ISO-14971")
}
$reqResp = Invoke-RestMethod -Uri "http://localhost:8080/api/requirements" -Method POST -Body ($req | ConvertTo-Json) -ContentType "application/json"

# 2. Generate architecture
$arch = Invoke-RestMethod -Uri "http://localhost:8080/api/architecture/generate/$($reqResp.id)" -Method POST

# 3. Run simulation
$sim = @{
    scenarioName = "Test"
    batterySize = 5000
    samplingRate = 100
    airflowTarget = 50
    processingPower = 80
    thermalThreshold = 55
    powerMode = "BALANCED"
}
$simResp = Invoke-RestMethod -Uri "http://localhost:8080/api/simulation/run/$($arch.id)" -Method POST -Body ($sim | ConvertTo-Json) -ContentType "application/json"

# 4. Get insights
$insights = Invoke-RestMethod -Uri "http://localhost:8080/api/insights/simulation/$($simResp.id)" -Method POST
```

---

**End of API Testing Guide**

Use this document to generate comprehensive, complex test cases that validate all aspects of the MedTwin Backend system.
