package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.base_question.QuestionSourceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionSourceRepository extends JpaRepository<QuestionSourceEntity, Long> {
}