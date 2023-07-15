package com.example.lmsbackend.domain.exam.base_question;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "quiz_group_question")
@FieldNameConstants
@NamedEntityGraph(
        name = "group-question",
        attributeNodes = {
                @NamedAttributeNode(value = "questions", subgraph = "questions-question"),
                @NamedAttributeNode("question"),
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "questions-question",
                        attributeNodes = {
                                @NamedAttributeNode(value = "question", subgraph = "questions-question-questionSource")
                        }
                ),
                @NamedSubgraph(
                        name = "questions-question-questionSource",
                        attributeNodes = {
                                @NamedAttributeNode("questionSource")
                        }
                )
        }
)
public class GroupQuestionEntity extends Question {

    @OneToMany(fetch = LAZY, mappedBy = "group", cascade = CascadeType.ALL)
    @org.hibernate.annotations.OrderBy(clause = "order ASC")
    private Set<QuestionInGroupEntity> questions = new HashSet<>();

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "id")
    private QuestionEntity question;
}
