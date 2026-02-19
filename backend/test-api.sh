#!/bin/bash

# MedTwin Backend API Test Script
# Tests all 5 core capabilities

BASE_URL="http://localhost:8080/api"
GREEN='\033[0;32m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║   MedTwin Backend - API Test Suite                       ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Test 1: Health Check
echo -e "${BLUE}[TEST 1]${NC} Health Check..."
HEALTH=$(curl -s $BASE_URL/health)
if [[ $HEALTH == *"UP"* ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Server is running"
else
    echo -e "${RED}✗ FAILED${NC} - Server is not responding"
    exit 1
fi
echo ""

# Test 2: Create Requirement
echo -e "${BLUE}[TEST 2]${NC} Creating Device Requirement..."
REQ_RESPONSE=$(curl -s -X POST $BASE_URL/requirements \
  -H "Content-Type: application/json" \
  -d '{
    "deviceType": "Ventilator",
    "deviceClass": "IIb",
    "powerSource": "Battery Backup",
    "portability": "Portable",
    "complianceStandards": ["IEC-60601-1", "ISO-14971", "ISO-13485"],
    "functionalRequirements": "Portable ventilator for emergency use",
    "targetAirflow": 45,
    "samplingRate": 100,
    "batteryCapacity": 5000,
    "processingPower": 75,
    "thermalThreshold": 60,
    "powerMode": "BALANCED"
  }')

REQ_ID=$(echo $REQ_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
if [[ ! -z "$REQ_ID" ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Requirement created with ID: $REQ_ID"
else
    echo -e "${RED}✗ FAILED${NC} - Could not create requirement"
    exit 1
fi
echo ""

# Test 3: Generate Architecture
echo -e "${BLUE}[TEST 3]${NC} Generating System Architecture..."
ARCH_RESPONSE=$(curl -s -X POST $BASE_URL/architecture/generate/$REQ_ID)
ARCH_ID=$(echo $ARCH_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
if [[ ! -z "$ARCH_ID" ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Architecture generated with ID: $ARCH_ID"
    COMPONENT_COUNT=$(echo $ARCH_RESPONSE | grep -o '"componentName"' | wc -l)
    echo "  Components generated: $COMPONENT_COUNT"
else
    echo -e "${RED}✗ FAILED${NC} - Could not generate architecture"
    exit 1
fi
echo ""

# Test 4: Initialize Digital Twin
echo -e "${BLUE}[TEST 4]${NC} Initializing Digital Twin..."
TWIN_RESPONSE=$(curl -s -X POST $BASE_URL/twin/initialize/$ARCH_ID)
TWIN_ID=$(echo $TWIN_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*')
if [[ ! -z "$TWIN_ID" ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Digital Twin initialized with ID: $TWIN_ID"
    HEALTH_SCORE=$(echo $TWIN_RESPONSE | grep -o '"healthScore":[0-9]*' | grep -o '[0-9]*')
    echo "  Initial Health Score: $HEALTH_SCORE"
else
    echo -e "${RED}✗ FAILED${NC} - Could not initialize digital twin"
    exit 1
fi
echo ""

# Test 5: Run Simulation
echo -e "${BLUE}[TEST 5]${NC} Running What-If Simulation..."
SIM_RESPONSE=$(curl -s -X POST $BASE_URL/simulation/run/$ARCH_ID \
  -H "Content-Type: application/json" \
  -d '{
    "scenarioName": "Test Scenario",
    "batterySize": 5000,
    "samplingRate": 100,
    "airflowTarget": 45,
    "processingPower": 75,
    "thermalThreshold": 60,
    "powerMode": "BALANCED"
  }')

SIM_ID=$(echo $SIM_RESPONSE | grep -o '"id":[0-9]*' | grep -o '[0-9]*' | head -1)
if [[ ! -z "$SIM_ID" ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Simulation completed with ID: $SIM_ID"
    BATTERY_LIFE=$(echo $SIM_RESPONSE | grep -o '"batteryLife":[0-9.]*' | grep -o '[0-9.]*')
    RISK_SCORE=$(echo $SIM_RESPONSE | grep -o '"riskScore":[0-9.]*' | grep -o '[0-9.]*')
    echo "  Battery Life: ${BATTERY_LIFE}h"
    echo "  Risk Score: ${RISK_SCORE}%"
else
    echo -e "${RED}✗ FAILED${NC} - Could not run simulation"
    exit 1
fi
echo ""

# Test 6: Get AI Insights
echo -e "${BLUE}[TEST 6]${NC} Fetching AI Insights..."
INSIGHTS_RESPONSE=$(curl -s $BASE_URL/insights/simulation/$SIM_ID)
INSIGHT_COUNT=$(echo $INSIGHTS_RESPONSE | grep -o '"title"' | wc -l)
if [[ $INSIGHT_COUNT -gt 0 ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Generated $INSIGHT_COUNT AI insights"
else
    echo -e "${RED}✗ FAILED${NC} - No insights generated"
    exit 1
fi
echo ""

# Test 7: Get Current State
echo -e "${BLUE}[TEST 7]${NC} Fetching Current Digital Twin State..."
STATE_RESPONSE=$(curl -s $BASE_URL/twin/state/$ARCH_ID)
if [[ $STATE_RESPONSE == *"batteryLevel"* ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - Digital Twin state retrieved"
    BATTERY_LEVEL=$(echo $STATE_RESPONSE | grep -o '"batteryLevel":[0-9.]*' | grep -o '[0-9.]*')
    STATUS=$(echo $STATE_RESPONSE | grep -o '"status":"[A-Z]*"' | grep -o '[A-Z]*')
    echo "  Battery Level: ${BATTERY_LEVEL}%"
    echo "  Status: $STATUS"
else
    echo -e "${RED}✗ FAILED${NC} - Could not retrieve state"
    exit 1
fi
echo ""

# Test 8: Get Optimized Parameters
echo -e "${BLUE}[TEST 8]${NC} Getting AI-Optimized Parameters..."
OPT_RESPONSE=$(curl -s $BASE_URL/simulation/optimize/$ARCH_ID)
if [[ $OPT_RESPONSE == *"scenarioName"* ]]; then
    echo -e "${GREEN}✓ PASSED${NC} - AI optimization parameters generated"
else
    echo -e "${RED}✗ FAILED${NC} - Could not generate optimized parameters"
    exit 1
fi
echo ""

# Summary
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║                                                           ║"
echo "║   ${GREEN}ALL TESTS PASSED!${NC}                                     ║"
echo "║                                                           ║"
echo "║   ✓ Requirement Processing                                ║"
echo "║   ✓ Architecture Generation                               ║"
echo "║   ✓ Digital Twin State Management                         ║"
echo "║   ✓ Simulation & What-If Engine                           ║"
echo "║   ✓ AI Insight Layer                                      ║"
echo "║                                                           ║"
echo "║   Backend is ready for demo! 🚀                           ║"
echo "║                                                           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""
echo "Created Resources:"
echo "  - Requirement ID: $REQ_ID"
echo "  - Architecture ID: $ARCH_ID"
echo "  - Digital Twin ID: $TWIN_ID"
echo "  - Simulation ID: $SIM_ID"
echo "  - AI Insights: $INSIGHT_COUNT"
echo ""
echo "Next Steps:"
echo "  1. View H2 Console: http://localhost:8080/h2-console"
echo "  2. Monitor real-time state: watch -n 1 'curl -s $BASE_URL/twin/state/$ARCH_ID | jq'"
echo "  3. Run more simulations with different parameters"
echo ""
