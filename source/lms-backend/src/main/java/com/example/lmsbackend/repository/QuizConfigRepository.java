package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.QuizConfigEntity;
import com.example.lmsbackend.repository.custom.QuizConfigRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface QuizConfigRepository extends JpaRepository<QuizConfigEntity, Long>,
        QuerydslPredicateExecutor<QuizConfigEntity>, QuizConfigRepositoryCustom {

}
