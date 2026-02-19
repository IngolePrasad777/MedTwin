package com.medtwin.repository;

import com.medtwin.model.DigitalTwinState;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DigitalTwinStateRepository extends MongoRepository<DigitalTwinState, String> {
    List<DigitalTwinState> findByArchitectureIdOrderByTimestampDesc(String architectureId);
    Optional<DigitalTwinState> findFirstByArchitectureIdAndIsLiveTrueOrderByTimestampDesc(String architectureId);
}
