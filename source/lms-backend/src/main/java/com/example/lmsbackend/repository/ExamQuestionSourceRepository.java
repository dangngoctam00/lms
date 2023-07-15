package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.ExamQuestionSourceEntity;
import com.example.lmsbackend.repository.custom.ExamQuestionSourceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ExamQuestionSourceRepository extends JpaRepository<ExamQuestionSourceEntity, Long>,
        QuerydslPredicateExecutor<ExamQuestionSourceEntity>, ExamQuestionSourceRepositoryCustom {

    @Query("SELECT e.question.id FROM ExamQuestionSourceEntity e WHERE e.exam.id = ?1")
    List<Long> findQuestionsIdInExam(Long examId);
}