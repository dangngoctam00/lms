package com.example.lmsbackend.dto.request.google.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.calendar.model.EventDateTime;
import lombok.Data;

@Data
public class EventDto {
    @JsonProperty("start")
    private EventDateTime start;
    @JsonProperty("end")
    private EventDateTime end;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("location")
    private String location;
    @JsonProperty("description")
    private String description;
}
