package com.example.lmsbackend.utils;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.EventDateTime;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.TimeZone;

public class DateTimeUtils {

    public static DateTime getGoogleDateTime(LocalDateTime dateTime) {
        return new DateTime(Timestamp.valueOf(dateTime), TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }

    public static EventDateTime getGoogleEventDateTime(LocalDateTime dateTime) {
        var eventDateTime = new EventDateTime();
        eventDateTime.setDateTime(getGoogleDateTime(dateTime));
        return eventDateTime;
    }
}
