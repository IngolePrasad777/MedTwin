package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "device_requirements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequirement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String deviceType; // Ventilator, Infusion Pump, etc.
    
    @Column(nullable = false)
    private String deviceClass; // IIa, IIb, III
    
    private String powerSource; // Mains, Battery, Hybrid
    
    private String portability; // Portable, Fixed, Mobile
    
    @ElementCollection
    @CollectionTable(name = "compliance_standards", joinColumns = @JoinColumn(name = "requirement_id"))
    @Column(name = "standard")
    private List<String> complianceStandards;
    
    @Column(length = 2000)
    private String functionalRequirements;
    
    // Technical Specifications
    private Integer targetAirflow; // L/min
    private Integer samplingRate; // Hz
    private Integer batteryCapacity; // mAh
    private Integer processingPower; // %
    private Integer thermalThreshold; // °C
    
    @Enumerated(EnumType.STRING)
    private PowerMode powerMode;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    private RequirementStatus status;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = RequirementStatus.DRAFT;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum PowerMode {
        ECO, BALANCED, PERFORMANCE
    }
    
    public enum RequirementStatus {
        DRAFT, VALIDATED, ARCHITECTURE_GENERATED, ACTIVE
    }
}
