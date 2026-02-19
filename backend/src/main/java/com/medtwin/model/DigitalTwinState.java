package com.medtwin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "digital_twin_states")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalTwinState {
    
    @Id
    private String id;
    
    @DBRef
    private SystemArchitecture architecture;
    
    // Real-time Metrics
    private Double batteryLevel; // %
    private Double thermalLoad; // °C
    private Double airflowRate; // L/min
    private Double pressure; // cmH2O
    private Double systemLoad; // %
    private Double efficiency; // %
    private Integer samplingRate; // Hz
    private Double powerConsumption; // Watts
    
    // Component States
    private Map<String, String> componentStates;
    
    // Health & Status
    private Integer healthScore; // 1-100
    private Integer reliabilityScore; // 1-100
    private OperationalStatus status;
    private LocalDateTime timestamp;
    private Boolean isLive; // Real device vs simulation
    
    public void onCreate() {
        timestamp = LocalDateTime.now();
        if (status == null) {
            status = OperationalStatus.NORMAL;
        }
    }
    
    public enum OperationalStatus {
        NORMAL, WARNING, CRITICAL, OFFLINE
    }
}
