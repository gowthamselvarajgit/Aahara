package com.aahara.backend.repository;

import com.aahara.backend.entity.WaterLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WaterLogRepository extends JpaRepository<WaterLog, String> {
    List<WaterLog> findByUserIdAndEntryDate(String userId, LocalDate entryDate);
    Optional<WaterLog> findByIdAndUserId(String id, String userId);
    Optional<WaterLog> findByUserIdAndClientId(String userId, String clientId);
}
