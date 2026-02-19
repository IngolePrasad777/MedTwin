package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "simulation_data_points")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationDataPoint {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "simulation_run_id", nullable = false)
    private SimulationRun simulationRun;
    
    @Column(nullable = false)
    private Integer timeStep; // hour
    
    @Column(nullable = false)
    private Double batteryLevel; // %
    
    @Column(nullable = false)
    private Double thermalLoad; // °C
    
    @Column(nullable = false)
    private Double efficiency; // %
    
    @Column(nullable = false)
    private Double powerConsumption; // Watts
    
    @Column(nullable = false)
    private Double riskScore; // 0-100
}
