package com.example.lmsbackend.service.exam.operation;

import com.example.lmsbackend.exceptions.exam.UnsupportedQuestionTypeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionOperationFactory {

    private final WritingQuestionOperation writingQuestionOperation;
    private final MultiChoiceQuestionOperation multiChoiceQuestionOperation;
    private final FillInBlankQuestionOperation fillInBlankQuestionOperation;
    private final FillInBlankDragAndDropQuestionOperation fillInBlankDragAndDropQuestionOperation;
    private final FillInBlankMultiChoiceQuestionOperation FillInBlankMultiChoiceQuestionOperation;
    private final GroupQuestionOperation groupQuestionOperation;

    public QuestionOperation getQuestionOperation(String type) {
        switch (type) {
            case "WRITING":
                return writingQuestionOperation;
            case "MULTI_CHOICE":
                return multiChoiceQuestionOperation;
            case "FILL_IN_BLANK":
                return fillInBlankQuestionOperation;
            case "FILL_IN_BLANK_WITH_CHOICES":
                return FillInBlankMultiChoiceQuestionOperation;
            case "FILL_IN_BLANK_DRAG_AND_DROP":
                return fillInBlankDragAndDropQuestionOperation;
            case "GROUP":
                return groupQuestionOperation;
            default:
                throw new UnsupportedQuestionTypeException(type);
        }
    }
}
