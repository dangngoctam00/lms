package com.example.lmsbackend.domain.exam.fill_in_blank_with_choices;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_fill_in_blank_multi_choice_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "fill-in-blank-multi-choice-blanks",
        attributeNodes = {
                @NamedAttributeNode(value = "blanks", subgraph = "blanks-options")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "blanks-options",
                        attributeNodes = {
                                @NamedAttributeNode("options")
                        }
                )
        }
)
public class FillInBlankMultiChoiceQuestionEntity extends Question {

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<FillInBlankMultiChoiceBlankEntity> blanks = new HashSet<>();

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionEntity question;
}