package com.example.lmsbackend.dto.exam;

import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoiceQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.group.GroupQuestionDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoiceQuestionAnswerDto;
import com.example.lmsbackend.dto.exam.writing.WritingQuestionDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = WritingQuestionDto.class, name = "WRITING"),
                @JsonSubTypes.Type(value = MultiChoiceQuestionAnswerDto.class, name = "MULTI_CHOICE"),
                @JsonSubTypes.Type(value = FillInBlankQuestionAnswerDto.class, name = "FILL_IN_BLANK"),
                @JsonSubTypes.Type(value = FillInBlankMultiChoiceQuestionAnswerDto.class, name = "FILL_IN_BLANK_WITH_CHOICES"),
                @JsonSubTypes.Type(value = FillInBlankDragAndDropQuestionAnswerDto.class, name = "FILL_IN_BLANK_DRAG_AND_DROP"),
                @JsonSubTypes.Type(value = GroupQuestionDto.class, name = "GROUP")
        }
)
public class QuestionAnswerDto {
    private Long id;
    private String description;
    private String note;
    private Integer point;
    private String level;
    private Integer order;
    private String attachment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String type;
    private Long textbookId;
    private Double earnedPoint;
}
