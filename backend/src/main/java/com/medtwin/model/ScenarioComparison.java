package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Scenario Comparison Result
 * Priority 6: Advanced scenario analysis
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScenarioComparison {
    
    private String scenarioAName;
    private String scenarioBName;
    
    // Scenario A Metrics
    private Double scenarioA_BatteryLife;
    private Double scenarioA_ThermalLoad;
    private Double scenarioA_RiskScore;
    private Double scenarioA_Efficiency;
    private Double scenarioA_Cost;
    
    // Scenario B Metrics
    private Double scenarioB_BatteryLife;
    private Double scenarioB_ThermalLoad;
    private Double scenarioB_RiskScore;
    private Double scenarioB_Efficiency;
    private Double scenarioB_Cost;
    
    // Comparisons (Positive = B is better)
    private Double batteryLifeDelta;
    private Double thermalLoadDelta;
    private Double riskScoreDelta;
    private Double efficiencyDelta;
    private Double costDelta;
    
    // Winner Analysis
    private String winnerScenario; // A or B
    private String winnerReason;
    private Double overallScoreA;
    private Double overallScoreB;
    
    // Detailed Comparison
    private List<MetricComparison> metricComparisons;
    
    // Recommendation
    private String recommendation;
    private String reasoning;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricComparison {
        private String metricName;
        private Double valueA;
        private Double valueB;
        private Double delta;
        private String winner; // A, B, or TIE
        private String interpretation;
    }
}
