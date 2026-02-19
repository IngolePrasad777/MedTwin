package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// Embedded document (no @Document annotation - embedded in SystemArchitecture)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemComponent {
    
    private String componentName;
    private String componentType; // Controller, Sensor, Actuator, Power, UI
    private String description;
    private String suggestedPart; // e.g., "STM32H7", "TPM 2.0"
    private List<ComponentSpecification> specifications;
    private List<String> interfaces; // SPI, I2C, UART, etc.
    private Double powerConsumption; // Watts
    private Double thermalOutput; // Watts
    private Integer reliabilityScore; // 1-100
    private Double cost; // USD
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentSpecification {
        private String key;
        private String value;
        private String unit;
    }
}
