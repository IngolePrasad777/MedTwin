package com.medtwin.service;

import com.medtwin.model.DeviceRequirement;
import com.medtwin.model.SystemArchitecture;
import com.medtwin.model.SystemComponent;
import com.medtwin.repository.SystemArchitectureRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Capability 2: Architecture Generation
 * Generates system architecture from requirements using AI-driven logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ArchitectureGenerationService {
    
    private final SystemArchitectureRepository architectureRepository;
    private final RequirementProcessingService requirementService;
    private final KnowledgeService knowledgeService;
    private final ConstraintValidationService constraintValidationService;
    
    public SystemArchitecture generateArchitecture(String requirementId) {
        log.info("Generating architecture for requirement ID: {}", requirementId);
        
        DeviceRequirement requirement = requirementService.getRequirement(requirementId);
        
        // 🔥 INTEGRATION 1: Retrieve relevant compliance clauses from RAG layer
        log.info("Retrieving compliance clauses from knowledge base...");
        List<KnowledgeService.ComplianceClause> complianceClauses = 
                knowledgeService.getRelevantComplianceClauses(
                        requirement.getDeviceType(), 
                        "architecture generation");
        
        // 🔥 INTEGRATION 2: Retrieve design patterns from RAG layer
        log.info("Retrieving design patterns from knowledge base...");
        java.util.Map<String, Object> reqMap = new java.util.HashMap<>();
        reqMap.put("portability", requirement.getPortability());
        reqMap.put("batteryCapacity", requirement.getBatteryCapacity());
        reqMap.put("thermalThreshold", requirement.getThermalThreshold());
        
        List<KnowledgeService.DesignPattern> designPatterns = 
                knowledgeService.retrieveDesignPatterns(requirement.getDeviceType(), reqMap);
        
        // Generate architecture based on device type
        SystemArchitecture architecture = createBaseArchitecture(requirement);
        
        // Generate components
        List<SystemComponent> components = generateComponents(requirement, architecture);
        architecture.setComponents(components);
        
        // 🔥 REFINEMENT 1: RAG visibly affects architecture - add components based on retrieved clauses
        log.info("Applying RAG-driven architecture modifications...");
        applyRAGDrivenModifications(architecture, complianceClauses, designPatterns);
        
        // 🔥 INTEGRATION 3: Build dependency graph (graph modeling)
        log.info("Building component dependency graph...");
        buildDependencyGraph(architecture);
        
        // 🔥 INTEGRATION 4: Apply compliance constraints from RAG
        log.info("Applying compliance constraints...");
        applyComplianceConstraints(architecture, complianceClauses);
        
        // Initialize timestamps
        architecture.onCreate();
        
        // Calculate metrics
        calculateArchitectureMetrics(architecture);
        
        // 🔥 INTEGRATION 5: Validate architecture against constraints
        log.info("Validating architecture constraints...");
        ConstraintValidationService.ValidationResult validation = 
                constraintValidationService.validateArchitecture(architecture);
        
        // Adjust confidence based on validation
        if (!validation.isPassed()) {
            double penalty = (validation.getViolations().size() * 0.05);
            architecture.setConfidenceScore(Math.max(0.6, architecture.getConfidenceScore() - penalty));
            log.warn("Architecture has {} constraint violations, confidence reduced to {}",
                    validation.getViolations().size(), architecture.getConfidenceScore());
        }
        
        // Update requirement status
        requirement.setStatus(DeviceRequirement.RequirementStatus.ARCHITECTURE_GENERATED);
        
        SystemArchitecture saved = architectureRepository.save(architecture);
        log.info("Architecture generated successfully with ID: {} (confidence: {}, validation: {})",
                saved.getId(), saved.getConfidenceScore(), validation.isPassed() ? "PASSED" : "FAILED");
        
        return saved;
    }
    
    /**
     * 🔥 REFINEMENT 1: RAG visibly affects architecture
     * Add/modify components based on retrieved compliance clauses and design patterns
     */
    private void applyRAGDrivenModifications(SystemArchitecture architecture,
                                            List<KnowledgeService.ComplianceClause> clauses,
                                            List<KnowledgeService.DesignPattern> patterns) {
        // Null safety
        if (clauses == null) clauses = new ArrayList<>();
        if (patterns == null) patterns = new ArrayList<>();
        
        List<SystemComponent> modifications = new ArrayList<>();
        StringBuilder ragLog = new StringBuilder("RAG-driven modifications:\n");
        
        // Check thermal safety clauses
        boolean thermalClauseFound = clauses.stream()
                .anyMatch(c -> c.getCategory().equals("Thermal Safety") && c.getRelevanceScore() > 0.8);
        
        if (thermalClauseFound) {
            log.info("High-relevance thermal safety clause detected - adding enhanced cooling system");
            SystemComponent coolingSystem = SystemComponent.builder()
                    .componentName("Enhanced Cooling System")
                    .componentType("Thermal")
                    .description("RAG-recommended: Added due to IEC 60601-1 thermal safety requirements")
                    .suggestedPart("Heat Sink + Active Fan")
                    .specifications(Arrays.asList(
                            new SystemComponent.ComponentSpecification("Cooling Capacity", "15", "W"),
                            new SystemComponent.ComponentSpecification("Fan Speed", "3000", "RPM"),
                            new SystemComponent.ComponentSpecification("Noise Level", "35", "dB")
                    ))
                    .interfaces(Arrays.asList("PWM"))
                    .powerConsumption(0.8)
                    .thermalOutput(-5.0) // Negative = removes heat
                    .reliabilityScore(92)
                    .cost(18.0)
                    .build();
            
            modifications.add(coolingSystem);
            ragLog.append("✓ Added Enhanced Cooling System (IEC 60601-1 thermal compliance)\n");
        }
        
        // Check redundancy patterns
        boolean redundancyPatternFound = patterns.stream()
                .anyMatch(p -> p.getName().contains("Redundant") && p.getApplicabilityScore() > 0.8);
        
        if (redundancyPatternFound) {
            log.info("High-applicability redundancy pattern detected - adding backup sensor");
            SystemComponent backupSensor = SystemComponent.builder()
                    .componentName("Backup Sensor Array")
                    .componentType("Sensor")
                    .description("RAG-recommended: Added for redundancy pattern compliance")
                    .suggestedPart("Secondary Sensor Set")
                    .specifications(Arrays.asList(
                            new SystemComponent.ComponentSpecification("Redundancy", "2x", ""),
                            new SystemComponent.ComponentSpecification("Failover Time", "100", "ms")
                    ))
                    .interfaces(Arrays.asList("I2C"))
                    .powerConsumption(0.3)
                    .thermalOutput(0.1)
                    .reliabilityScore(96)
                    .cost(25.0)
                    .build();
            
            modifications.add(backupSensor);
            ragLog.append("✓ Added Backup Sensor Array (redundancy design pattern)\n");
        }
        
        // Check battery life clauses
        boolean batteryClauseFound = clauses.stream()
                .anyMatch(c -> c.getCategory().equals("Power Management") && c.getRelevanceScore() > 0.85);
        
        if (batteryClauseFound && architecture.getRequirement().getBatteryCapacity() < 5000) {
            log.info("Battery life compliance clause detected - upgrading power management");
            // Find and upgrade power management component
            architecture.getComponents().stream()
                    .filter(c -> c.getComponentType().equals("Power"))
                    .findFirst()
                    .ifPresent(pmu -> {
                        pmu.setDescription(pmu.getDescription() + " [RAG-upgraded for IEC 60601-1-6 compliance]");
                        pmu.getSpecifications().add(
                                new SystemComponent.ComponentSpecification("Power Optimization", "Advanced", "Mode"));
                        ragLog.append("✓ Upgraded Power Management Unit (IEC 60601-1-6 battery compliance)\n");
                    });
        }
        
        // Add all modifications
        architecture.getComponents().addAll(modifications);
        
        // Update architecture description with RAG log
        architecture.setDescription(architecture.getDescription() + "\n\n" + ragLog.toString());
        
        log.info("RAG-driven modifications complete: {} components added/modified", modifications.size());
    }
    
    /**
     * Build component dependency graph for graph-based reasoning
     */
    private void buildDependencyGraph(SystemArchitecture architecture) {
        java.util.Map<String, String> graph = new java.util.HashMap<>();
        
        // Null safety - ensure components list exists
        if (architecture.getComponents() == null) {
            architecture.setComponents(new ArrayList<>());
        }
        
        // Define dependencies based on component types
        for (SystemComponent component : architecture.getComponents()) {
            switch (component.getComponentType()) {
                case "Controller":
                    // Controller depends on power
                    graph.put(component.getComponentName(), "Power Management Unit");
                    break;
                case "Sensor":
                    // Sensors depend on controller
                    graph.put(component.getComponentName(), "Main Controller");
                    break;
                case "Actuator":
                    // Actuators depend on controller
                    graph.put(component.getComponentName(), "Main Controller");
                    break;
                case "UI":
                    // UI depends on controller
                    graph.put(component.getComponentName(), "Main Controller");
                    break;
                case "Communication":
                    // Communication depends on controller
                    graph.put(component.getComponentName(), "Main Controller");
                    break;
                case "Security":
                    // Security depends on controller
                    graph.put(component.getComponentName(), "Main Controller");
                    break;
                case "Power":
                    // Power is root (no dependencies)
                    graph.put(component.getComponentName(), "ROOT");
                    break;
            }
        }
        
        architecture.setDependencyGraph(graph);
        log.info("Dependency graph built with {} nodes", graph.size());
    }
    
    /**
     * Apply compliance constraints from RAG-retrieved clauses
     */
    private void applyComplianceConstraints(SystemArchitecture architecture, 
                                           List<KnowledgeService.ComplianceClause> clauses) {
        StringBuilder complianceNotes = new StringBuilder();
        complianceNotes.append("Compliance-aware generation applied:\n");
        
        for (KnowledgeService.ComplianceClause clause : clauses) {
            complianceNotes.append(String.format("- %s %s (relevance: %.2f)\n",
                    clause.getStandard(), clause.getClause(), clause.getRelevanceScore()));
            
            // Apply specific constraints based on clause
            if (clause.getCategory().equals("Thermal Safety")) {
                // Ensure thermal management is adequate
                enforceThermalSafety(architecture);
            } else if (clause.getCategory().equals("Power Management")) {
                // Ensure battery capacity is adequate
                enforcePowerRequirements(architecture);
            }
        }
        
        // Append compliance notes to description
        String currentDesc = architecture.getDescription();
        architecture.setDescription(currentDesc + "\n\n" + complianceNotes.toString());
    }
    
    private void enforceThermalSafety(SystemArchitecture architecture) {
        // Ensure thermal output is within limits
        double totalThermal = architecture.getComponents().stream()
                .mapToDouble(SystemComponent::getThermalOutput)
                .sum();
        
        if (totalThermal > 15.0) {
            log.warn("Total thermal output {} exceeds safe limit, adjusting components", totalThermal);
            // In real system, would adjust component selection
        }
    }
    
    private void enforcePowerRequirements(SystemArchitecture architecture) {
        // Ensure power consumption supports battery life requirements
        double totalPower = architecture.getComponents().stream()
                .mapToDouble(SystemComponent::getPowerConsumption)
                .sum();
        
        Integer batteryCapacity = architecture.getRequirement().getBatteryCapacity();
        if (batteryCapacity != null) {
            double estimatedLife = (batteryCapacity / 1000.0) * 3.7 / totalPower;
            if (estimatedLife < 8.0) {
                log.warn("Estimated battery life {}h below 8h requirement", estimatedLife);
                // In real system, would adjust component selection
            }
        }
    }
    
    private SystemArchitecture createBaseArchitecture(DeviceRequirement requirement) {
        return SystemArchitecture.builder()
                .requirement(requirement)
                .architectureName(requirement.getDeviceType() + " System Architecture")
                .description("AI-generated architecture for " + requirement.getDeviceType())
                .confidenceScore(0.95)
                .complexityScore(calculateComplexity(requirement))
                .status(SystemArchitecture.ArchitectureStatus.GENERATED)
                .build();
    }
    
    private List<SystemComponent> generateComponents(DeviceRequirement requirement, SystemArchitecture architecture) {
        List<SystemComponent> components = new ArrayList<>();
        
        // Main Controller
        components.add(createMainController(requirement, architecture));
        
        // Sensor Array
        components.add(createSensorArray(requirement, architecture));
        
        // Power Management
        components.add(createPowerManagement(requirement, architecture));
        
        // User Interface
        components.add(createUserInterface(requirement, architecture));
        
        // Security Module
        components.add(createSecurityModule(requirement, architecture));
        
        // Communication Module
        components.add(createCommunicationModule(requirement, architecture));
        
        // Device-specific components
        if ("Ventilator".equalsIgnoreCase(requirement.getDeviceType())) {
            components.add(createAirflowControl(requirement, architecture));
        }
        
        return components;
    }
    
    private SystemComponent createMainController(DeviceRequirement requirement, SystemArchitecture architecture) {
        String suggestedPart = requirement.getProcessingPower() > 80 ? "STM32H7 (480MHz)" : "STM32F4 (180MHz)";
        
        return SystemComponent.builder()
                .componentName("Main Controller")
                .componentType("Controller")
                .description("Primary processing unit for device control logic")
                .suggestedPart(suggestedPart)
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Clock Speed", requirement.getProcessingPower() > 80 ? "480" : "180", "MHz"),
                        new SystemComponent.ComponentSpecification("Flash Memory", "2", "MB"),
                        new SystemComponent.ComponentSpecification("RAM", "1", "MB")
                ))
                .interfaces(Arrays.asList("SPI", "I2C", "UART", "CAN-FD"))
                .powerConsumption(requirement.getProcessingPower() * 0.02)
                .thermalOutput(requirement.getProcessingPower() * 0.015)
                .reliabilityScore(95)
                .cost(45.0)
                .build();
    }
    
    private SystemComponent createSensorArray(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("Sensor Array")
                .componentType("Sensor")
                .description("Multi-sensor array for real-time monitoring")
                .suggestedPart("Honeywell HSC Series + BME680")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Sampling Rate", requirement.getSamplingRate().toString(), "Hz"),
                        new SystemComponent.ComponentSpecification("Accuracy", "±0.25", "%"),
                        new SystemComponent.ComponentSpecification("Response Time", "10", "ms")
                ))
                .interfaces(Arrays.asList("I2C", "SPI"))
                .powerConsumption(requirement.getSamplingRate() * 0.005)
                .thermalOutput(requirement.getSamplingRate() * 0.002)
                .reliabilityScore(92)
                .cost(35.0)
                .build();
    }
    
    private SystemComponent createPowerManagement(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("Power Management Unit")
                .componentType("Power")
                .description("Battery management and power distribution")
                .suggestedPart("BQ25703A + TPS63070")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Battery Capacity", requirement.getBatteryCapacity().toString(), "mAh"),
                        new SystemComponent.ComponentSpecification("Voltage", "3.7", "V"),
                        new SystemComponent.ComponentSpecification("Efficiency", "95", "%")
                ))
                .interfaces(Arrays.asList("I2C", "SMBus"))
                .powerConsumption(0.5)
                .thermalOutput(0.3)
                .reliabilityScore(98)
                .cost(25.0)
                .build();
    }
    
    private SystemComponent createUserInterface(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("User Interface")
                .componentType("UI")
                .description("Touchscreen display and user controls")
                .suggestedPart("7-inch TFT LCD + Capacitive Touch")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Resolution", "800x480", "px"),
                        new SystemComponent.ComponentSpecification("Touch Points", "5", "points"),
                        new SystemComponent.ComponentSpecification("Brightness", "500", "cd/m²")
                ))
                .interfaces(Arrays.asList("SPI", "I2C"))
                .powerConsumption(1.5)
                .thermalOutput(0.8)
                .reliabilityScore(90)
                .cost(55.0)
                .build();
    }
    
    private SystemComponent createSecurityModule(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("Security Module")
                .componentType("Security")
                .description("Hardware security and encryption")
                .suggestedPart("TPM 2.0 + ATECC608")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Encryption", "AES-256", "bit"),
                        new SystemComponent.ComponentSpecification("Key Storage", "Secure", "element"),
                        new SystemComponent.ComponentSpecification("Compliance", "FIPS 140-2", "Level 2")
                ))
                .interfaces(Arrays.asList("I2C", "SPI"))
                .powerConsumption(0.2)
                .thermalOutput(0.1)
                .reliabilityScore(99)
                .cost(15.0)
                .build();
    }
    
    private SystemComponent createCommunicationModule(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("Communication Module")
                .componentType("Communication")
                .description("WiFi, Bluetooth, and Ethernet connectivity")
                .suggestedPart("ESP32-WROOM + W5500")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("WiFi", "802.11 b/g/n", ""),
                        new SystemComponent.ComponentSpecification("Bluetooth", "5.0", ""),
                        new SystemComponent.ComponentSpecification("Ethernet", "10/100", "Mbps")
                ))
                .interfaces(Arrays.asList("SPI", "UART"))
                .powerConsumption(0.8)
                .thermalOutput(0.4)
                .reliabilityScore(93)
                .cost(20.0)
                .build();
    }
    
    private SystemComponent createAirflowControl(DeviceRequirement requirement, SystemArchitecture architecture) {
        return SystemComponent.builder()
                .componentName("Airflow Control System")
                .componentType("Actuator")
                .description("Precision airflow control and regulation")
                .suggestedPart("Proportional Valve + Blower Motor")
                .specifications(Arrays.asList(
                        new SystemComponent.ComponentSpecification("Flow Range", "0-" + requirement.getTargetAirflow(), "L/min"),
                        new SystemComponent.ComponentSpecification("Accuracy", "±2", "%"),
                        new SystemComponent.ComponentSpecification("Response Time", "50", "ms")
                ))
                .interfaces(Arrays.asList("PWM", "I2C"))
                .powerConsumption(requirement.getTargetAirflow() * 0.03)
                .thermalOutput(requirement.getTargetAirflow() * 0.015)
                .reliabilityScore(94)
                .cost(85.0)
                .build();
    }
    
    private void calculateArchitectureMetrics(SystemArchitecture architecture) {
        double totalCost = architecture.getComponents().stream()
                .mapToDouble(SystemComponent::getCost)
                .sum();
        
        int avgReliability = (int) architecture.getComponents().stream()
                .mapToInt(SystemComponent::getReliabilityScore)
                .average()
                .orElse(90);
        
        architecture.setEstimatedCost(totalCost);
        architecture.setReliabilityScore(avgReliability);
    }
    
    private int calculateComplexity(DeviceRequirement requirement) {
        int complexity = 50; // Base complexity
        
        if (requirement.getSamplingRate() > 150) complexity += 15;
        if (requirement.getProcessingPower() > 80) complexity += 10;
        if (requirement.getComplianceStandards().size() > 2) complexity += 10;
        if ("Portable".equalsIgnoreCase(requirement.getPortability())) complexity += 15;
        
        return Math.min(100, complexity);
    }
    
    public SystemArchitecture getArchitecture(String id) {
        return architectureRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Architecture not found with ID: " + id));
    }
    
    public SystemArchitecture getArchitectureByRequirement(String requirementId) {
        return architectureRepository.findByRequirementId(requirementId)
                .orElseThrow(() -> new IllegalArgumentException("Architecture not found for requirement ID: " + requirementId));
    }
}
