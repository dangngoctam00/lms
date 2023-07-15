package com.example.lmsbackend.dto.classes;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClassSchedulerConfig {
    @NotNull(message = "This is a required field")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startedAt;

    @NotNull(message = "This is a required field")
    @Positive(message = "This value must be positive")
    private Integer numberOfSession = 0;

    @NotNull(message = "This is a required field")
    @Valid
    private List<ClassSession> sessionRecurrence = new ArrayList<>();

    @Getter
    @Setter
    public static class ClassSession {
        @Pattern(regexp = "^(MONDAY|TUESDAY|WEDNESDAY|THURSDAY|FRIDAY|SATURDAY|SUNDAY)$",
                message = "For the dayOfWeek when configuring class scheduler, only values MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY are accepted")
        private String dayOfWeek;
        @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "This field must be HH:MM format")
        private String startedAt;
        @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]", message = "This field must be HH:MM format")
        private String finishedAt;
    }
}
