package com.example.lmsbackend.dto.calendar;

import com.google.api.services.calendar.model.Event;
import lombok.Data;

@Data
public class EventDto {

    private Event event;
    private String eventType;
}
