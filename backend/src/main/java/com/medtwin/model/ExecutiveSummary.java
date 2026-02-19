package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Executive Summary - Dashboard-ready high-level overview
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecutiveSummary {
    
    private String engineVersion;
    private Double designMaturityScore; // 0-100
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    private String complianceStatus; // PASS, PASS_WITH_WARNINGS, FAILED
    private Integer activeAnomalies;
    private Boolean optimizationRecommended;
    private List<String> topRisks;
    private List<String> topRecommendations;
    private String timestamp;
    
    // Quick metrics
    private Double batteryLife;
    private Double thermalLoad;
    private Double efficiency;
    private Integer totalSimulations;
    private Integer criticalInsights;
}
