package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.auth.UserDto;
import com.example.lmsbackend.dto.response.course.UnitDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class ClassSessionResponseDto {

    private Long id;
    private UserDto teacher;
    private UnitDto unit;
    @NotNull(message = "This is a required field")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime startedAt;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime finishedAt;

    private String room;
    private String note;
    private Boolean notifyWithEmail;
    private Boolean applyTeacherToAll;
    private Boolean applyRoomToAll;
}
