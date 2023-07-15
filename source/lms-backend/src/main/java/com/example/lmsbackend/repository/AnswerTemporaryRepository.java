package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.AnswerTemporaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerTemporaryRepository extends JpaRepository<AnswerTemporaryEntity, Long> {
}
