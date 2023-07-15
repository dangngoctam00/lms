package com.example.lmsbackend.domain.exam.multi_choice;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "quiz_multi_choice_option")
@Getter
@Setter
@FieldNameConstants
public class MultiChoiceOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "is_correct")
    private Boolean isCorrect;

    @Column(name = "answer_key")
    private Integer answerKey;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private MultiChoiceQuestionEntity question;

    @Column(name = "sort_index")
    private Integer order;
}