package com.medtwin.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "device_requirements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequirement {
    
    @Id
    private String id;
    
    private String deviceType; // Ventilator, Infusion Pump, etc.
    private String deviceClass; // IIa, IIb, III
    private String powerSource; // Mains, Battery, Hybrid
    private String portability; // Portable, Fixed, Mobile
    private List<String> complianceStandards;
    private String functionalRequirements;
    
    // Technical Specifications
    private Integer targetAirflow; // L/min
    private Integer samplingRate; // Hz
    private Integer batteryCapacity; // mAh
    private Integer processingPower; // %
    private Integer thermalThreshold; // °C
    private PowerMode powerMode;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RequirementStatus status;
    
    @org.springframework.data.annotation.Transient
    public void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = RequirementStatus.DRAFT;
        }
    }
    
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum PowerMode {
        ECO, BALANCED, PERFORMANCE
    }
    
    public enum RequirementStatus {
        DRAFT, VALIDATED, ARCHITECTURE_GENERATED, ACTIVE
    }
}
