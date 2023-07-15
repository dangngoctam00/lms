package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.course.ChapterDto;
import lombok.Data;

import java.util.List;

@Data
public class ClassContentDto {
    private Long id;
    private List<ChapterDto> chapters;
}
