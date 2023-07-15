package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.QuizClassEntity;
import com.example.lmsbackend.domain.coursemodel.QuizCourseEntity;
import com.example.lmsbackend.repository.custom.QuizClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface QuizClassRepository extends JpaRepository<QuizClassEntity, Long>,
        QuerydslPredicateExecutor<QuizClassEntity>, QuizClassRepositoryCustom {

    @Query("SELECT q FROM QuizClassEntity q LEFT JOIN FETCH q.config WHERE q.id = ?1")
    Optional<QuizClassEntity> findFetchConfigById(Long id);

    @Query("SELECT q FROM QuizClassEntity q LEFT JOIN FETCH q.exam WHERE q.id = ?1")
    Optional<QuizClassEntity> findFetchExamById(Long id);

    @Query("SELECT q.id FROM QuizClassEntity q WHERE q.quizCourse.id = ?1")
    List<Long> findQuizzesIdByQuizCourseId(Long quizCourseId);

    @Query("SELECT q FROM QuizClassEntity q WHERE q.quizCourse.id = ?1")
    List<QuizClassEntity> findByQuizCourseIdAndClassIdIn(Long quizCourseId, List<Long> classId);

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true ELSE false END FROM QuizClassEntity q WHERE q.id = ?1 AND q.createdBy = ?2")
    Boolean izQuizCreatedByUser(Long quizId, String username);

    @Query("SELECT COUNT(q) FROM QuizClassEntity q WHERE q.exam.id = ?1")
    long countQuizByExam(Long examId);

    @Query("SELECT q.exam.id FROM QuizClassEntity q WHERE q.id = ?1")
    Long getExamIdOfQuiz(Long quizId);

    @Query("SELECT q.id FROM QuizClassEntity q WHERE q.id IN ?1 AND q.quizCourse IS NULL")
    List<Long> getQuizClassIdNotInCourseIn(List<Long> idList);

    @Query("SELECT q.id FROM QuizClassEntity q WHERE q.quizCourse.id = ?1 AND q.classEntity.id = ?2")
    Optional<Long> findIdByQuizCourseIdAndClassId(Long activityCourseId, Long classId);

    @Query("SELECT u FROM QuizClassEntity u WHERE u.id IN ?1")
    List<QuizClassEntity> findQuizByIdIn(List<Long> idList);

    @Query("SELECT u FROM QuizClassEntity u WHERE u.id IN ?1 AND u.state = 'PUBLISHED'")
    List<QuizClassEntity> findPublishedQuizByIdIn(List<Long> idList);

    @Query("SELECT u.title FROM QuizClassEntity u WHERE u.id = ?1")
    String getNameById(Long id);
}
