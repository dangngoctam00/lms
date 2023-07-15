package com.example.lmsbackend.dto.request.course;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ReorderChapterDto {

    @NotNull(message = "This is  required field")
    private ReorderItem from;

    @NotNull(message = "This is a required field")
    private ReorderItem to;

    @Data
    public static class ReorderItem {
        private Long id;
        private Integer order;
    }
}
