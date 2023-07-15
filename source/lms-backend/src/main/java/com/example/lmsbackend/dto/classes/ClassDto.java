package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.course.CourseInformationDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ClassDto {
    private Long id;
    private String name;
    private String code;
    private String avatar;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate startedAt;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate endedAt;
    private String calendarId;
    private String daysOfWeek;

    @Pattern(regexp = "^(CREATED|ONGOING|ENDED)$",
            message = "For the Type, only values NOT_AVAILABLE, AVAILABLE, COMPLETED are accepted")
    private String status;

    @Pattern(regexp = "^(ONLINE|OFFLINE|HYBRID)$",
            message = "For the Type, only values ONLINE, OFFLINE, HYBRID are accepted")
    private String type;

    private CourseInformationDto course;

    public ClassDto() {
    }

    public ClassDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
