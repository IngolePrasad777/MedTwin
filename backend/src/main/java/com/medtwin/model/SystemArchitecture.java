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
@Table(name = "system_architectures")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemArchitecture {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "requirement_id", nullable = false)
    private DeviceRequirement requirement;
    
    @Column(nullable = false)
    private String architectureName;
    
    @Column(length = 1000)
    private String description;
    
    @OneToMany(mappedBy = "architecture", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SystemComponent> components = new ArrayList<>();
    
    // Graph modeling: Component dependencies
    @ElementCollection
    @CollectionTable(name = "component_dependencies", joinColumns = @JoinColumn(name = "architecture_id"))
    @MapKeyColumn(name = "component_name")
    @Column(name = "depends_on")
    private java.util.Map<String, String> dependencyGraph = new java.util.HashMap<>();
    
    @Column(nullable = false)
    private Double confidenceScore; // AI confidence in architecture
    
    @Column(nullable = false)
    private Integer complexityScore; // 1-100
    
    @Column(nullable = false)
    private Double estimatedCost; // USD
    
    @Column(nullable = false)
    private Integer reliabilityScore; // 1-100
    
    @Column(nullable = false)
    private LocalDateTime generatedAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Enumerated(EnumType.STRING)
    private ArchitectureStatus status;
    
    @PrePersist
    protected void onCreate() {
        generatedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ArchitectureStatus.GENERATED;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum ArchitectureStatus {
        GENERATED, VALIDATED, DEPLOYED, ARCHIVED
    }
}
