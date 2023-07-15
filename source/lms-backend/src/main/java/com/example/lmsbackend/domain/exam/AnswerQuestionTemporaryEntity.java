package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "quiz_answer_question_temporary")
@FieldNameConstants
@NamedEntityGraph(
        name = "answer-question-temporary-session-values-question",
        attributeNodes = {
                @NamedAttributeNode("session"),
                @NamedAttributeNode("question"),
                @NamedAttributeNode("values"),
        }
)
public class AnswerQuestionTemporaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "session_id")
    private QuizSessionEntity session;

    @OneToMany(fetch = LAZY, mappedBy = "answer", cascade = ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<AnswerTemporaryEntity> values = new HashSet<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id")
    private QuestionEntity question;

    @Column(name = "optional_student_note")
    private String optionalStudentNote;

    @Column(name = "attempts")
    private Integer attempts = 0;

    public void setSession(QuizSessionEntity session) {
        this.session = session;
        session.getAnswers().add(this);
    }

    public void removeAnswer(AnswerTemporaryEntity answer) {
        this.values.remove(answer);
        answer.setAnswer(null);
    }
}
