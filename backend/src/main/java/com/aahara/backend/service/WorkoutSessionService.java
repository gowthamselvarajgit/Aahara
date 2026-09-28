package com.aahara.backend.service;

import com.aahara.backend.dto.WorkoutSessionRequestDto;
import com.aahara.backend.dto.WorkoutSessionResponseDto;
import com.aahara.backend.entity.WorkoutSession;
import com.aahara.backend.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WorkoutSessionService {
    private final WorkoutSessionRepository sessionRepository;

    @Transactional
    public WorkoutSessionResponseDto createSession(String userId, WorkoutSessionRequestDto request) {
        if (request.getClientId() != null) {
            Optional<WorkoutSession> existing = sessionRepository.findByUserIdAndClientId(userId, request.getClientId());
            if (existing.isPresent()) {
                return mapToDto(existing.get());
            }
        }

        WorkoutSession session = WorkoutSession.builder()
            .id(UUID.randomUUID().toString())
            .userId(userId)
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .routineId(request.getRoutineId())
            .notes(request.getNotes())
            .clientId(request.getClientId())
            .build();
        return mapToDto(sessionRepository.save(session));
    }

    @Transactional(readOnly = true)
    public Page<WorkoutSessionResponseDto> getSessions(String userId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return sessionRepository.findByUserIdAndStartTimeBetweenOrderByStartTimeDesc(userId, from, to, pageable)
                .map(this::mapToDto);
    }

    @Transactional(readOnly = true)
    public WorkoutSessionResponseDto getSession(String userId, String id) {
        WorkoutSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
        return mapToDto(session);
    }

    @Transactional
    public WorkoutSessionResponseDto updateSession(String userId, String id, WorkoutSessionRequestDto request) {
        WorkoutSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());
        session.setNotes(request.getNotes());
        session.setRoutineId(request.getRoutineId());
        if (request.getVersion() != null) session.setVersion(request.getVersion());
        return mapToDto(sessionRepository.save(session));
    }

    @Transactional
    public void deleteSession(String userId, String id) {
        WorkoutSession session = sessionRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found or unauthorized"));
        session.setDeletedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    private WorkoutSessionResponseDto mapToDto(WorkoutSession entity) {
        return WorkoutSessionResponseDto.builder()
            .id(entity.getId())
            .userId(entity.getUserId())
            .routineId(entity.getRoutineId())
            .startTime(entity.getStartTime())
            .endTime(entity.getEndTime())
            .notes(entity.getNotes())
            .clientId(entity.getClientId())
            .syncId(entity.getSyncId())
            .version(entity.getVersion())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
