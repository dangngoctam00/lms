package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.base_question.QuestionEntity;
import com.example.lmsbackend.repository.custom.QuestionRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Long>,
        QuerydslPredicateExecutor<QuestionEntity>, QuestionRepositoryCustom {

    @EntityGraph(attributePaths = {
            "questionSource",
            "writingQuestion",
            "fillInBlankQuestion",
            "multiChoiceQuestion",
            "fillInBlankMultiChoiceQuestion",
            "fillInBlankDragAndDropQuestion",
            "groupQuestion"})
    List<QuestionEntity> findByIdIn(List<Long> idList);

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END FROM QuestionEntity q WHERE (q.point IS NULL OR q.point = 0) AND q.id IN " +
            "(SELECT qq.question.id FROM ExamQuestionSourceEntity qq WHERE qq.exam.id = ?1)")
    boolean isExistingUnPointedQuestion(Long examId);
}