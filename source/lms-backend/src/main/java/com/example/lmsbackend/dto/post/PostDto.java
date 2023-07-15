package com.example.lmsbackend.dto.post;

import com.example.lmsbackend.dto.classes.CommentPagedDto;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
public class PostDto {

    private Long id;

    @NotBlank(message = "This is required field")
    private String title;

    @NotBlank(message = "This is required field")
    private String content;

    private CommentPagedDto comments;
    private Integer repliesCount = 0;

    private Integer upVoteCount;
    private Integer downVoteCount;

    private String personalPreference;

    private UsersPagedDto.UserInformation createdBy;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;
}
