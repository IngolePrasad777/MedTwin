package com.medtwin.repository;

import com.medtwin.model.DeviceRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRequirementRepository extends JpaRepository<DeviceRequirement, Long> {
    List<DeviceRequirement> findByDeviceType(String deviceType);
    List<DeviceRequirement> findByStatus(DeviceRequirement.RequirementStatus status);
}
