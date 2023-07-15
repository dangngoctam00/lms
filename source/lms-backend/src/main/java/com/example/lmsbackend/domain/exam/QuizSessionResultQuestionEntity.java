package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "quiz_session_result_question")
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "exam-session-result-answer",
        attributeNodes = {
                @NamedAttributeNode(value = "answer", subgraph = "answer-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "answer-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("values")
                        }
                )
        }
)
public class QuizSessionResultQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "session_result_id")
    private QuizSessionResultEntity sessionResult;

    @OneToOne(fetch = LAZY, optional = false)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @OneToOne(fetch = LAZY, cascade = {PERSIST, MERGE})
    @JoinColumn(name = "answer_id")
    private AnswerQuestionTemporaryEntity answer;

    @Column(name = "earned_point")
    private Double earnedPoint;

    public void setSessionResult(QuizSessionResultEntity sessionResult) {
        this.sessionResult = sessionResult;
        sessionResult.getResultQuestions().add(this);
    }

    public void setSessionResultWithout(QuizSessionResultEntity sessionResult) {
        this.sessionResult = sessionResult;
    }
}
