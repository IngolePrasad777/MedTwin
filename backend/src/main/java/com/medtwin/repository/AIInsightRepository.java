package com.medtwin.repository;

import com.medtwin.model.AIInsight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIInsightRepository extends JpaRepository<AIInsight, Long> {
    List<AIInsight> findBySimulationRunIdOrderByGeneratedAtDesc(Long simulationRunId);
    List<AIInsight> findByArchitectureIdOrderByGeneratedAtDesc(Long architectureId);
    List<AIInsight> findBySeverityOrderByGeneratedAtDesc(AIInsight.InsightSeverity severity);
}
