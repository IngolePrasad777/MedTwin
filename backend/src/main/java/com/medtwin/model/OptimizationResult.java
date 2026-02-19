package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Optimization Result with Before/After Comparison
 * Priority 2: Make optimization results explainable
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OptimizationResult {
    
    // Original Configuration
    private Map<String, Double> originalParameters;
    private Double originalBatteryLife;
    private Double originalThermalLoad;
    private Double originalRiskScore;
    private Double originalEfficiency;
    private Double originalCost;
    
    // Optimized Configuration
    private Map<String, Double> optimizedParameters;
    private Double optimizedBatteryLife;
    private Double optimizedThermalLoad;
    private Double optimizedRiskScore;
    private Double optimizedEfficiency;
    private Double optimizedCost;
    
    // Improvements (Delta)
    private Double batteryLifeImprovement; // hours
    private Double thermalReduction; // degrees
    private Double riskReduction; // percentage
    private Double efficiencyGain; // percentage
    private Double costSavings; // USD
    
    // Overall Metrics
    private Double improvementPercentage; // 0-100
    private String strategyApplied; // Description of optimization strategy
    private String reasoning; // Why these changes were made
    private Double confidenceScore; // 0-1
    
    // 🔥 REFINEMENT 4: System-Level Composite Score
    private Double originalDesignMaturityScore; // 0-100 headline metric
    private Double optimizedDesignMaturityScore; // 0-100 headline metric
    private Double designMaturityImprovement; // delta
    
    // Recommendations
    private String primaryRecommendation;
    private String secondaryRecommendation;
    private String warningIfAny;
}
