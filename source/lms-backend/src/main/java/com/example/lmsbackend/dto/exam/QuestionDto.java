package com.example.lmsbackend.dto.exam;


import com.example.lmsbackend.dto.exam.fill_in_blank.FillInBlankQuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_drag_and_drop.FillInBlankDragAndDropQuestionDto;
import com.example.lmsbackend.dto.exam.fill_in_blank_multi_choices.FillInBlankMultiChoiceQuestionDto;
import com.example.lmsbackend.dto.exam.group.GroupQuestionDto;
import com.example.lmsbackend.dto.exam.multi_choice.MultiChoicesQuestionDto;
import com.example.lmsbackend.dto.exam.writing.WritingQuestionDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, visible = true, property = "type")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = WritingQuestionDto.class, name = "WRITING"),
                @JsonSubTypes.Type(value = MultiChoicesQuestionDto.class, name = "MULTI_CHOICE"),
                @JsonSubTypes.Type(value = FillInBlankQuestionDto.class, name = "FILL_IN_BLANK"),
                @JsonSubTypes.Type(value = FillInBlankMultiChoiceQuestionDto.class, name = "FILL_IN_BLANK_WITH_CHOICES"),
                @JsonSubTypes.Type(value = FillInBlankDragAndDropQuestionDto.class, name = "FILL_IN_BLANK_DRAG_AND_DROP"),
                @JsonSubTypes.Type(value = GroupQuestionDto.class, name = "GROUP")
        }
)
public class QuestionDto {
    private Long id;
    private String description;
    private String note;
    private Integer point;
    private Integer order;
    private String attachment;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Pattern(regexp = "^(WRITING|MULTI_CHOICE|FILL_IN_BLANK|FILL_IN_BLANK_WITH_CHOICES|FILL_IN_BLANK_DRAG_AND_DROP|GROUP)$",
            message = "For the Type, only values WRITING, MULTI_CHOICE, FILL_IN_BLANK, FILL_IN_BLANK_WITH_CHOICES, FILL_IN_BLANK_DRAG_AND_DROP, GROUP are accepted")
    private String type;
}
