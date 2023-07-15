package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.event.CalendarEntity;
import com.example.lmsbackend.domain.event.CalendarType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CalendarRepository extends JpaRepository<CalendarEntity, String> {

    Optional<CalendarEntity> findByTypeAndAndTypeId(CalendarType type, Long typeId);

    @Query("SELECT c FROM CalendarEntity c LEFT JOIN FETCH c.events WHERE c.type = ?1 AND c.typeId = ?2")
    Optional<CalendarEntity> findFetchEventsByTypeAndAndTypeId(CalendarType type, Long typeId);
}
