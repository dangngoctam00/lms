package com.example.lmsbackend.domain.exam;

import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "quiz_session_flag")
@FieldNameConstants
public class QuizSessionFlagEntity {

    @EmbeddedId
    private QuizSessionFlagPK id = new QuizSessionFlagPK();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private QuizSessionEntity session;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("questionId")
    @JoinColumn(name = "question_id")
    private QuestionEntity question;
}