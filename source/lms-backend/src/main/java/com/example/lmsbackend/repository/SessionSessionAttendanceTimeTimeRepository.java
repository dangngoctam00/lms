package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;
import com.example.lmsbackend.repository.custom.SessionAttendanceTimeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessionSessionAttendanceTimeTimeRepository extends JpaRepository<SessionAttendanceTimeEntity, Long>, SessionAttendanceTimeRepositoryCustom {

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM SessionAttendanceTimeEntity a WHERE a.id = ?1 AND a.isOfficial = true")
    Boolean existsByIdAndOfficial(Long id);
}
