package com.aahara.backend.repository;

import com.aahara.backend.entity.WorkoutSession;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, String> {
    @Query("SELECT w FROM WorkoutSession w WHERE w.userId = :userId AND w.startTime >= :fromTime AND w.startTime <= :toTime ORDER BY w.startTime DESC")
    Page<WorkoutSession> findByUserIdAndStartTimeBetweenOrderByStartTimeDesc(@Param("userId") String userId, @Param("fromTime") LocalDateTime fromTime, @Param("toTime") LocalDateTime toTime, Pageable pageable);
    
    Optional<WorkoutSession> findByIdAndUserId(String id, String userId);
}
