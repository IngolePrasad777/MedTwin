package com.medtwin.repository;

import com.medtwin.model.SystemArchitecture;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemArchitectureRepository extends MongoRepository<SystemArchitecture, String> {
    Optional<SystemArchitecture> findByRequirementId(String requirementId);
}
