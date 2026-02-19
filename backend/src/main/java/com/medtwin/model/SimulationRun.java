package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "simulation_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRun {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "architecture_id", nullable = false)
    private SystemArchitecture architecture;
    
    @Column(nullable = false)
    private String scenarioName;
    
    // Input Parameters
    @Column(nullable = false)
    private Integer batterySize; // mAh
    
    @Column(nullable = false)
    private Integer samplingRate; // Hz
    
    @Column(nullable = false)
    private Integer airflowTarget; // L/min
    
    @Column(nullable = false)
    private Integer processingPower; // %
    
    @Column(nullable = false)
    private Integer thermalThreshold; // °C
    
    @Enumerated(EnumType.STRING)
    private DeviceRequirement.PowerMode powerMode;
    
    // Simulation Results
    @Column(nullable = false)
    private Double batteryLife; // hours
    
    @Column(nullable = false)
    private Double thermalLoad; // °C
    
    @Column(nullable = false)
    private Double efficiency; // %
    
    @Column(nullable = false)
    private Double reliability; // %
    
    @Column(nullable = false)
    private Double costImpact; // USD
    
    @Column(nullable = false)
    private Double riskScore; // 0-100
    
    @Column(nullable = false)
    private Double complianceScore; // 0-100
    
    // Enhanced Metrics (Priority 3)
    @Column(nullable = false)
    private Double peakTemperature; // Maximum thermal load
    
    @Column(nullable = false)
    private Double minimumBatteryLife; // Worst-case battery
    
    @Column(nullable = false)
    private Double averageEfficiency; // Mean efficiency
    
    @Column(nullable = false)
    private Double stabilityIndex; // System stability score
    
    @Column(nullable = false)
    private Double overallRiskScore; // Comprehensive risk
    
    @Column(nullable = false)
    private String riskLevel; // LOW, MEDIUM, HIGH, CRITICAL
    
    @Column(nullable = false)
    private Double designMaturityScore; // 0-100 overall score
    
    // Anomaly Detection Fields
    private Boolean anomalyDetected;
    
    @Column(length = 1000)
    private String anomalyType; // e.g., "Thermal Spike", "Battery Drain", "Efficiency Drop"
    
    @Column(length = 2000)
    private String anomalyDetails; // Detailed description of anomalies
    
    private Integer anomalyCount; // Number of anomalies detected
    
    @Column(length = 50)
    private String anomalySeverity; // LOW, MEDIUM, HIGH (based on magnitude)
    
    // Time Series Data
    @OneToMany(mappedBy = "simulationRun", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SimulationDataPoint> dataPoints = new ArrayList<>();
    
    @Column(nullable = false)
    private Integer duration; // hours
    
    @Column(nullable = false)
    private LocalDateTime startedAt;
    
    private LocalDateTime completedAt;
    
    @Enumerated(EnumType.STRING)
    private SimulationStatus status;
    
    @PrePersist
    protected void onCreate() {
        startedAt = LocalDateTime.now();
        if (status == null) {
            status = SimulationStatus.RUNNING;
        }
    }
    
    public enum SimulationStatus {
        RUNNING, COMPLETED, FAILED
    }
}
