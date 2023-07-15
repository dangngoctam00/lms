package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "quiz_session")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "exam-session-schema-quiz-answers",
        attributeNodes = {
                @NamedAttributeNode(value = "quiz", subgraph = "quiz-sub"),
                @NamedAttributeNode(value = "answers", subgraph = "question")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "question",
                        attributeNodes = {
                                @NamedAttributeNode("question")
                        }
                ),
                @NamedSubgraph(
                        name = "quiz-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "exam", subgraph = "exam-sub")
                        }
                ),
                @NamedSubgraph(
                        name = "exam-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "questions")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "exam-session-schema-config-answers",
        attributeNodes = {
                @NamedAttributeNode(value = "quiz", subgraph = "quiz-exam"),
                @NamedAttributeNode(value = "answers", subgraph = "answers-values")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "quiz-exam",
                        attributeNodes = {
                                @NamedAttributeNode(value = "exam"),
                                @NamedAttributeNode(value = "config")
                        }
                ),
                @NamedSubgraph(
                        name = "answers-values",
                        attributeNodes = {
                                @NamedAttributeNode(value = "values")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "session-result",
        attributeNodes = {
                @NamedAttributeNode(value = "result", subgraph = "result-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "result-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode(value = "resultQuestions", subgraph = "result-questions-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "result-questions-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("question"),
                                @NamedAttributeNode(value = "answer", subgraph = "answer-subgraph")
                        }
                ),
                @NamedSubgraph(
                        name = "answer-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("values")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "session-result-quiz",
        attributeNodes = {
                @NamedAttributeNode("quiz"),
                @NamedAttributeNode("result")
        }
)
@NamedEntityGraph(
        name = "session-quiz",
        attributeNodes = {
                @NamedAttributeNode(value = "quiz", subgraph = "quiz-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "quiz-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "exam")
                        }
                )
        }
)
public class QuizSessionEntity {

    @Id
    private UUID id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "quiz_id")
    private QuizClassEntity quiz;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = LAZY)
    private StudentEntity student;

    @Column(name = "last_session")
    private Boolean lastSession = true;

    @OneToMany(fetch = LAZY, mappedBy = "session", cascade = CascadeType.ALL)
    private Set<AnswerQuestionTemporaryEntity> answers = new HashSet<>();

    @OneToOne(fetch = LAZY, mappedBy = "session", cascade = CascadeType.ALL, optional = false)
    private QuizSessionResultEntity result;
}
