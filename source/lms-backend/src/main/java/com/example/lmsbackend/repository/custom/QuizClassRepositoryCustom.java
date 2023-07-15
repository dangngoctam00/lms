package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.dto.response.course.QuizDto;
import com.example.lmsbackend.enums.AccountTypeEnum;

import java.util.List;
import java.util.Optional;

public interface QuizClassRepositoryCustom {
    Optional<QuizClassEntity> findFetchById(Long id, String properties);

    List<QuizDto> findQuizzesByIdIn(List<Long> idList);

    List<QuizClassEntity> findByClassAndRole(Long classId, AccountTypeEnum accountType);
}
