package com.medtwin.service;

import com.medtwin.model.DeviceRequirement;
import com.medtwin.repository.DeviceRequirementRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Capability 1: Requirement Processing
 * Validates and processes medical device requirements
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RequirementProcessingService {
    
    private final DeviceRequirementRepository requirementRepository;
    
    @Transactional
    public DeviceRequirement processRequirement(DeviceRequirement requirement) {
        log.info("Processing requirement for device type: {}", requirement.getDeviceType());
        
        // Validate requirement
        validateRequirement(requirement);
        
        // Set defaults if not provided
        applyDefaults(requirement);
        
        // Save requirement
        DeviceRequirement saved = requirementRepository.save(requirement);
        
        log.info("Requirement processed successfully with ID: {}", saved.getId());
        return saved;
    }
    
    private void validateRequirement(DeviceRequirement requirement) {
        if (requirement.getDeviceType() == null || requirement.getDeviceType().isBlank()) {
            throw new IllegalArgumentException("Device type is required");
        }
        
        if (requirement.getComplianceStandards() == null || requirement.getComplianceStandards().isEmpty()) {
            throw new IllegalArgumentException("At least one compliance standard is required");
        }
        
        // Validate technical specifications
        if (requirement.getBatteryCapacity() != null && requirement.getBatteryCapacity() < 1000) {
            throw new IllegalArgumentException("Battery capacity must be at least 1000 mAh");
        }
        
        if (requirement.getSamplingRate() != null && (requirement.getSamplingRate() < 10 || requirement.getSamplingRate() > 200)) {
            throw new IllegalArgumentException("Sampling rate must be between 10-200 Hz");
        }
        
        log.debug("Requirement validation passed");
    }
    
    private void applyDefaults(DeviceRequirement requirement) {
        if (requirement.getDeviceClass() == null) {
            requirement.setDeviceClass("IIb");
        }
        
        if (requirement.getPowerMode() == null) {
            requirement.setPowerMode(DeviceRequirement.PowerMode.BALANCED);
        }
        
        if (requirement.getBatteryCapacity() == null) {
            requirement.setBatteryCapacity(5000);
        }
        
        if (requirement.getSamplingRate() == null) {
            requirement.setSamplingRate(100);
        }
        
        if (requirement.getTargetAirflow() == null) {
            requirement.setTargetAirflow(45);
        }
        
        if (requirement.getProcessingPower() == null) {
            requirement.setProcessingPower(75);
        }
        
        if (requirement.getThermalThreshold() == null) {
            requirement.setThermalThreshold(60);
        }
        
        log.debug("Applied default values to requirement");
    }
    
    public DeviceRequirement getRequirement(Long id) {
        return requirementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requirement not found with ID: " + id));
    }
    
    public List<DeviceRequirement> getAllRequirements() {
        return requirementRepository.findAll();
    }
    
    public List<DeviceRequirement> getRequirementsByType(String deviceType) {
        return requirementRepository.findByDeviceType(deviceType);
    }
    
    @Transactional
    public DeviceRequirement updateRequirement(Long id, DeviceRequirement updatedRequirement) {
        DeviceRequirement existing = getRequirement(id);
        
        // Update fields
        existing.setDeviceType(updatedRequirement.getDeviceType());
        existing.setDeviceClass(updatedRequirement.getDeviceClass());
        existing.setPowerSource(updatedRequirement.getPowerSource());
        existing.setPortability(updatedRequirement.getPortability());
        existing.setComplianceStandards(updatedRequirement.getComplianceStandards());
        existing.setFunctionalRequirements(updatedRequirement.getFunctionalRequirements());
        existing.setTargetAirflow(updatedRequirement.getTargetAirflow());
        existing.setSamplingRate(updatedRequirement.getSamplingRate());
        existing.setBatteryCapacity(updatedRequirement.getBatteryCapacity());
        existing.setProcessingPower(updatedRequirement.getProcessingPower());
        existing.setThermalThreshold(updatedRequirement.getThermalThreshold());
        existing.setPowerMode(updatedRequirement.getPowerMode());
        
        validateRequirement(existing);
        
        return requirementRepository.save(existing);
    }
}
