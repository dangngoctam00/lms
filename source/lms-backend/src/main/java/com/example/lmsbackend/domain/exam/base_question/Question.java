package com.example.lmsbackend.domain.exam.base_question;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


@Getter
@Setter
@MappedSuperclass
@FieldNameConstants
public class Question {
    @Id
    private Long id;
}