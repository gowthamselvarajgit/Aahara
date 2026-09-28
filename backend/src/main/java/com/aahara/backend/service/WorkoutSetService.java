package com.aahara.backend.service;

import com.aahara.backend.dto.WorkoutSetRequestDto;
import com.aahara.backend.dto.WorkoutSetResponseDto;
import com.aahara.backend.entity.WorkoutSession;
import com.aahara.backend.entity.WorkoutSet;
import com.aahara.backend.repository.WorkoutSessionRepository;
import com.aahara.backend.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkoutSetService {
    private final WorkoutSetRepository setRepository;
    private final WorkoutSessionRepository sessionRepository;

    @Transactional
    public WorkoutSetResponseDto createSet(String userId, String sessionId, WorkoutSetRequestDto request) {
        WorkoutSession session = sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
                
        if (request.getClientId() != null) {
            Optional<WorkoutSet> existing = setRepository.findBySessionIdAndClientId(sessionId, request.getClientId());
            if (existing.isPresent()) {
                return mapToDto(existing.get());
            }
        }
                
        WorkoutSet set = WorkoutSet.builder()
            .id(UUID.randomUUID().toString())
            .sessionId(session.getId())
            .exerciseId(request.getExerciseId())
            .setNumber(request.getSetNumber())
            .setType(request.getSetType())
            .weightKg(request.getWeightKg())
            .reps(request.getReps())
            .durationSeconds(request.getDurationSeconds())
            .distanceMeters(request.getDistanceMeters())
            .rpe(request.getRpe())
            .isCompleted(request.getIsCompleted())
            .clientId(request.getClientId())
            .build();
            
        return mapToDto(setRepository.save(set));
    }

    @Transactional
    public WorkoutSetResponseDto updateSet(String userId, String sessionId, String setId, WorkoutSetRequestDto request) {
        sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
                
        WorkoutSet set = setRepository.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("Set not found"));
                
        if (!set.getSessionId().equals(sessionId)) {
            throw new IllegalArgumentException("Set does not belong to session");
        }
        
        set.setExerciseId(request.getExerciseId());
        set.setSetNumber(request.getSetNumber());
        set.setSetType(request.getSetType());
        set.setWeightKg(request.getWeightKg());
        set.setReps(request.getReps());
        set.setDurationSeconds(request.getDurationSeconds());
        set.setDistanceMeters(request.getDistanceMeters());
        set.setRpe(request.getRpe());
        set.setIsCompleted(request.getIsCompleted());
        if (request.getVersion() != null) set.setVersion(request.getVersion());
        
        return mapToDto(setRepository.save(set));
    }

    @Transactional(readOnly = true)
    public List<WorkoutSetResponseDto> getSetsForSession(String userId, String sessionId) {
        sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
                
        return setRepository.findBySessionIdOrderBySetNumberAsc(sessionId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSet(String userId, String sessionId, String setId) {
        sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
                
        WorkoutSet set = setRepository.findById(setId)
                .orElseThrow(() -> new IllegalArgumentException("Set not found"));
                
        if (!set.getSessionId().equals(sessionId)) {
            throw new IllegalArgumentException("Set does not belong to session");
        }
        
        set.setDeletedAt(LocalDateTime.now());
        setRepository.save(set);
    }

    private WorkoutSetResponseDto mapToDto(WorkoutSet entity) {
        return WorkoutSetResponseDto.builder()
            .id(entity.getId())
            .sessionId(entity.getSessionId())
            .exerciseId(entity.getExerciseId())
            .setNumber(entity.getSetNumber())
            .setType(entity.getSetType())
            .weightKg(entity.getWeightKg())
            .reps(entity.getReps())
            .durationSeconds(entity.getDurationSeconds())
            .distanceMeters(entity.getDistanceMeters())
            .rpe(entity.getRpe())
            .isCompleted(entity.getIsCompleted())
            .clientId(entity.getClientId())
            .syncId(entity.getSyncId())
            .version(entity.getVersion())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
