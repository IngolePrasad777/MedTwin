package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "digital_twin_states")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DigitalTwinState {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "architecture_id", nullable = false)
    private SystemArchitecture architecture;
    
    // Real-time Metrics
    @Column(nullable = false)
    private Double batteryLevel; // %
    
    @Column(nullable = false)
    private Double thermalLoad; // °C
    
    @Column(nullable = false)
    private Double airflowRate; // L/min
    
    @Column(nullable = false)
    private Double pressure; // cmH2O
    
    @Column(nullable = false)
    private Double systemLoad; // %
    
    @Column(nullable = false)
    private Double efficiency; // %
    
    @Column(nullable = false)
    private Integer samplingRate; // Hz
    
    @Column(nullable = false)
    private Double powerConsumption; // Watts
    
    // Component States
    @ElementCollection
    @CollectionTable(name = "component_states", joinColumns = @JoinColumn(name = "twin_state_id"))
    @MapKeyColumn(name = "component_name")
    @Column(name = "state_value")
    private Map<String, String> componentStates;
    
    // Health & Status
    @Column(nullable = false)
    private Integer healthScore; // 1-100
    
    @Column(nullable = false)
    private Integer reliabilityScore; // 1-100
    
    @Enumerated(EnumType.STRING)
    private OperationalStatus status;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    private Boolean isLive; // Real device vs simulation
    
    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
        if (status == null) {
            status = OperationalStatus.NORMAL;
        }
    }
    
    public enum OperationalStatus {
        NORMAL, WARNING, CRITICAL, OFFLINE
    }
}
