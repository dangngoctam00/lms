package com.example.lmsbackend.domain.exam.multi_choice;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_multi_choice_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "multi-choice-options",
        attributeNodes = {
                @NamedAttributeNode("options"),
        }
)
public class MultiChoiceQuestionEntity extends Question {

    @Column(name = "is_multiple_answer")
    private Boolean isMultipleAnswer;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<MultiChoiceOptionEntity> options = new HashSet<>();

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionEntity question;
}