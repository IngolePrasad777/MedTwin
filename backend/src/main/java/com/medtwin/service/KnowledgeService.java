package com.medtwin.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Knowledge Retrieval Service (RAG Layer Simulation)
 * Simulates vector DB + embedding retrieval for compliance and design patterns
 */
@Service
@Slf4j
public class KnowledgeService {
    
    private final Map<String, List<ComplianceClause>> isoKnowledgeBase;
    private final Map<String, List<DesignPattern>> designPatternBase;
    
    public KnowledgeService() {
        this.isoKnowledgeBase = loadPredefinedISOClauses();
        this.designPatternBase = loadDesignPatterns();
        log.info("Knowledge base initialized with {} ISO standards and {} design patterns",
                isoKnowledgeBase.size(), designPatternBase.size());
    }
    
    /**
     * Retrieve relevant compliance clauses for device type
     * Simulates RAG retrieval with relevance scoring
     */
    public List<ComplianceClause> getRelevantComplianceClauses(String deviceType, String context) {
        log.debug("Retrieving compliance clauses for device: {}, context: {}", deviceType, context);
        
        List<ComplianceClause> allClauses = isoKnowledgeBase.getOrDefault(deviceType, new ArrayList<>());
        List<ComplianceClause> relevant = new ArrayList<>();
        
        // Simulate embedding similarity scoring
        for (ComplianceClause clause : allClauses) {
            double relevanceScore = calculateRelevanceScore(clause, context);
            if (relevanceScore > 0.6) {
                clause.setRelevanceScore(relevanceScore);
                relevant.add(clause);
            }
        }
        
        // Sort by relevance
        relevant.sort((a, b) -> Double.compare(b.getRelevanceScore(), a.getRelevanceScore()));
        
        log.info("Retrieved {} relevant clauses (from {} total)", relevant.size(), allClauses.size());
        return relevant.subList(0, Math.min(5, relevant.size()));
    }
    
    /**
     * Retrieve design patterns for architecture generation
     */
    public List<DesignPattern> retrieveDesignPatterns(String deviceType, Map<String, Object> requirements) {
        log.debug("Retrieving design patterns for: {}", deviceType);
        
        List<DesignPattern> patterns = designPatternBase.getOrDefault(deviceType, new ArrayList<>());
        List<DesignPattern> applicable = new ArrayList<>();
        
        for (DesignPattern pattern : patterns) {
            double applicabilityScore = calculateApplicability(pattern, requirements);
            if (applicabilityScore > 0.5) {
                pattern.setApplicabilityScore(applicabilityScore);
                applicable.add(pattern);
            }
        }
        
        applicable.sort((a, b) -> Double.compare(b.getApplicabilityScore(), a.getApplicabilityScore()));
        
        log.info("Retrieved {} applicable design patterns", applicable.size());
        return applicable;
    }
    
    /**
     * Get context-aware recommendations
     */
    public List<String> getContextualRecommendations(String deviceType, String scenario) {
        List<String> recommendations = new ArrayList<>();
        
        // Simulate RAG-based recommendation retrieval
        if (scenario.contains("thermal")) {
            recommendations.add("Implement active cooling per ISO 60601-1 Section 11.6.3");
            recommendations.add("Use thermal-aware component placement from IEEE 1680.1");
            recommendations.add("Consider heat pipe technology for portable devices");
        }
        
        if (scenario.contains("battery")) {
            recommendations.add("Follow IEC 60601-1-6 battery safety guidelines");
            recommendations.add("Implement smart battery management per ISO 13485");
            recommendations.add("Use lithium-ion cells with UL 2054 certification");
        }
        
        if (scenario.contains("accuracy")) {
            recommendations.add("Calibration per ISO 80601-2-12 for respiratory devices");
            recommendations.add("Implement redundant sensors for critical measurements");
            recommendations.add("Follow FDA guidance on software validation");
        }
        
        return recommendations;
    }
    
    /**
     * Simulate embedding-based relevance scoring
     */
    private double calculateRelevanceScore(ComplianceClause clause, String context) {
        double score = 0.5; // Base score
        
        String lowerContext = context.toLowerCase();
        String lowerClause = clause.getClause().toLowerCase() + " " + clause.getRequirement().toLowerCase();
        
        // Keyword matching (simulates semantic similarity)
        if (lowerContext.contains("thermal") && lowerClause.contains("thermal")) score += 0.3;
        if (lowerContext.contains("battery") && lowerClause.contains("battery")) score += 0.3;
        if (lowerContext.contains("safety") && lowerClause.contains("safety")) score += 0.2;
        if (lowerContext.contains("portable") && lowerClause.contains("portable")) score += 0.2;
        if (lowerContext.contains("accuracy") && lowerClause.contains("accuracy")) score += 0.25;
        
        // Deterministic scoring (removed randomness for consistency)
        // Add small deterministic variance based on clause text length
        score += (clause.getClause().length() % 10) * 0.01;
        
        return Math.min(1.0, Math.max(0.0, score));
    }
    
    /**
     * Calculate pattern applicability
     */
    private double calculateApplicability(DesignPattern pattern, Map<String, Object> requirements) {
        double score = 0.6; // Base score
        
        // Check requirement matches
        if (requirements.containsKey("portability") && pattern.getName().contains("Portable")) {
            score += 0.2;
        }
        if (requirements.containsKey("batteryCapacity") && pattern.getName().contains("Power")) {
            score += 0.15;
        }
        if (requirements.containsKey("thermalThreshold") && pattern.getName().contains("Thermal")) {
            score += 0.15;
        }
        
        return Math.min(1.0, score);
    }
    
    /**
     * Load predefined ISO clauses (simulates vector DB)
     */
    private Map<String, List<ComplianceClause>> loadPredefinedISOClauses() {
        Map<String, List<ComplianceClause>> kb = new HashMap<>();
        
        // Medical device clauses
        List<ComplianceClause> medicalClauses = Arrays.asList(
                ComplianceClause.builder()
                        .standard("IEC 60601-1")
                        .clause("Section 11.6.3")
                        .requirement("Surface temperature shall not exceed 50°C during normal operation")
                        .category("Thermal Safety")
                        .criticality("HIGH")
                        .build(),
                ComplianceClause.builder()
                        .standard("IEC 60601-1-6")
                        .clause("Section 6.8")
                        .requirement("Battery shall provide minimum 8 hours continuous operation")
                        .category("Power Management")
                        .criticality("MEDIUM")
                        .build(),
                ComplianceClause.builder()
                        .standard("ISO 13485")
                        .clause("Section 7.3.3")
                        .requirement("Device reliability shall exceed 95% over operational lifetime")
                        .category("Quality Management")
                        .criticality("HIGH")
                        .build(),
                ComplianceClause.builder()
                        .standard("ISO 14971")
                        .clause("Section 4.3")
                        .requirement("Risk score shall be below 30% for acceptable risk classification")
                        .category("Risk Management")
                        .criticality("CRITICAL")
                        .build(),
                ComplianceClause.builder()
                        .standard("ISO 80601-2-12")
                        .clause("Section 201.12.1")
                        .requirement("Respiratory devices must maintain ±5% accuracy for flow measurements")
                        .category("Performance")
                        .criticality("HIGH")
                        .build(),
                ComplianceClause.builder()
                        .standard("IEC 62304")
                        .clause("Section 5.1.1")
                        .requirement("Software safety classification must be documented and justified")
                        .category("Software Safety")
                        .criticality("MEDIUM")
                        .build()
        );
        
        kb.put("Portable Spirometer", medicalClauses);
        kb.put("Medical Device", medicalClauses);
        kb.put("Respiratory Device", medicalClauses);
        
        return kb;
    }
    
    /**
     * Load design patterns (simulates knowledge base)
     */
    private Map<String, List<DesignPattern>> loadDesignPatterns() {
        Map<String, List<DesignPattern>> patterns = new HashMap<>();
        
        List<DesignPattern> medicalPatterns = Arrays.asList(
                DesignPattern.builder()
                        .name("Portable Medical Device Architecture")
                        .description("Optimized for battery life and thermal management")
                        .components(Arrays.asList("Low-power MCU", "Smart battery management", "Thermal monitoring"))
                        .benefits(Arrays.asList("Extended battery life", "Reduced heat generation", "Compact form factor"))
                        .constraints(Arrays.asList("Limited processing power", "Size constraints"))
                        .build(),
                DesignPattern.builder()
                        .name("High-Accuracy Sensor Array")
                        .description("Redundant sensors with calibration system")
                        .components(Arrays.asList("Primary sensor", "Backup sensor", "Calibration module"))
                        .benefits(Arrays.asList("99.5%+ accuracy", "Fault tolerance", "Self-calibration"))
                        .constraints(Arrays.asList("Higher cost", "Increased power consumption"))
                        .build(),
                DesignPattern.builder()
                        .name("Thermal-Aware Power Management")
                        .description("Dynamic power scaling based on thermal load")
                        .components(Arrays.asList("Temperature sensors", "Power controller", "Cooling system"))
                        .benefits(Arrays.asList("Prevents overheating", "Extends component life", "Maintains performance"))
                        .constraints(Arrays.asList("Complex control logic", "Additional sensors required"))
                        .build()
        );
        
        patterns.put("Portable Spirometer", medicalPatterns);
        patterns.put("Medical Device", medicalPatterns);
        
        return patterns;
    }
    
    // DTOs
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ComplianceClause {
        private String standard;
        private String clause;
        private String requirement;
        private String category;
        private String criticality;
        private double relevanceScore;
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class DesignPattern {
        private String name;
        private String description;
        private List<String> components;
        private List<String> benefits;
        private List<String> constraints;
        private double applicabilityScore;
    }
}
