package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.multi_choice.MultiChoiceQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MultiChoiceQuestionRepository extends JpaRepository<MultiChoiceQuestionEntity, Long> {

    @EntityGraph("multi-choice-options")
    Optional<MultiChoiceQuestionEntity> findById(Long id);
}
