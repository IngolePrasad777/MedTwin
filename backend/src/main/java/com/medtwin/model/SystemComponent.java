package com.medtwin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "system_components")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemComponent {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "architecture_id", nullable = false)
    private SystemArchitecture architecture;
    
    @Column(nullable = false)
    private String componentName;
    
    @Column(nullable = false)
    private String componentType; // Controller, Sensor, Actuator, Power, UI
    
    @Column(length = 1000)
    private String description;
    
    @Column(length = 500)
    private String suggestedPart; // e.g., "STM32H7", "TPM 2.0"
    
    @ElementCollection
    @CollectionTable(name = "component_specifications", joinColumns = @JoinColumn(name = "component_id"))
    private List<ComponentSpecification> specifications;
    
    @ElementCollection
    @CollectionTable(name = "component_interfaces", joinColumns = @JoinColumn(name = "component_id"))
    @Column(name = "interface_type")
    private List<String> interfaces; // SPI, I2C, UART, etc.
    
    @Column(nullable = false)
    private Double powerConsumption; // Watts
    
    @Column(nullable = false)
    private Double thermalOutput; // Watts
    
    @Column(nullable = false)
    private Integer reliabilityScore; // 1-100
    
    @Column(nullable = false)
    private Double cost; // USD
    
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ComponentSpecification {
        private String key;
        private String value;
        private String unit;
    }
}
