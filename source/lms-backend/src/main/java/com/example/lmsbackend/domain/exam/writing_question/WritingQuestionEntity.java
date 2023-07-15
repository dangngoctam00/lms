package com.example.lmsbackend.domain.exam.writing_question;

import com.example.lmsbackend.domain.exam.base_question.Question;
import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "quiz_writing_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "writing-question",
        attributeNodes = {
                @NamedAttributeNode(value = "question", subgraph = "questionSource"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "questionSource",
                        attributeNodes = {
                                @NamedAttributeNode("questionSource")
                        }
                )
        }
)
public class WritingQuestionEntity extends Question {

    /**
    * https://hibernate.atlassian.net/browse/HHH-10771
    * */
    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuestionEntity question;
}