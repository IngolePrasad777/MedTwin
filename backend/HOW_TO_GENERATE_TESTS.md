# How to Generate Custom Test Cases with AI

## Quick Start

1. **Copy the API Testing Guide**
   - Open `API_TESTING_GUIDE.md`
   - Copy the entire content

2. **Paste to ChatGPT or Any AI**
   - Start a new conversation
   - Paste the guide
   - Add your specific test request

3. **Example Prompts**

### Basic Request
```
I've provided the MedTwin API documentation above. 
Please generate 10 test cases that cover:
- Happy path scenarios
- Edge cases
- Error handling
Output in PowerShell format.
```

### Advanced Request
```
Based on the API documentation provided, create a comprehensive test suite that:
1. Tests all 34 endpoints
2. Includes boundary value analysis
3. Tests anomaly detection with different severity levels
4. Validates the iterative optimization convergence
5. Tests concurrent async simulations
6. Includes negative test cases
7. Validates all business rules
Output as a PowerShell script with detailed assertions.
```

### Specific Feature Request
```
Generate test cases specifically for:
- RAG layer integration (endpoints 30-32)
- Anomaly detection with HIGH severity
- Optimization convergence in 3 iterations
- Compliance validation against all 5 standards
Include setup, execution, and validation steps.
```

### Performance Testing Request
```
Create performance test scenarios that:
- Test with 100 concurrent simulations
- Measure response times for each endpoint
- Test with maximum parameter values
- Validate system behavior under load
Output in Python using pytest.
```

### Integration Testing Request
```
Generate end-to-end integration tests for:
1. Complete workflow: Requirement → Architecture → Simulation → Insights
2. Master pipeline with full analysis
3. Scenario comparison with 3 different configurations
4. Digital twin state evolution over multiple simulations
Include data validation at each step.
```

---

## What You'll Get

The AI will generate test scripts with:
- ✅ Complete test setup
- ✅ API calls with proper parameters
- ✅ Assertions and validations
- ✅ Error handling
- ✅ Logging and reporting
- ✅ Comments explaining each test

---

## Supported Output Formats

Request tests in any format:
- **PowerShell** (`.ps1`)
- **Python** (`pytest`, `unittest`)
- **JavaScript** (`Jest`, `Mocha`)
- **cURL** commands
- **Postman** collections
- **JMeter** test plans

---

## Example AI Conversation

**You:**
```
[Paste API_TESTING_GUIDE.md content]

Generate 5 test cases for anomaly detection:
1. Test HIGH severity thermal spike
2. Test MEDIUM severity battery drain
3. Test multiple anomalies simultaneously
4. Test anomaly-free simulation
5. Test anomaly severity classification

Output in PowerShell with detailed comments.
```

**AI Will Generate:**
```powershell
# Test 1: HIGH Severity Thermal Spike
# Objective: Verify system detects thermal spike >20°C
$test1 = @{
    scenarioName = "High Thermal Test"
    batterySize = 5000
    samplingRate = 100
    airflowTarget = 80
    processingPower = 95
    thermalThreshold = 45
    powerMode = "PERFORMANCE"
}
# ... (complete test implementation)
```

---

## Tips for Better Test Generation

1. **Be Specific**: Mention exact endpoints, parameters, expected results
2. **Provide Context**: Explain what you're trying to validate
3. **Request Format**: Specify PowerShell, Python, etc.
4. **Include Assertions**: Ask for specific validation checks
5. **Error Scenarios**: Request both positive and negative tests

---

## Common Test Patterns to Request

### Pattern 1: Boundary Testing
```
Generate boundary tests for all numeric parameters:
- Test at minimum value
- Test at minimum + 1
- Test at maximum value
- Test at maximum - 1
- Test below minimum (expect error)
- Test above maximum (expect error)
```

### Pattern 2: State Transition Testing
```
Test the requirement status transitions:
DRAFT → VALIDATED → ARCHITECTURE_GENERATED
Verify each transition is valid and state is persisted.
```

### Pattern 3: Data Integrity Testing
```
Generate tests that verify:
- Simulation calculations are accurate
- Optimization improvements are real
- Anomaly detection thresholds are correct
- Compliance checks reference correct standards
```

### Pattern 4: Workflow Testing
```
Create tests for complete user workflows:
1. Medical device designer creates requirement
2. System generates architecture with RAG
3. Designer runs multiple simulations
4. System detects anomalies
5. Designer reviews AI insights
6. System optimizes configuration
7. Designer validates compliance
```

---

## Advanced Testing Scenarios

### Chaos Testing
```
Generate chaos tests that:
- Send malformed JSON
- Use invalid IDs
- Send requests out of order
- Test with missing required fields
- Test with null values
- Test with extremely large values
```

### Load Testing
```
Create load tests that:
- Run 100 simulations simultaneously
- Test database connection pooling
- Measure response time degradation
- Test memory usage under load
- Verify no data corruption
```

### Security Testing
```
Generate security tests for:
- SQL injection attempts (MongoDB)
- XSS in string fields
- Parameter tampering
- Unauthorized access attempts
- Rate limiting validation
```

---

## Sample Output You Can Expect

```powershell
# MedTwin Backend - Custom Test Suite
# Generated by AI based on API_TESTING_GUIDE.md

$baseUrl = "http://localhost:8080/api"
$testResults = @()

# Test 1: Boundary Value - Minimum Battery Capacity
Write-Host "Test 1: Minimum Battery Capacity (1000 mAh)" -ForegroundColor Yellow
try {
    $req = @{
        deviceType = "Ventilator"
        batteryCapacity = 1000  # Minimum allowed
        samplingRate = 100
        # ... other required fields
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/requirements" -Method POST -Body ($req | ConvertTo-Json) -ContentType "application/json"
    
    # Assertions
    if ($response.batteryCapacity -eq 1000) {
        Write-Host "  ✓ PASS: Battery capacity accepted at minimum" -ForegroundColor Green
        $testResults += @{Test="Min Battery"; Status="PASS"}
    }
} catch {
    Write-Host "  ✗ FAIL: $($_.Exception.Message)" -ForegroundColor Red
    $testResults += @{Test="Min Battery"; Status="FAIL"}
}

# Test 2: Anomaly Detection - HIGH Severity
# ... (more tests)

# Summary Report
Write-Host "`nTest Summary:" -ForegroundColor Cyan
$passed = ($testResults | Where-Object {$_.Status -eq "PASS"}).Count
$failed = ($testResults | Where-Object {$_.Status -eq "FAIL"}).Count
Write-Host "Passed: $passed" -ForegroundColor Green
Write-Host "Failed: $failed" -ForegroundColor Red
```

---

## Troubleshooting

**Q: AI doesn't understand the API structure**
- Make sure you pasted the complete `API_TESTING_GUIDE.md`
- Add more context about what you're testing

**Q: Generated tests don't run**
- Verify server is running on `localhost:8080`
- Check PowerShell execution policy
- Ensure all required fields are included

**Q: Need different test format**
- Explicitly request: "Output in Python using pytest"
- Provide example of desired format

---

## Next Steps

1. ✅ Copy `API_TESTING_GUIDE.md`
2. ✅ Paste to ChatGPT/Claude/Gemini
3. ✅ Request specific test scenarios
4. ✅ Run generated tests
5. ✅ Iterate and refine

---

**Happy Testing! 🚀**

Your MedTwin backend is production-ready and fully documented for AI-powered test generation.
