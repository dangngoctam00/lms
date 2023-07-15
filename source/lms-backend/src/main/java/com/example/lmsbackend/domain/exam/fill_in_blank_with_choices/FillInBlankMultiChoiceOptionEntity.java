package com.example.lmsbackend.domain.exam.fill_in_blank_with_choices;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "quiz_fill_in_blank_multi_choice_option")
@Getter
@Setter
@FieldNameConstants
public class FillInBlankMultiChoiceOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "blank_id")
    private FillInBlankMultiChoiceBlankEntity blank;

    @Column(name = "answer_key")
    private Integer answerKey;

    @Column(name = "content")
    private String content;

    @Column(name = "sort_index")
    private Integer order;
}