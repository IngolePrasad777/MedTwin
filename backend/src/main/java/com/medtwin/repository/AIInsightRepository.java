package com.medtwin.repository;

import com.medtwin.model.AIInsight;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AIInsightRepository extends MongoRepository<AIInsight, String> {
    List<AIInsight> findBySimulationRunIdOrderByGeneratedAtDesc(String simulationRunId);
    List<AIInsight> findByArchitectureIdOrderByGeneratedAtDesc(String architectureId);
    List<AIInsight> findBySeverityOrderByGeneratedAtDesc(AIInsight.InsightSeverity severity);
}
