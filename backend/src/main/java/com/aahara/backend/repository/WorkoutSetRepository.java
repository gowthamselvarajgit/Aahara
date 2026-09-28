package com.aahara.backend.repository;

import com.aahara.backend.entity.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, String> {
    List<WorkoutSet> findBySessionIdOrderBySetNumberAsc(String sessionId);
    Optional<WorkoutSet> findBySessionIdAndClientId(String sessionId, String clientId);
}
