package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.coursemodel.QuizCourseEntity;
import com.example.lmsbackend.dto.response.course.QuizDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface QuizCourseRepository extends JpaRepository<QuizCourseEntity, Long> {

    @Query("SELECT new com.example.lmsbackend.dto.response.course.QuizDto(q.id, q.title) FROM QuizCourseEntity q WHERE q.id IN ?1")
    List<QuizDto> findQuizzesDtoByIdIn(List<Long> idList);

    @Query("SELECT q FROM QuizCourseEntity q LEFT JOIN FETCH q.tag WHERE q.id = ?1")
    Optional<QuizCourseEntity> findFetchTagById(Long quizId);

    @Query("SELECT COUNT(q) FROM QuizCourseEntity q WHERE q.exam.id = ?1")
    long countQuizByExam(Long examId);

    @Query("SELECT u FROM QuizCourseEntity u WHERE u.id IN ?1")
    List<QuizCourseEntity> findQuizByIdIn(List<Long> idList);

    @Query("SELECT u FROM QuizCourseEntity u WHERE u.course.id = ?1")
    List<QuizCourseEntity> findByCourseId(Long courseId);

    @Query("SELECT u.title FROM QuizCourseEntity u WHERE u.id = ?1")
    String getNameById(Long id);
}
