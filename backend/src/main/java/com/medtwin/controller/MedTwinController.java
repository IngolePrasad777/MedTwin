package com.medtwin.controller;

import com.medtwin.model.*;
import com.medtwin.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * Main REST Controller for MedTwin Backend
 * Exposes all 5 core capabilities via REST APIs
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // For prototype - restrict in production
@RequiredArgsConstructor
@Slf4j
public class MedTwinController {
    
    private final RequirementProcessingService requirementService;
    private final ArchitectureGenerationService architectureService;
    private final DigitalTwinStateService twinStateService;
    private final SimulationEngineService simulationService;
    private final AIInsightService aiInsightService;
    private final KnowledgeService knowledgeService;
    private final ConstraintValidationService constraintValidationService;
    private final SystemOrchestrationService systemOrchestrationService;
    
    private static final String ENGINE_VERSION = "1.2.0";
    
    // ==================== Health Check ====================
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "MedTwin Backend",
                "version", ENGINE_VERSION,
                "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
    
    // ==================== System Orchestration (Master Pipeline) ====================
    
    @PostMapping("/system/full-analysis/{architectureId}")
    public ResponseEntity<SystemAnalysisResult> runFullAnalysis(
            @PathVariable Long architectureId,
            @Valid @RequestBody SimulationEngineService.SimulationParameters params) {
        log.info("POST /api/system/full-analysis/{} - Running full system analysis", architectureId);
        SystemAnalysisResult result = systemOrchestrationService.runFullAnalysis(architectureId, params);
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/system/executive-summary/{architectureId}")
    public ResponseEntity<ExecutiveSummary> getExecutiveSummary(@PathVariable Long architectureId) {
        log.info("GET /api/system/executive-summary/{} - Generating executive summary", architectureId);
        ExecutiveSummary summary = systemOrchestrationService.getExecutiveSummary(architectureId);
        return ResponseEntity.ok(summary);
    }
    
    // ==================== Capability 1: Requirements API ====================
    
    @PostMapping("/requirements")
    public ResponseEntity<DeviceRequirement> createRequirement(@Valid @RequestBody DeviceRequirement requirement) {
        log.info("POST /api/requirements - Creating new requirement");
        DeviceRequirement created = requirementService.processRequirement(requirement);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/requirements/{id}")
    public ResponseEntity<DeviceRequirement> getRequirement(@PathVariable Long id) {
        log.info("GET /api/requirements/{} - Fetching requirement", id);
        return ResponseEntity.ok(requirementService.getRequirement(id));
    }
    
    @GetMapping("/requirements")
    public ResponseEntity<List<DeviceRequirement>> getAllRequirements() {
        log.info("GET /api/requirements - Fetching all requirements");
        return ResponseEntity.ok(requirementService.getAllRequirements());
    }
    
    @GetMapping("/requirements/type/{deviceType}")
    public ResponseEntity<List<DeviceRequirement>> getRequirementsByType(@PathVariable String deviceType) {
        log.info("GET /api/requirements/type/{} - Fetching requirements by type", deviceType);
        return ResponseEntity.ok(requirementService.getRequirementsByType(deviceType));
    }
    
    @PutMapping("/requirements/{id}")
    public ResponseEntity<DeviceRequirement> updateRequirement(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequirement requirement) {
        log.info("PUT /api/requirements/{} - Updating requirement", id);
        return ResponseEntity.ok(requirementService.updateRequirement(id, requirement));
    }
    
    // ==================== Capability 2: Architecture API ====================
    
    @PostMapping("/architecture/generate/{requirementId}")
    public ResponseEntity<SystemArchitecture> generateArchitecture(@PathVariable Long requirementId) {
        log.info("POST /api/architecture/generate/{} - Generating architecture", requirementId);
        SystemArchitecture architecture = architectureService.generateArchitecture(requirementId);
        return ResponseEntity.status(HttpStatus.CREATED).body(architecture);
    }
    
    @GetMapping("/architecture/{id}")
    public ResponseEntity<SystemArchitecture> getArchitecture(@PathVariable Long id) {
        log.info("GET /api/architecture/{} - Fetching architecture", id);
        return ResponseEntity.ok(architectureService.getArchitecture(id));
    }
    
    @GetMapping("/architecture/requirement/{requirementId}")
    public ResponseEntity<SystemArchitecture> getArchitectureByRequirement(@PathVariable Long requirementId) {
        log.info("GET /api/architecture/requirement/{} - Fetching architecture by requirement", requirementId);
        return ResponseEntity.ok(architectureService.getArchitectureByRequirement(requirementId));
    }
    
    // ==================== Capability 3: Digital Twin API ====================
    
    @PostMapping("/twin/initialize/{architectureId}")
    public ResponseEntity<DigitalTwinState> initializeTwin(@PathVariable Long architectureId) {
        log.info("POST /api/twin/initialize/{} - Initializing digital twin", architectureId);
        DigitalTwinState state = twinStateService.createInitialState(architectureId);
        return ResponseEntity.status(HttpStatus.CREATED).body(state);
    }
    
    @GetMapping("/twin/state/{architectureId}")
    public ResponseEntity<DigitalTwinState> getCurrentState(@PathVariable Long architectureId) {
        log.info("GET /api/twin/state/{} - Fetching current state", architectureId);
        return ResponseEntity.ok(twinStateService.getCurrentState(architectureId));
    }
    
    @PutMapping("/twin/state/{architectureId}")
    public ResponseEntity<DigitalTwinState> updateState(
            @PathVariable Long architectureId,
            @RequestBody DigitalTwinState state) {
        log.info("PUT /api/twin/state/{} - Updating state", architectureId);
        return ResponseEntity.ok(twinStateService.updateState(architectureId, state));
    }
    
    @GetMapping("/twin/history/{architectureId}")
    public ResponseEntity<List<DigitalTwinState>> getStateHistory(
            @PathVariable Long architectureId,
            @RequestParam(defaultValue = "10") int limit) {
        log.info("GET /api/twin/history/{} - Fetching state history (limit: {})", architectureId, limit);
        return ResponseEntity.ok(twinStateService.getStateHistory(architectureId, limit));
    }
    
    @PostMapping("/twin/deactivate/{architectureId}")
    public ResponseEntity<Void> deactivateTwin(@PathVariable Long architectureId) {
        log.info("POST /api/twin/deactivate/{} - Deactivating twin", architectureId);
        twinStateService.deactivateState(architectureId);
        return ResponseEntity.ok().build();
    }
    
    // ==================== Capability 4: Simulation API ====================
    
    @PostMapping("/simulation/run/{architectureId}")
    public ResponseEntity<SimulationRun> runSimulation(
            @PathVariable Long architectureId,
            @Valid @RequestBody SimulationEngineService.SimulationParameters params) {
        log.info("POST /api/simulation/run/{} - Running simulation: {}", architectureId, params.getScenarioName());
        SimulationRun simulation = simulationService.runSimulation(architectureId, params);
        
        // Auto-generate AI insights
        aiInsightService.generateInsightsForSimulation(simulation.getId());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(simulation);
    }
    
    @GetMapping("/simulation/{id}")
    public ResponseEntity<SimulationRun> getSimulation(@PathVariable Long id) {
        log.info("GET /api/simulation/{} - Fetching simulation", id);
        return ResponseEntity.ok(simulationService.getSimulation(id));
    }
    
    @GetMapping("/simulation/architecture/{architectureId}")
    public ResponseEntity<List<SimulationRun>> getSimulationsByArchitecture(@PathVariable Long architectureId) {
        log.info("GET /api/simulation/architecture/{} - Fetching simulations", architectureId);
        return ResponseEntity.ok(simulationService.getSimulationsByArchitecture(architectureId));
    }
    
    @GetMapping("/simulation/optimize/{architectureId}")
    public ResponseEntity<SimulationEngineService.SimulationParameters> getOptimizedParameters(
            @PathVariable Long architectureId) {
        log.info("GET /api/simulation/optimize/{} - Generating optimized parameters", architectureId);
        return ResponseEntity.ok(simulationService.generateOptimizedParameters(architectureId));
    }
    
    @GetMapping("/simulation/optimize-detailed/{architectureId}")
    public ResponseEntity<OptimizationResult> getDetailedOptimization(@PathVariable Long architectureId) {
        log.info("GET /api/simulation/optimize-detailed/{} - Generating detailed optimization with before/after comparison", architectureId);
        OptimizationResult result = simulationService.generateOptimizedParametersWithComparison(architectureId);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/simulation/compare/{architectureId}")
    public ResponseEntity<ScenarioComparison> compareScenarios(
            @PathVariable Long architectureId,
            @RequestBody Map<String, SimulationEngineService.SimulationParameters> scenarios) {
        log.info("POST /api/simulation/compare/{} - Comparing scenarios", architectureId);
        
        SimulationEngineService.SimulationParameters scenarioA = scenarios.get("scenarioA");
        SimulationEngineService.SimulationParameters scenarioB = scenarios.get("scenarioB");
        
        if (scenarioA == null || scenarioB == null) {
            throw new IllegalArgumentException("Both scenarioA and scenarioB must be provided");
        }
        
        ScenarioComparison comparison = simulationService.compareScenarios(architectureId, scenarioA, scenarioB);
        return ResponseEntity.ok(comparison);
    }
    
    // ==================== Capability 5: AI Insights API ====================
    
    @PostMapping("/insights/simulation/{simulationId}")
    public ResponseEntity<List<AIInsight>> generateSimulationInsights(@PathVariable Long simulationId) {
        log.info("POST /api/insights/simulation/{} - Generating insights", simulationId);
        List<AIInsight> insights = aiInsightService.generateInsightsForSimulation(simulationId);
        return ResponseEntity.status(HttpStatus.CREATED).body(insights);
    }
    
    @PostMapping("/insights/architecture/{architectureId}")
    public ResponseEntity<List<AIInsight>> generateArchitectureInsights(@PathVariable Long architectureId) {
        log.info("POST /api/insights/architecture/{} - Generating insights", architectureId);
        List<AIInsight> insights = aiInsightService.generateInsightsForArchitecture(architectureId);
        return ResponseEntity.status(HttpStatus.CREATED).body(insights);
    }
    
    @GetMapping("/insights/simulation/{simulationId}")
    public ResponseEntity<List<AIInsight>> getSimulationInsights(@PathVariable Long simulationId) {
        log.info("GET /api/insights/simulation/{} - Fetching insights", simulationId);
        return ResponseEntity.ok(aiInsightService.getInsightsForSimulation(simulationId));
    }
    
    @GetMapping("/insights/architecture/{architectureId}")
    public ResponseEntity<List<AIInsight>> getArchitectureInsights(@PathVariable Long architectureId) {
        log.info("GET /api/insights/architecture/{} - Fetching insights", architectureId);
        return ResponseEntity.ok(aiInsightService.getInsightsForArchitecture(architectureId));
    }
    
    @GetMapping("/insights/critical")
    public ResponseEntity<List<AIInsight>> getCriticalInsights() {
        log.info("GET /api/insights/critical - Fetching critical insights");
        return ResponseEntity.ok(aiInsightService.getCriticalInsights());
    }
    
    @GetMapping("/insights/compliance/{simulationId}")
    public ResponseEntity<List<ComplianceCheck>> getComplianceChecks(@PathVariable Long simulationId) {
        log.info("GET /api/insights/compliance/{} - Generating compliance checks", simulationId);
        List<ComplianceCheck> checks = aiInsightService.generateComplianceChecks(simulationId);
        return ResponseEntity.ok(checks);
    }
    
    // ==================== Knowledge Service API (RAG Layer) ====================
    
    @GetMapping("/knowledge/compliance/{deviceType}")
    public ResponseEntity<List<KnowledgeService.KnowledgeItem>> getComplianceClauses(@PathVariable String deviceType) {
        log.info("GET /api/knowledge/compliance/{} - Retrieving compliance clauses", deviceType);
        List<KnowledgeService.KnowledgeItem> clauses = knowledgeService.getRelevantComplianceClauses(deviceType);
        return ResponseEntity.ok(clauses);
    }
    
    @GetMapping("/knowledge/patterns/{deviceType}")
    public ResponseEntity<List<KnowledgeService.KnowledgeItem>> getDesignPatterns(@PathVariable String deviceType) {
        log.info("GET /api/knowledge/patterns/{} - Retrieving design patterns", deviceType);
        List<KnowledgeService.KnowledgeItem> patterns = knowledgeService.retrieveDesignPatterns(deviceType);
        return ResponseEntity.ok(patterns);
    }
    
    @GetMapping("/knowledge/recommendations/{deviceType}")
    public ResponseEntity<List<String>> getContextualRecommendations(@PathVariable String deviceType) {
        log.info("GET /api/knowledge/recommendations/{} - Retrieving contextual recommendations", deviceType);
        List<String> recommendations = knowledgeService.getContextualRecommendations(deviceType);
        return ResponseEntity.ok(recommendations);
    }
    
    // ==================== Constraint Validation API ====================
    
    @PostMapping("/validation/architecture/{id}")
    public ResponseEntity<ConstraintValidationService.ValidationResult> validateArchitecture(@PathVariable Long id) {
        log.info("POST /api/validation/architecture/{} - Validating architecture", id);
        SystemArchitecture architecture = architectureService.getArchitecture(id);
        ConstraintValidationService.ValidationResult result = constraintValidationService.validateArchitecture(architecture);
        return ResponseEntity.ok(result);
    }
    
    @PostMapping("/validation/simulation/{id}")
    public ResponseEntity<ConstraintValidationService.ValidationResult> validateSimulation(@PathVariable Long id) {
        log.info("POST /api/validation/simulation/{} - Validating simulation", id);
        SimulationRun simulation = simulationService.getSimulation(id);
        ConstraintValidationService.ValidationResult result = constraintValidationService.validateSimulation(simulation);
        return ResponseEntity.ok(result);
    }
    
    // ==================== Async Simulation API ====================
    
    @PostMapping("/simulation/run-async/{architectureId}")
    public ResponseEntity<Map<String, String>> runSimulationAsync(
            @PathVariable Long architectureId,
            @Valid @RequestBody SimulationEngineService.SimulationParameters params) {
        log.info("POST /api/simulation/run-async/{} - Starting async simulation: {}", architectureId, params.getScenarioName());
        
        // Start async simulation (non-blocking)
        simulationService.runSimulationAsync(architectureId, params);
        
        return ResponseEntity.accepted().body(Map.of(
                "status", "ACCEPTED",
                "message", "Simulation started in background. Check /api/simulation/architecture/" + architectureId + " for results.",
                "architectureId", architectureId.toString(),
                "scenarioName", params.getScenarioName(),
                "engineVersion", ENGINE_VERSION
        ));
    }
}
