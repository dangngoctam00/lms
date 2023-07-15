package com.example.lmsbackend.domain.exam.fill_in_blank_dnd_question;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "quiz_fill_in_blank_drag_and_drop_answer")
@Getter
@Setter
@FieldNameConstants
public class FillInBlankDragAndDropAnswerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private FillInBlankDragAndDropQuestionEntity question;

    @Column(name = "content")
    private String content;

    @Column(name = "key")
    private Integer key;

    @Column(name = "sort_index")
    private Long order;
}