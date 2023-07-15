package com.example.lmsbackend.domain.exam;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@Table(name = "quiz_answer_temporary")
@FieldNameConstants
public class AnswerTemporaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "sort_order")
    private Integer order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "answer_question_id")
    private AnswerQuestionTemporaryEntity answer;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerTemporaryEntity that = (AnswerTemporaryEntity) o;
        if (this.id == null || that.id == null) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
