package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.coursemodel.CourseEntity;
import com.example.lmsbackend.domain.coursemodel.CourseLearningContentEntity;
import com.example.lmsbackend.repository.custom.CourseRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<CourseEntity, Long>, QuerydslPredicateExecutor<CourseEntity>,
        CourseRepositoryCustom {

    @EntityGraph("course-program")
    List<CourseEntity> findCourseEntityByIdIn(List<Long> coursesId);

    Optional<CourseEntity> findByCode(String code);

    @Query("SELECT content FROM CourseLearningContentEntity content " +
            "LEFT JOIN FETCH content.chapters " +
            "LEFT JOIN FETCH content.course " +
            "WHERE content.course.id = ?1")
    Optional<CourseLearningContentEntity> findLearningContentById(Long id);

    @Query("SELECT MAX(indexTable.order) FROM ChapterActivityCourseEntity indexTable " +
            "WHERE indexTable.chapter.id IN (" +
            "SELECT chapter.id FROM ChapterCourseEntity chapter WHERE chapter.courseContent.id = ?1)")
    Optional<Integer> findActionMaxSortIndex(Long id);


    @Query("SELECT MAX(indexTable.order) FROM ChapterActivityCourseEntity indexTable " +
            "WHERE indexTable.id.chapterId = ?1")
    Optional<Integer> findActionMaxSortIndexInChapter(Long chapterId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CourseEntity c WHERE c.id = ?1 AND c.createdBy = ?2")
    Boolean isCreatedByUser(Long courseId, String username);
}
