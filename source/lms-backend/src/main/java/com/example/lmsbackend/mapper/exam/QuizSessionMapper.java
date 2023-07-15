package com.example.lmsbackend.mapper.exam;

import com.example.lmsbackend.domain.exam.QuizSessionEntity;
import com.example.lmsbackend.dto.exam.SessionResultDto;
import org.apache.commons.lang3.BooleanUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.temporal.ChronoUnit;

@Mapper(componentModel = "spring", uses = {
        AuditMapper.class
})
public interface QuizSessionMapper {

    @Mapping(target = "sessionId", source = "entity.id")
    @Mapping(target = "score", source = "entity.result.score")
    @Mapping(target = "finalVerdict", source = "entity.result.finalVerdict")
    @Mapping(target = "timeTaken", expression = "java(mapTimeTaken(entity))")
    @Mapping(target = "user", source = "student")
    SessionResultDto mapToSessionResultOverview(QuizSessionEntity entity);

    @Mapping(target = "sessionId", source = "entity.id")
    @Mapping(target = "score", ignore = true)
    @Mapping(target = "finalVerdict", ignore = true)
    @Mapping(target = "timeTaken", expression = "java(mapTimeTaken(entity))")
    @Mapping(target = "user", source = "student")
    SessionResultDto mapToSessionHideResult(QuizSessionEntity entity);

    default Long mapTimeTaken(QuizSessionEntity entity) {
        if (entity.getSubmittedAt() == null) {
            return null;
        }
        long between = ChronoUnit.SECONDS.between(entity.getStartedAt(), entity.getSubmittedAt());
        Long timeLimit = entity.getQuiz().getConfig().getTimeLimit();
        if (BooleanUtils.isTrue(entity.getQuiz().getConfig().getHaveTimeLimit()) && between > timeLimit * 60) {
            return timeLimit * 60;
        }
        return between;
    }
}
