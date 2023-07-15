package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface ClassSessionRepositoryCustom {

    List<ClassSessionEntity> findClassSessions(Long classId, LocalDateTime date, Integer previous, Integer after);
}
