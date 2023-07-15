package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank_with_choices.FillInBlankMultiChoiceQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FillInBlankMultiChoiceQuestionRepository extends JpaRepository<FillInBlankMultiChoiceQuestionEntity, Long> {

    @EntityGraph("fill-in-blank-multi-choice-blanks")
    Optional<FillInBlankMultiChoiceQuestionEntity> findById(Long aLong);
}
