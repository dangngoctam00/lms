package com.example.lmsbackend.dto.resource;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class TextbookDto {
    private Long id;

    @NotNull(message = "This field is required")
    private String name;
    private String attachment;
    private String author;
    private String description;
}
