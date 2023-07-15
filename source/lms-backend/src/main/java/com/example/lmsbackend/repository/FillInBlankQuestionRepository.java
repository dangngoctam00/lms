package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.fill_in_blank.FillInBlankQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FillInBlankQuestionRepository extends JpaRepository<FillInBlankQuestionEntity, Long> {

    @EntityGraph("fill-in-blank-blanks")
    Optional<FillInBlankQuestionEntity> findById(Long id);
}
