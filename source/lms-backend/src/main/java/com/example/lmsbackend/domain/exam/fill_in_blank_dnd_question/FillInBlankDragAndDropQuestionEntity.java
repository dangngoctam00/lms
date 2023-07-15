package com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_fill_in_blank_drag_and_drop_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "fill-in-blank-drag-and-drop-answers-blanks",
        attributeNodes = {
                @NamedAttributeNode("answers"),
                @NamedAttributeNode("blanks"),
        }
)
public class FillInBlankDragAndDropQuestionEntity extends Question {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OrderBy("order ASC")
    private Set<FillInBlankDragAndDropAnswerEntity> answers = new HashSet<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OrderBy("order ASC")
    private Set<FillInBlankDragAndDropBlankEntity> blanks = new HashSet<>();

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionEntity question;
}