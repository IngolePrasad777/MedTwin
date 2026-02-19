package com.medtwin.service;

import com.medtwin.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * System Orchestration Service - Master pipeline for full analysis
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class SystemOrchestrationService {
    
    private static final String ENGINE_VERSION = "1.2.0";
    
    private final SimulationEngineService simulationService;
    private final ConstraintValidationService constraintValidationService;
    private final AIInsightService aiInsightService;
    private final ArchitectureGenerationService architectureService;
    
    /**
     * Master Pipeline - Full system analysis in one call
     */
    public SystemAnalysisResult runFullAnalysis(String architectureId, SimulationEngineService.SimulationParameters params) {
        log.info("Starting full system analysis for architecture ID: {}", architectureId);
        
        // Step 1: Run simulation with anomaly detection
        log.info("Step 1/5: Running simulation...");
        SimulationRun simulation = simulationService.runSimulation(architectureId, params);
        
        // Step 2: Validate constraints
        log.info("Step 2/5: Validating constraints...");
        ConstraintValidationService.ValidationResult validation = 
                constraintValidationService.validateSimulation(simulation);
        
        // Step 3: Generate AI insights
        log.info("Step 3/5: Generating AI insights...");
        List<AIInsight> insights = aiInsightService.generateInsightsForSimulation(simulation.getId());
        
        // Step 4: Run optimization
        log.info("Step 4/5: Running iterative optimization...");
        OptimizationResult optimization = simulationService.generateOptimizedParametersWithComparison(architectureId);
        
        // Step 5: Calculate final metrics
        log.info("Step 5/5: Calculating final metrics...");
        
        String constraintStatus = determineConstraintStatus(validation);
        String anomalySeverity = simulation.getAnomalySeverity() != null ? 
                simulation.getAnomalySeverity() : "NONE";
        
        SystemAnalysisResult result = SystemAnalysisResult.builder()
                .engineVersion(ENGINE_VERSION)
                .designMaturityScore(optimization.getOptimizedDesignMaturityScore())
                .riskLevel(simulation.getRiskLevel())
                .constraintStatus(constraintStatus)
                .anomalySeverity(anomalySeverity)
                .optimized(true)
                .riskReduction(optimization.getRiskReduction())
                .batteryImprovement(optimization.getBatteryLifeImprovement())
                .iterations(extractIterationCount(optimization.getStrategyApplied()))
                .simulation(simulation)
                .optimization(optimization)
                .insights(insights)
                .timestamp(LocalDateTime.now().toString())
                .build();
        
        log.info("Full system analysis complete: Maturity={}, Risk={}, Anomalies={}", 
                result.getDesignMaturityScore(), result.getRiskLevel(), result.getAnomalySeverity());
        
        return result;
    }
    
    /**
     * Executive Summary - Dashboard-ready overview
     */
    public ExecutiveSummary getExecutiveSummary(String architectureId) {
        log.info("Generating executive summary for architecture ID: {}", architectureId);
        
        SystemArchitecture architecture = architectureService.getArchitecture(architectureId);
        
        // Get latest simulation
        List<SimulationRun> simulations = simulationService.getSimulationsByArchitecture(architectureId);
        SimulationRun latestSimulation = simulations.isEmpty() ? null : simulations.get(0);
        
        // Get insights
        List<AIInsight> insights = latestSimulation != null ? 
                aiInsightService.getInsightsForSimulation(latestSimulation.getId()) : new ArrayList<>();
        
        // Get validation
        ConstraintValidationService.ValidationResult validation = latestSimulation != null ?
                constraintValidationService.validateSimulation(latestSimulation) : null;
        
        // Extract top risks and recommendations
        List<String> topRisks = insights.stream()
                .filter(i -> "CRITICAL".equals(i.getSeverity()) || "HIGH".equals(i.getSeverity()))
                .map(AIInsight::getTitle)
                .limit(3)
                .collect(Collectors.toList());
        
        List<String> topRecommendations = insights.stream()
                .flatMap(i -> i.getRecommendations() != null ? i.getRecommendations().stream() : java.util.stream.Stream.empty())
                .filter(r -> r != null && !r.isEmpty())
                .limit(3)
                .collect(Collectors.toList());
        
        // Count critical insights
        long criticalCount = insights.stream()
                .filter(i -> "CRITICAL".equals(i.getSeverity()))
                .count();
        
        // Determine if optimization is recommended
        boolean optimizationRecommended = latestSimulation != null && 
                (latestSimulation.getRiskScore() > 40 || latestSimulation.getBatteryLife() < 8);
        
        ExecutiveSummary summary = ExecutiveSummary.builder()
                .engineVersion(ENGINE_VERSION)
                .designMaturityScore(latestSimulation != null ? latestSimulation.getDesignMaturityScore() : 0.0)
                .riskLevel(latestSimulation != null ? latestSimulation.getRiskLevel() : "UNKNOWN")
                .complianceStatus(validation != null ? determineConstraintStatus(validation) : "UNKNOWN")
                .activeAnomalies(latestSimulation != null && latestSimulation.getAnomalyDetected() ? 
                        latestSimulation.getAnomalyCount() : 0)
                .optimizationRecommended(optimizationRecommended)
                .topRisks(topRisks.isEmpty() ? List.of("No critical risks identified") : topRisks)
                .topRecommendations(topRecommendations.isEmpty() ? 
                        List.of("System operating within acceptable parameters") : topRecommendations)
                .timestamp(LocalDateTime.now().toString())
                .batteryLife(latestSimulation != null ? latestSimulation.getBatteryLife() : 0.0)
                .thermalLoad(latestSimulation != null ? latestSimulation.getThermalLoad() : 0.0)
                .efficiency(latestSimulation != null ? latestSimulation.getEfficiency() : 0.0)
                .totalSimulations(simulations.size())
                .criticalInsights((int) criticalCount)
                .build();
        
        log.info("Executive summary generated: Maturity={}, Risk={}, Anomalies={}", 
                summary.getDesignMaturityScore(), summary.getRiskLevel(), summary.getActiveAnomalies());
        
        return summary;
    }
    
    private String determineConstraintStatus(ConstraintValidationService.ValidationResult validation) {
        if (validation.isPassed()) {
            return "PASS";
        }
        
        long criticalCount = validation.getViolations().stream()
                .filter(v -> "CRITICAL".equals(v.getSeverity()))
                .count();
        
        return criticalCount > 0 ? "FAILED" : "PASS_WITH_WARNINGS";
    }
    
    private Integer extractIterationCount(String strategyApplied) {
        if (strategyApplied == null) return 0;
        
        // Extract number from "converged in X iterations"
        String[] parts = strategyApplied.split("converged in ");
        if (parts.length > 1) {
            String numPart = parts[1].split(" ")[0];
            try {
                return Integer.parseInt(numPart);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }
}
