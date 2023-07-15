package com.example.lmsbackend.dto.response.course;

import com.example.lmsbackend.domain.coursemodel.UnitCourseTextBookEntity;
import com.example.lmsbackend.enums.ActivityState;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UnitDto {
    private Long id;
    @NotNull(message = "This is a required field")
    private String title;
    private String content;
    private List<TextbookInUnit> textbooks = new ArrayList<>();
    private Integer order;
    private Boolean isFromCourse = false;
    private String state;
    private String attachment;

    public UnitDto() {
    }

    public UnitDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public UnitDto(Long id, String title, Boolean isFromCourse) {
        this.id = id;
        this.title = title;
        this.isFromCourse = isFromCourse;
    }

    public UnitDto(Long id, String title, Long unitCourseId, ActivityState state) {
        this.id = id;
        this.title = title;
        this.state = state.getState();
        this.isFromCourse = unitCourseId != null;
    }

    public UnitDto(Long id, String title, Set<UnitCourseTextBookEntity> textbooks) {
        this.id = id;
        this.title = title;
        this.textbooks = textbooks.stream()
                .map(t -> {
                    var dto = new TextbookInUnit();
                    dto.setTextbookId(t.getId().getTextbookId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Data
    public static class TextbookInUnit {
        private Long textbookId;
        private String name;
        private String note;
    }
}
