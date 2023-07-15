package com.example.lmsbackend.dto.request.course;

import com.example.lmsbackend.dto.response.course.ChapterDto;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class CourseDto {
    private Long id;
    @NotBlank(message = "Tên khóa học không được để trống")
    private String name;
    @NotBlank(message = "Mã khóa học không được để trống")
    private String code;
    @NotBlank(message = "Trình độ khóa học không được để trống")
    private String level;
    private String description;
    private Integer price;
    private String background;
    private List<Long> programsId;
    private List<ChapterDto> chapters;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;
    private UsersPagedDto.UserInformation createdBy;
    private UsersPagedDto.UserInformation updatedBy;
}
