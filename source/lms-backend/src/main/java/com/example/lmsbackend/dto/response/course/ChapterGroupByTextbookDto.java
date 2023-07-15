package com.example.lmsbackend.dto.response.course;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChapterGroupByTextbookDto {

    private Long textbookId;
    private String textbookName;
    private List<ChapterDto> chapters = new ArrayList();
}
