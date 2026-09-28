package com.aahara.backend.repository;

import com.aahara.backend.entity.DiaryEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiaryEntryRepository extends JpaRepository<DiaryEntry, String> {
    @Query("SELECT d FROM DiaryEntry d WHERE d.userId = :userId AND d.entryDate >= :fromDate AND d.entryDate <= :toDate ORDER BY d.entryDate DESC")
    Page<DiaryEntry> findByUserIdAndEntryDateBetweenOrderByEntryDateDesc(@Param("userId") String userId, @Param("fromDate") LocalDate fromDate, @Param("toDate") LocalDate toDate, Pageable pageable);
    
    List<DiaryEntry> findByUserIdAndEntryDate(String userId, LocalDate entryDate);
    Optional<DiaryEntry> findByIdAndUserId(String id, String userId);
}
