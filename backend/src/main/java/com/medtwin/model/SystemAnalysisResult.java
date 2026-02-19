package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Master Pipeline Result - Single structured response for full system analysis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemAnalysisResult {
    
    private String engineVersion;
    private Double designMaturityScore; // 0-100 headline metric
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private String constraintStatus; // PASS, PASS_WITH_WARNINGS, FAILED
    private String anomalySeverity; // NONE, LOW, MEDIUM, HIGH
    private Boolean optimized;
    private Double riskReduction;
    private Double batteryImprovement;
    private Integer iterations;
    
    // Detailed results
    private SimulationRun simulation;
    private OptimizationResult optimization;
    private java.util.List<AIInsight> insights;
    private String timestamp;
}
