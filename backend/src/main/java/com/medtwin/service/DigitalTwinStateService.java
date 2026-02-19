package com.medtwin.service;

import com.medtwin.model.DigitalTwinState;
import com.medtwin.model.SystemArchitecture;
import com.medtwin.repository.DigitalTwinStateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Capability 3: Digital Twin State Management
 * Manages real-time state of digital twin instances
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DigitalTwinStateService {
    
    private final DigitalTwinStateRepository stateRepository;
    private final ArchitectureGenerationService architectureService;
    private final Random random = new Random();
    
    @Transactional
    public DigitalTwinState createInitialState(Long architectureId) {
        log.info("Creating initial digital twin state for architecture ID: {}", architectureId);
        
        SystemArchitecture architecture = architectureService.getArchitecture(architectureId);
        
        DigitalTwinState state = DigitalTwinState.builder()
                .architecture(architecture)
                .batteryLevel(100.0)
                .thermalLoad(25.0)
                .airflowRate(45.0)
                .pressure(15.0)
                .systemLoad(20.0)
                .efficiency(95.0)
                .samplingRate(100)
                .powerConsumption(5.5)
                .componentStates(initializeComponentStates(architecture))
                .healthScore(100)
                .reliabilityScore(architecture.getReliabilityScore())
                .status(DigitalTwinState.OperationalStatus.NORMAL)
                .isLive(true)
                .build();
        
        return stateRepository.save(state);
    }
    
    private Map<String, String> initializeComponentStates(SystemArchitecture architecture) {
        Map<String, String> states = new HashMap<>();
        architecture.getComponents().forEach(component -> {
            states.put(component.getComponentName(), "OPERATIONAL");
        });
        return states;
    }
    
    @Transactional
    public DigitalTwinState updateState(Long architectureId, DigitalTwinState updatedState) {
        log.debug("Updating digital twin state for architecture ID: {}", architectureId);
        
        DigitalTwinState currentState = getCurrentState(architectureId);
        
        // Update metrics
        currentState.setBatteryLevel(updatedState.getBatteryLevel());
        currentState.setThermalLoad(updatedState.getThermalLoad());
        currentState.setAirflowRate(updatedState.getAirflowRate());
        currentState.setPressure(updatedState.getPressure());
        currentState.setSystemLoad(updatedState.getSystemLoad());
        currentState.setEfficiency(updatedState.getEfficiency());
        currentState.setSamplingRate(updatedState.getSamplingRate());
        currentState.setPowerConsumption(updatedState.getPowerConsumption());
        
        // Update health and status
        updateHealthAndStatus(currentState);
        
        return stateRepository.save(currentState);
    }
    
    /**
     * Simulates real-time state updates (for demo purposes)
     * In production, this would receive data from actual devices
     */
    @Scheduled(fixedRate = 5000) // Every 5 seconds
    @Transactional
    public void simulateRealTimeUpdates() {
        List<DigitalTwinState> liveStates = stateRepository.findAll().stream()
                .filter(DigitalTwinState::getIsLive)
                .toList();
        
        for (DigitalTwinState state : liveStates) {
            // Simulate realistic changes
            state.setBatteryLevel(Math.max(0, state.getBatteryLevel() - 0.05));
            state.setThermalLoad(state.getThermalLoad() + (random.nextDouble() - 0.5) * 0.5);
            state.setAirflowRate(state.getAirflowRate() + (random.nextDouble() - 0.5) * 2);
            state.setPressure(state.getPressure() + (random.nextDouble() - 0.5) * 0.5);
            state.setSystemLoad(Math.max(10, Math.min(100, state.getSystemLoad() + (random.nextDouble() - 0.5) * 5)));
            state.setEfficiency(Math.max(60, Math.min(100, state.getEfficiency() + (random.nextDouble() - 0.5) * 2)));
            
            // Calculate power consumption
            state.setPowerConsumption(calculatePowerConsumption(state));
            
            // Update health and status
            updateHealthAndStatus(state);
            
            stateRepository.save(state);
        }
        
        if (!liveStates.isEmpty()) {
            log.debug("Updated {} live digital twin states", liveStates.size());
        }
    }
    
    private double calculatePowerConsumption(DigitalTwinState state) {
        double basePower = 2.5;
        double samplingPower = (state.getSamplingRate() / 100.0) * 1.2;
        double systemLoadPower = (state.getSystemLoad() / 100.0) * 2.0;
        double airflowPower = (state.getAirflowRate() / 50.0) * 1.5;
        
        return basePower + samplingPower + systemLoadPower + airflowPower;
    }
    
    private void updateHealthAndStatus(DigitalTwinState state) {
        int healthScore = 100;
        
        // Deduct health based on various factors
        if (state.getBatteryLevel() < 20) healthScore -= 20;
        else if (state.getBatteryLevel() < 50) healthScore -= 10;
        
        if (state.getThermalLoad() > 60) healthScore -= 25;
        else if (state.getThermalLoad() > 50) healthScore -= 10;
        
        if (state.getSystemLoad() > 90) healthScore -= 15;
        else if (state.getSystemLoad() > 80) healthScore -= 5;
        
        if (state.getEfficiency() < 70) healthScore -= 20;
        else if (state.getEfficiency() < 80) healthScore -= 10;
        
        state.setHealthScore(Math.max(0, healthScore));
        
        // Determine operational status
        if (healthScore < 50) {
            state.setStatus(DigitalTwinState.OperationalStatus.CRITICAL);
        } else if (healthScore < 75) {
            state.setStatus(DigitalTwinState.OperationalStatus.WARNING);
        } else {
            state.setStatus(DigitalTwinState.OperationalStatus.NORMAL);
        }
    }
    
    public DigitalTwinState getCurrentState(Long architectureId) {
        return stateRepository.findFirstByArchitectureIdAndIsLiveTrueOrderByTimestampDesc(architectureId)
                .orElseThrow(() -> new IllegalArgumentException("No live state found for architecture ID: " + architectureId));
    }
    
    public List<DigitalTwinState> getStateHistory(Long architectureId, int limit) {
        List<DigitalTwinState> states = stateRepository.findByArchitectureIdOrderByTimestampDesc(architectureId);
        return states.stream().limit(limit).toList();
    }
    
    @Transactional
    public void deactivateState(Long architectureId) {
        DigitalTwinState state = getCurrentState(architectureId);
        state.setIsLive(false);
        state.setStatus(DigitalTwinState.OperationalStatus.OFFLINE);
        stateRepository.save(state);
        log.info("Deactivated digital twin state for architecture ID: {}", architectureId);
    }
}
