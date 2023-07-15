package com.example.lmsbackend.domain.exam.base_question;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Table(name = "group_question_has_question")
@Getter
@Setter
@FieldNameConstants
public class QuestionInGroupEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private QuestionEntity question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private GroupQuestionEntity group;

    @Column(name = "sort_index")
    private Integer order;

    public void setGroup(GroupQuestionEntity group) {
        this.group = group;
        group.getQuestions().add(this);
    }
}