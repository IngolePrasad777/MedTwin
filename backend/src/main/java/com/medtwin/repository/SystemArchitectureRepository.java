package com.medtwin.repository;

import com.medtwin.model.SystemArchitecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SystemArchitectureRepository extends JpaRepository<SystemArchitecture, Long> {
    Optional<SystemArchitecture> findByRequirementId(Long requirementId);
}
