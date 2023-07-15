package com.example.lmsbackend.domain.exam.fill_in_blank_with_choices;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "quiz_fill_in_blank_multi_choice_blank")
@Getter
@Setter
@FieldNameConstants
public class FillInBlankMultiChoiceBlankEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillInBlankMultiChoiceQuestionEntity question;

    @Column(name = "correct_answer_key", nullable = false)
    private Integer correctAnswerKey;

    @Column(name = "hint")
    private String hint;

    @Column(name = "sort_index")
    private Integer order;

    @OneToMany(mappedBy = "blank", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("order ASC")
    private Set<FillInBlankMultiChoiceOptionEntity> options = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FillInBlankMultiChoiceBlankEntity that = (FillInBlankMultiChoiceBlankEntity) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}