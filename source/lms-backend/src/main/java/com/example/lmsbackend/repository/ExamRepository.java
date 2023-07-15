package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.ExamEntity;
import com.example.lmsbackend.repository.custom.ExamRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ExamRepository extends JpaRepository<ExamEntity, Long>,
        QuerydslPredicateExecutor<ExamEntity>, ExamRepositoryCustom {

    @EntityGraph("exam-questionSources")
    Optional<ExamEntity> findById(Long id);

    @Query("SELECT e FROM ExamEntity e LEFT JOIN FETCH e.quizzesCourse WHERE e.id = ?1")
    Optional<ExamEntity> findFetchQuizCourseById(Long id);

    @Query("SELECT e FROM ExamEntity e LEFT JOIN FETCH e.quizzes WHERE e.id = ?1")
    Optional<ExamEntity> findFetchQuizClassById(Long id);

    @Query("SELECT CASE WHEN COUNT(q) > 0 THEN true " +
            "ELSE false END FROM QuestionEntity q " +
            "WHERE q.type = 'WRITING' AND q.id IN (" +
            "SELECT eq.question FROM ExamQuestionSourceEntity eq WHERE eq.exam.id = ?1)")
    boolean isHavingWritingQuestion(Long examId);

    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN true ELSE false END " +
            "FROM ExamEntity e WHERE e.id = ?1 AND e.createdBy = ?2")
    Boolean isCreatedByUser(Long examId, String username);

    boolean existsById(Long id);

    @Query("SELECT COALESCE(SUM(COALESCE(q.point, 0)), 0) FROM QuestionEntity q WHERE q.id IN (" +
            "SELECT qq.question.id FROM ExamQuestionSourceEntity qq WHERE qq.exam.id = ?1)")
    Long totalGradeOfExam(Long examId);

    @Query("SELECT u.title FROM ExamEntity u WHERE u.id = ?1")
    String getNameById(Long id);
}