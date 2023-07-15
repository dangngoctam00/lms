package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;

import java.util.List;

public interface SessionAttendanceTimeRepositoryCustom {

    List<SessionAttendanceTimeEntity> findBySessionId(Long session);
}
