package com.example.lmsbackend.domain.exam.fill_in_blank;

import com.example.lmsbackend.enums.MatchStrategy;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "quiz_fill_in_blank_option")
@Getter
@Setter
@FieldNameConstants
public class FillInBlankOptionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "expected_answer", nullable = false)
    private String expectedAnswer;

    @Column(name = "match_strategy", nullable = false)
    @Enumerated(EnumType.STRING)
    private MatchStrategy matchStrategy;

    @Column(name = "hint")
    private String hint;

    @Column(name = "sort_index")
    private Integer order;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private FillInBlankQuestionEntity question;
}