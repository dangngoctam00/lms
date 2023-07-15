package com.example.lmsbackend.domain.exam.base_question;


import com.example.lmsbackend.domain.exam.ExamQuestionSourceEntity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;


@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "question_source")
@FieldNameConstants
public class QuestionSourceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne(optional = false, fetch = LAZY, cascade = CascadeType.ALL, mappedBy = "questionSource")
    private QuestionEntity question;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true, fetch = LAZY)
    private Set<ExamQuestionSourceEntity> exams = new HashSet<>();

    public void setQuestion(QuestionEntity question) {
        this.question = question;
        question.setQuestionSource(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        QuestionSourceEntity that = (QuestionSourceEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
