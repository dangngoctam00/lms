package com.example.lmsbackend.dto.request.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReorderActivityDto {

    @NotNull(message = "This is  required field")
    private ReorderItem from;

    @NotNull(message = "This is  required field")
    private ReorderItem to;

    @Data
    public static class ReorderItem {
        private Long activityId;

        @NotNull(message = "This is  required field")
        private Long chapterId;
        private Integer order;
    }
}
