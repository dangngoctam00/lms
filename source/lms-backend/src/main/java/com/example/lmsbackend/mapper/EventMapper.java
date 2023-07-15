package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.event.EventEntity;
import com.example.lmsbackend.domain.event.EventType;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.enums.ActivityState;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;

import static com.example.lmsbackend.constant.AppConstant.CLASS_SESSION_DESCRIPTION_TEMPLATE;
import static com.example.lmsbackend.constant.AppConstant.TEACHER_TEMPLATE;

@Mapper(componentModel = "spring")
public interface EventMapper {

    default void mapQuizEvent(QuizClassEntity quiz, EventEntity event) {
        event.getId().setEventId(quiz.getId());
        event.getId().setEventType(EventType.QUIZ);
        event.setSummary(quiz.getTitle());
        event.setDescription(quiz.getDescription());
        event.setStartedAt(quiz.getConfig().getStartedAt());
        event.setEndedAt(quiz.getConfig().getValidBefore());
        event.setHidden(quiz.getState() == ActivityState.PRIVATE);
    }

    default void mapClassSessionEvent(ClassSessionEntity session, EventEntity event) {
        event.setSummary(session.getClassEntity().getName());
        var teacher = TEACHER_TEMPLATE;
        if (session.getTeacher() != null) {
            teacher = String.format(teacher, session.getTeacher().getLastName(), session.getTeacher().getFirstName());
        } else {
            teacher = String.format(teacher, "__", "");
        }
        event.setDescription(String.format(CLASS_SESSION_DESCRIPTION_TEMPLATE, StringUtils.isBlank(session.getRoom()) ? "__" : session.getRoom(), teacher));
    }
}
