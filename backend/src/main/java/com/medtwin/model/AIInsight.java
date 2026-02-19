package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ai_insights")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIInsight {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "simulation_run_id")
    private SimulationRun simulationRun;
    
    @ManyToOne
    @JoinColumn(name = "architecture_id")
    private SystemArchitecture architecture;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightType type;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InsightSeverity severity;
    
    @Column(nullable = false, length = 500)
    private String title;
    
    @Column(nullable = false, length = 2000)
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "insight_recommendations", joinColumns = @JoinColumn(name = "insight_id"))
    @Column(name = "recommendation")
    private List<String> recommendations;
    
    @Column(nullable = false)
    private Double confidenceScore; // 0-1
    
    @Column(nullable = false)
    private Double impactScore; // 0-100
    
    @Column(length = 1000)
    private String reasoning; // AI reasoning explanation
    
    @Column(nullable = false)
    private String affectedSubsystem; // Which component/subsystem
    
    @Column(length = 500)
    private String improvementIfApplied; // Projected improvement
    
    // Before/After Metrics
    @Column
    private Double currentValue;
    
    @Column
    private Double projectedValue;
    
    @Column
    private Double improvementDelta;
    
    @Column(nullable = false)
    private LocalDateTime generatedAt;
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
    }
    
    public enum InsightType {
        OPTIMIZATION, RISK_ALERT, COMPLIANCE_WARNING, PERFORMANCE_IMPROVEMENT, COST_REDUCTION
    }
    
    public enum InsightSeverity {
        INFO, WARNING, CRITICAL
    }
}
