package com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FillInBlankDragAndDropAnswerDto {
    private Long id;
    private Long key;
    private Long order;
    private String content;
}
