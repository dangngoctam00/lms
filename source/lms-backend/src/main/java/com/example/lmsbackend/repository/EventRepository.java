package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.event.EventEntity;
import com.example.lmsbackend.domain.event.EventKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EventRepository extends JpaRepository<EventEntity, EventKey> {

    @Query("SELECT e FROM EventEntity e WHERE e.calendar.id = ?1")
    List<EventEntity> findEventsByCalendar(Long calendarId);

    @Query("SELECT e FROM EventEntity e WHERE e.calendar.id IN ?1")
    List<EventEntity> findEventsByCalendarIn(List<Long> calendarsId);
}
