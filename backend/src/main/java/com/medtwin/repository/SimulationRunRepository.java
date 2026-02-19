package com.medtwin.repository;

import com.medtwin.model.SimulationRun;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRunRepository extends MongoRepository<SimulationRun, String> {
    List<SimulationRun> findByArchitectureIdOrderByStartedAtDesc(String architectureId);
    List<SimulationRun> findByStatus(SimulationRun.SimulationStatus status);
}
