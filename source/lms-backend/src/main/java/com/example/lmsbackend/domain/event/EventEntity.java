package com.example.lmsbackend.domain.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Setter
@Getter
@FieldNameConstants
public class EventEntity {

    @EmbeddedId
    private EventKey id = new EventKey();

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @Column(name = "hidden")
    private Boolean hidden = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id")
    private CalendarEntity calendar;

    public void setCalendar(CalendarEntity calendar) {
        this.calendar = calendar;
        calendar.getEvents().add(this);
    }
}
