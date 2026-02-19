package com.medtwin.repository;

import com.medtwin.model.DeviceRequirement;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRequirementRepository extends MongoRepository<DeviceRequirement, String> {
    List<DeviceRequirement> findByDeviceType(String deviceType);
    List<DeviceRequirement> findByStatus(DeviceRequirement.RequirementStatus status);
}
