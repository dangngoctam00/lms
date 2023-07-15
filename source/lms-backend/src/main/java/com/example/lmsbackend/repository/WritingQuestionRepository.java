package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.writing_question.WritingQuestionEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WritingQuestionRepository extends JpaRepository<WritingQuestionEntity, Long> {

    @EntityGraph("writing-question")
    Optional<WritingQuestionEntity> findById(Long id);
}
