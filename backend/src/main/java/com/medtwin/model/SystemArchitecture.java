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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Document(collection = "system_architectures")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SystemArchitecture {
    
    @Id
    private String id;
    
    @DBRef
    private DeviceRequirement requirement;
    
    private String architectureName;
    private String description;
    
    // Embed components directly (better performance for tightly coupled data)
    @Builder.Default
    private List<SystemComponent> components = new ArrayList<>();
    
    // Graph modeling: Component dependencies
    @Builder.Default
    private Map<String, String> dependencyGraph = new HashMap<>();
    
    private Double confidenceScore; // AI confidence in architecture
    private Integer complexityScore; // 1-100
    private Double estimatedCost; // USD
    private Integer reliabilityScore; // 1-100
    
    private LocalDateTime generatedAt;
    private LocalDateTime updatedAt;
    private ArchitectureStatus status;
    
    public void onCreate() {
        generatedAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = ArchitectureStatus.GENERATED;
        }
    }
    
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    public enum ArchitectureStatus {
        GENERATED, VALIDATED, DEPLOYED, ARCHIVED
    }
}
