package com.example.lmsbackend.dto.response.course;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextbookRef {

    private Long textbookId;
    private String name;
    private String note;
}
