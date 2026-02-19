package com.medtwin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Compliance Traceability Model
 * Priority 4: Enterprise-grade compliance tracking
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplianceCheck {
    
    private String standard; // e.g., "IEC 60601-1"
    private String clauseReference; // e.g., "Section 11.6.3"
    private String requirement; // What the standard requires
    private ComplianceStatus status; // PASS, WARNING, FAIL
    private String reasoning; // Why this status
    private String recommendation; // How to fix if not passing
    private Double currentValue; // Measured value
    private Double requiredValue; // Required value
    private String unit; // Unit of measurement
    
    public enum ComplianceStatus {
        PASS, WARNING, FAIL, NOT_APPLICABLE
    }
}
