package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.resource.TextbookEntity;
import com.example.lmsbackend.repository.custom.TextbookRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Set;

public interface TextbookRepository extends JpaRepository<TextbookEntity, Long>, QuerydslPredicateExecutor<TextbookEntity>,
        TextbookRepositoryCustom {

    List<TextbookEntity> findByIdIn(Set<Long> idList);

    @Query("SELECT t FROM TextbookEntity t LEFT JOIN fetch t.units WHERE t.id IN ?1")
    List<TextbookEntity> findFetchUnitCourse(Set<Long> idList);

    @Query("SELECT t FROM TextbookEntity t LEFT JOIN fetch t.exams WHERE t.id IN ?1")
    Set<TextbookEntity> findFetchExams(Set<Long> idList);

    @Query("SELECT t FROM TextbookEntity t LEFT JOIN fetch t.unitsClasses WHERE t.id IN ?1")
    List<TextbookEntity> findFetchUnitClass(Set<Long> idList);

    @Query("SELECT t FROM TextbookEntity t " +
            "WHERE t NOT IN (SELECT tc.textbook.id FROM CourseTextbookEntity tc WHERE tc.course.id = ?1) " +
            "AND lower(t.name) LIKE %?2%")
    List<TextbookEntity> findNotInCourse(Long courseId, String keyword);

    @Query("SELECT t FROM TextbookEntity t " +
            "WHERE t IN (SELECT tc.textbook.id FROM CourseTextbookEntity tc WHERE tc.course.id = ?1) " +
            "AND lower(t.name) LIKE %?2%")
    List<TextbookEntity> findInCourse(Long courseId, String keyword);

    @Query("SELECT t FROM TextbookEntity t " +
            "WHERE t IN (SELECT tc.textbook.id FROM ClassTextbookEntity tc WHERE tc.classEntity.id = ?1) " +
            "AND lower(t.name) LIKE %?2%")
    List<TextbookEntity> findInClass(Long classId, String keyword);

    @Query("SELECT u.name FROM TextbookEntity u WHERE u.id = ?1")
    String getNameById(Long id);

    @Query("SELECT c.textbook FROM CourseTextbookEntity c WHERE c.course.id = ?1")
    List<TextbookEntity> findByCourse(Long courseId);
}
