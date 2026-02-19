package com.medtwin.repository;

import com.medtwin.model.SimulationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SimulationRunRepository extends JpaRepository<SimulationRun, Long> {
    List<SimulationRun> findByArchitectureIdOrderByStartedAtDesc(Long architectureId);
    List<SimulationRun> findByStatus(SimulationRun.SimulationStatus status);
}
