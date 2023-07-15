package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Table(name = "quiz_result")
@Entity
@Getter
@Setter
@FieldNameConstants
public class QuizResultEntity {

    @Id
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "quiz_id")
    @MapsId
    private QuizClassEntity quiz;

    @Column(name = "number_of_participants")
    private Integer numberOfParticipants;

    @Column(name = "gpa")
    private Double gpa;

    @Column(name = "number_of_passed_student")
    private Integer numberOfPassedStudent;

    @OneToMany(fetch = LAZY, cascade = ALL, orphanRemoval = true, mappedBy = "quizResult")
    private Set<QuizResultStudentEntity> studentsResult = new HashSet<>();

    public void setQuiz(QuizClassEntity quiz) {
        this.quiz = quiz;
        quiz.setResult(this);
    }
}
