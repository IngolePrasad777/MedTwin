package com.medtwin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "simulation_runs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationRun {
    
    @Id
    private String id;
    
    @DBRef
    private SystemArchitecture architecture;
    
    private String scenarioName;
    
    // Input Parameters
    private Integer batterySize; // mAh
    private Integer samplingRate; // Hz
    private Integer airflowTarget; // L/min
    private Integer processingPower; // %
    private Integer thermalThreshold; // °C
    private DeviceRequirement.PowerMode powerMode;
    
    // Simulation Results
    private Double batteryLife; // hours
    private Double thermalLoad; // °C
    private Double efficiency; // %
    private Double reliability; // %
    private Double costImpact; // USD
    private Double riskScore; // 0-100
    private Double complianceScore; // 0-100
    
    // Enhanced Metrics
    private Double peakTemperature;
    private Double minimumBatteryLife;
    private Double averageEfficiency;
    private Double stabilityIndex;
    private Double overallRiskScore;
    private String riskLevel;
    private Double designMaturityScore;
    
    // Anomaly Detection Fields
    private Boolean anomalyDetected;
    private String anomalyType;
    private String anomalyDetails;
    private Integer anomalyCount;
    private String anomalySeverity;
    
    // Time Series Data (embedded)
    @Builder.Default
    private List<SimulationDataPoint> dataPoints = new ArrayList<>();
    
    private Integer duration; // hours
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private SimulationStatus status;
    
    public void onCreate() {
        startedAt = LocalDateTime.now();
        if (status == null) {
            status = SimulationStatus.RUNNING;
        }
    }
    
    public enum SimulationStatus {
        RUNNING, COMPLETED, FAILED
    }
}
