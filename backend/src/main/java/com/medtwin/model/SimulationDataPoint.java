package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// Embedded document (no @Document - embedded in SimulationRun)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationDataPoint {
    
    private Integer timeStep; // hour
    private Double batteryLevel; // %
    private Double thermalLoad; // °C
    private Double efficiency; // %
    private Double powerConsumption; // Watts
    private Double riskScore; // 0-100
}
