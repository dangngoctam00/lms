package com.example.lmsbackend.dto.calendar;


import lombok.Data;
import lombok.NonNull;

import javax.annotation.Nullable;

@Data
public class CalendarModificationDto {
    @Nullable
    private String description;
    @Nullable
    private String location;
    @NonNull
    private String summary;
    @Nullable
    private String timeZone;
}
