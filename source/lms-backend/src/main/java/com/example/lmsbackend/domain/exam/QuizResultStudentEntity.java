package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.enums.FinalVerdict;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Table(name = "quiz_result_student")
@Entity
@Getter
@Setter
@FieldNameConstants
public class QuizResultStudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "exam_result_id")
    private QuizResultEntity quizResult;

    @Column(name = "grade")
    @NotNull
    private Double grade;

    @Column(name = "result")
    @NotNull
    private FinalVerdict result;

    @Column(name = "number_of_attempt")
    private Integer numberOfAttempt;

    public void setQuizResult(QuizResultEntity quizResult) {
        this.quizResult = quizResult;
        quizResult.getStudentsResult().add(this);
    }
}
