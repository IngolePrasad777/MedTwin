package com.medtwin.service;

import com.medtwin.model.DeviceRequirement;
import com.medtwin.model.SimulationRun;
import com.medtwin.model.SystemArchitecture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Constraint Validation Engine
 * Validates designs against safety, performance, and regulatory constraints
 */
@Service
@Slf4j
public class ConstraintValidationService {
    
    /**
     * Validate architecture against all constraints
     */
    public ValidationResult validateArchitecture(SystemArchitecture architecture) {
        log.info("Validating architecture ID: {}", architecture.getId());
        
        List<ConstraintViolation> violations = new ArrayList<>();
        List<ConstraintCheck> checks = new ArrayList<>();
        
        DeviceRequirement req = architecture.getRequirement();
        
        // Thermal constraints
        checks.add(validateThermalConstraint(req));
        
        // Battery constraints
        checks.add(validateBatteryConstraint(req));
        
        // Power constraints
        checks.add(validatePowerConstraint(req));
        
        // Portability constraints
        checks.add(validatePortabilityConstraint(req, architecture));
        
        // Regulatory constraints
        checks.add(validateRegulatoryConstraint(req));
        
        // Collect violations
        for (ConstraintCheck check : checks) {
            if (!check.isPassed()) {
                violations.add(ConstraintViolation.builder()
                        .constraintName(check.getConstraintName())
                        .severity(check.getSeverity())
                        .message(check.getMessage())
                        .recommendation(check.getRecommendation())
                        .build());
            }
        }
        
        boolean allPassed = violations.isEmpty();
        log.info("Validation complete: {} checks, {} violations", checks.size(), violations.size());
        
        return ValidationResult.builder()
                .passed(allPassed)
                .totalChecks(checks.size())
                .passedChecks((int) checks.stream().filter(ConstraintCheck::isPassed).count())
                .violations(violations)
                .checks(checks)
                .overallScore(calculateOverallScore(checks))
                .build();
    }
    
    /**
     * Validate simulation results against constraints
     */
    public ValidationResult validateSimulation(SimulationRun simulation) {
        log.info("Validating simulation ID: {}", simulation.getId());
        
        List<ConstraintViolation> violations = new ArrayList<>();
        List<ConstraintCheck> checks = new ArrayList<>();
        
        // Thermal limit check
        ConstraintCheck thermalCheck = ConstraintCheck.builder()
                .constraintName("Thermal Limit")
                .constraintType("SAFETY")
                .passed(simulation.getThermalLoad() <= simulation.getThermalThreshold())
                .currentValue(simulation.getThermalLoad())
                .requiredValue((double) simulation.getThermalThreshold())
                .unit("°C")
                .severity(simulation.getThermalLoad() > simulation.getThermalThreshold() ? "CRITICAL" : "INFO")
                .message(simulation.getThermalLoad() <= simulation.getThermalThreshold() ?
                        "Thermal load within safe limits" :
                        String.format("Thermal load %.1f°C exceeds threshold %d°C",
                                simulation.getThermalLoad(), simulation.getThermalThreshold()))
                .recommendation(simulation.getThermalLoad() > simulation.getThermalThreshold() ?
                        "Reduce processing power or improve cooling" : "No action required")
                .build();
        checks.add(thermalCheck);
        
        // Battery life check
        ConstraintCheck batteryCheck = ConstraintCheck.builder()
                .constraintName("Minimum Battery Life")
                .constraintType("PERFORMANCE")
                .passed(simulation.getBatteryLife() >= 8.0)
                .currentValue(simulation.getBatteryLife())
                .requiredValue(8.0)
                .unit("hours")
                .severity(simulation.getBatteryLife() < 8.0 ? "HIGH" : "INFO")
                .message(simulation.getBatteryLife() >= 8.0 ?
                        "Battery life meets minimum requirement" :
                        String.format("Battery life %.1fh below 8h minimum", simulation.getBatteryLife()))
                .recommendation(simulation.getBatteryLife() < 8.0 ?
                        "Increase battery capacity or reduce power consumption" : "No action required")
                .build();
        checks.add(batteryCheck);
        
        // Risk threshold check
        ConstraintCheck riskCheck = ConstraintCheck.builder()
                .constraintName("Risk Threshold")
                .constraintType("SAFETY")
                .passed(simulation.getRiskScore() < 30.0)
                .currentValue(simulation.getRiskScore())
                .requiredValue(30.0)
                .unit("%")
                .severity(simulation.getRiskScore() >= 60 ? "CRITICAL" :
                        simulation.getRiskScore() >= 30 ? "HIGH" : "INFO")
                .message(simulation.getRiskScore() < 30 ?
                        "Risk score within acceptable range" :
                        String.format("Risk score %.1f%% exceeds acceptable threshold", simulation.getRiskScore()))
                .recommendation(simulation.getRiskScore() >= 30 ?
                        "Implement risk mitigation strategies per ISO 14971" : "No action required")
                .build();
        checks.add(riskCheck);
        
        // Efficiency check
        ConstraintCheck efficiencyCheck = ConstraintCheck.builder()
                .constraintName("Minimum Efficiency")
                .constraintType("PERFORMANCE")
                .passed(simulation.getEfficiency() >= 80.0)
                .currentValue(simulation.getEfficiency())
                .requiredValue(80.0)
                .unit("%")
                .severity(simulation.getEfficiency() < 80 ? "MEDIUM" : "INFO")
                .message(simulation.getEfficiency() >= 80 ?
                        "Efficiency meets performance target" :
                        String.format("Efficiency %.1f%% below 80%% target", simulation.getEfficiency()))
                .recommendation(simulation.getEfficiency() < 80 ?
                        "Optimize power management and thermal control" : "No action required")
                .build();
        checks.add(efficiencyCheck);
        
        // Reliability check
        ConstraintCheck reliabilityCheck = ConstraintCheck.builder()
                .constraintName("Reliability Target")
                .constraintType("QUALITY")
                .passed(simulation.getReliability() >= 95.0)
                .currentValue(simulation.getReliability())
                .requiredValue(95.0)
                .unit("%")
                .severity(simulation.getReliability() < 95 ? "HIGH" : "INFO")
                .message(simulation.getReliability() >= 95 ?
                        "Reliability meets ISO 13485 requirements" :
                        String.format("Reliability %.1f%% below 95%% requirement", simulation.getReliability()))
                .recommendation(simulation.getReliability() < 95 ?
                        "Add redundancy and error handling mechanisms" : "No action required")
                .build();
        checks.add(reliabilityCheck);
        
        // Collect violations
        for (ConstraintCheck check : checks) {
            if (!check.isPassed()) {
                violations.add(ConstraintViolation.builder()
                        .constraintName(check.getConstraintName())
                        .severity(check.getSeverity())
                        .message(check.getMessage())
                        .recommendation(check.getRecommendation())
                        .build());
            }
        }
        
        boolean allPassed = violations.isEmpty();
        
        return ValidationResult.builder()
                .passed(allPassed)
                .totalChecks(checks.size())
                .passedChecks((int) checks.stream().filter(ConstraintCheck::isPassed).count())
                .violations(violations)
                .checks(checks)
                .overallScore(calculateOverallScore(checks))
                .build();
    }
    
    private ConstraintCheck validateThermalConstraint(DeviceRequirement req) {
        boolean passed = req.getThermalThreshold() != null && req.getThermalThreshold() <= 50;
        return ConstraintCheck.builder()
                .constraintName("Thermal Threshold")
                .constraintType("SAFETY")
                .passed(passed)
                .currentValue(req.getThermalThreshold() != null ? req.getThermalThreshold().doubleValue() : 0)
                .requiredValue(50.0)
                .unit("°C")
                .severity(passed ? "INFO" : "HIGH")
                .message(passed ? "Thermal threshold within IEC 60601-1 limits" :
                        "Thermal threshold exceeds 50°C safety limit")
                .recommendation(passed ? "No action required" :
                        "Reduce thermal threshold to meet IEC 60601-1 Section 11.6.3")
                .build();
    }
    
    private ConstraintCheck validateBatteryConstraint(DeviceRequirement req) {
        boolean passed = req.getBatteryCapacity() != null && req.getBatteryCapacity() >= 4000;
        return ConstraintCheck.builder()
                .constraintName("Battery Capacity")
                .constraintType("PERFORMANCE")
                .passed(passed)
                .currentValue(req.getBatteryCapacity() != null ? req.getBatteryCapacity().doubleValue() : 0)
                .requiredValue(4000.0)
                .unit("mAh")
                .severity(passed ? "INFO" : "MEDIUM")
                .message(passed ? "Battery capacity sufficient for 8+ hour operation" :
                        "Battery capacity may not support 8-hour minimum runtime")
                .recommendation(passed ? "No action required" :
                        "Increase battery capacity to minimum 4000mAh")
                .build();
    }
    
    private ConstraintCheck validatePowerConstraint(DeviceRequirement req) {
        boolean passed = req.getProcessingPower() != null && req.getProcessingPower() <= 90;
        return ConstraintCheck.builder()
                .constraintName("Processing Power Limit")
                .constraintType("SAFETY")
                .passed(passed)
                .currentValue(req.getProcessingPower() != null ? req.getProcessingPower().doubleValue() : 0)
                .requiredValue(90.0)
                .unit("%")
                .severity(passed ? "INFO" : "HIGH")
                .message(passed ? "Processing power within safe operating range" :
                        "Processing power exceeds safe operating limit")
                .recommendation(passed ? "No action required" :
                        "Reduce processing power to prevent thermal issues")
                .build();
    }
    
    private ConstraintCheck validatePortabilityConstraint(DeviceRequirement req, SystemArchitecture arch) {
        boolean isPortable = "Portable".equalsIgnoreCase(req.getPortability());
        boolean hasBattery = req.getBatteryCapacity() != null && req.getBatteryCapacity() > 0;
        boolean passed = !isPortable || hasBattery;
        
        return ConstraintCheck.builder()
                .constraintName("Portability Requirement")
                .constraintType("DESIGN")
                .passed(passed)
                .severity(passed ? "INFO" : "CRITICAL")
                .message(passed ? "Portability requirements satisfied" :
                        "Portable device must have battery power")
                .recommendation(passed ? "No action required" :
                        "Add battery power system for portable operation")
                .build();
    }
    
    private ConstraintCheck validateRegulatoryConstraint(DeviceRequirement req) {
        boolean hasStandards = req.getComplianceStandards() != null && !req.getComplianceStandards().isEmpty();
        boolean passed = hasStandards;
        
        return ConstraintCheck.builder()
                .constraintName("Regulatory Compliance")
                .constraintType("REGULATORY")
                .passed(passed)
                .severity(passed ? "INFO" : "CRITICAL")
                .message(passed ? "Compliance standards documented" :
                        "No compliance standards specified")
                .recommendation(passed ? "No action required" :
                        "Specify applicable standards (IEC 60601-1, ISO 13485, etc.)")
                .build();
    }
    
    private double calculateOverallScore(List<ConstraintCheck> checks) {
        if (checks.isEmpty()) return 0.0;
        
        long passed = checks.stream().filter(ConstraintCheck::isPassed).count();
        return (passed / (double) checks.size()) * 100.0;
    }
    
    // DTOs
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ValidationResult {
        private boolean passed;
        private int totalChecks;
        private int passedChecks;
        private double overallScore;
        private List<ConstraintViolation> violations;
        private List<ConstraintCheck> checks;
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ConstraintCheck {
        private String constraintName;
        private String constraintType;
        private boolean passed;
        private Double currentValue;
        private Double requiredValue;
        private String unit;
        private String severity;
        private String message;
        private String recommendation;
    }
    
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class ConstraintViolation {
        private String constraintName;
        private String severity;
        private String message;
        private String recommendation;
    }
}
