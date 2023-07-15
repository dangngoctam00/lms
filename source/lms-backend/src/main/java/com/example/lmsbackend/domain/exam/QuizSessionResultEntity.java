package com.example.lmsbackend.domain.exam;


import com.example.lmsbackend.enums.FinalVerdict;
import com.example.lmsbackend.enums.GradedState;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "quiz_session_result")
@Getter
@Setter
@FieldNameConstants
public class QuizSessionResultEntity {

    @Id
    private UUID id;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private QuizSessionEntity session;

    @OneToMany(fetch = LAZY, mappedBy = "sessionResult", cascade = ALL)
    private Set<QuizSessionResultQuestionEntity> resultQuestions = new HashSet<>();

    @Column(name = "score")
    private Double score;

    @Column(name = "graded_state")
    @Enumerated(EnumType.STRING)
    @NotNull
    private GradedState gradedState;

    @Column(name = "final_verdict")
    @Enumerated(EnumType.STRING)
    private FinalVerdict finalVerdict;

    public void setSession(QuizSessionEntity session) {
        this.session = session;
        session.setResult(this);
    }

    public void addGrade(Double grade) {
        if (score == null) {
            this.score = grade;
        } else {
            this.score += grade;
        }
    }
}
