package com.medtwin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "ai_insights")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIInsight {
    
    @Id
    private String id;
    
    @DBRef
    private SimulationRun simulationRun;
    
    @DBRef
    private SystemArchitecture architecture;
    
    private InsightType type;
    private InsightSeverity severity;
    private String title;
    private String description;
    private List<String> recommendations;
    private Double confidenceScore; // 0-1
    private Double impactScore; // 0-100
    private String reasoning; // AI reasoning explanation
    private String affectedSubsystem; // Which component/subsystem
    private String improvementIfApplied; // Projected improvement
    
    // Before/After Metrics
    private Double currentValue;
    private Double projectedValue;
    private Double improvementDelta;
    
    private LocalDateTime generatedAt;
    
    public void onCreate() {
        generatedAt = LocalDateTime.now();
    }
    
    public enum InsightType {
        OPTIMIZATION, RISK_ALERT, COMPLIANCE_WARNING, PERFORMANCE_IMPROVEMENT, COST_REDUCTION
    }
    
    public enum InsightSeverity {
        INFO, WARNING, CRITICAL
    }
}
