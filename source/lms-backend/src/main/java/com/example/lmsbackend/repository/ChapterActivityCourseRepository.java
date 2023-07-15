package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.course.ChapterActivityCourseEntity;
import com.example.lmsbackend.domain.course.ChapterActivityCourseKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterActivityCourseRepository extends JpaRepository<ChapterActivityCourseEntity, ChapterActivityCourseKey> {

    @Query("SELECT c FROM ChapterActivityCourseEntity c " +
            "WHERE c.chapter.id IN (" +
            "SELECT chapter.id FROM ChapterCourseEntity chapter WHERE chapter.courseContent.id = ?1)")
    List<ChapterActivityCourseEntity> findChapterActionByCourse(Long courseId);

    @Query("SELECT c FROM ChapterActivityCourseEntity c " +
            "WHERE c.chapter.id IN (" +
            "SELECT chapter.id FROM ChapterCourseEntity chapter WHERE chapter.courseContent.id = ?1) " +
            "AND c.id.activityType = ?2")
    List<ChapterActivityCourseEntity> findChapterActionByCourse(Long courseId, String type);

    @Query("SELECT new com.example.lmsbackend.domain.course.ChapterActivityCourseKey(c.id.activityId, c.id.activityType) FROM ChapterActivityCourseEntity c " +
            "WHERE c.chapter.id = ?1")
    List<ChapterActivityCourseKey> findChapterActionByChapter(Long chapterId);

    @Query("SELECT c FROM ChapterActivityCourseEntity c WHERE c.chapter.id IN " +
            "(SELECT chapter.id FROM ChapterCourseEntity chapter WHERE chapter.courseContent.id = ?1) " +
            "AND c.order > ?4 AND c.id.activityId = ?2 AND c.order = ?3")
    List<ChapterActivityCourseEntity> findChapterActionByCourseAndHigherOrderThanExcept(Long courseId, Long actionId, Integer fromSortIndex, Integer toSortIndex);

    @Query("SELECT c FROM ChapterActivityCourseEntity c " +
            "WHERE c.id.chapterId = ?1 AND c.order > ?2")
    List<ChapterActivityCourseEntity> findActionByChapterAndHigherOrder(Long chapterId, Integer toSortIndex);

    @Query("SELECT c FROM ChapterActivityCourseEntity c " +
            "WHERE c.id.chapterId = ?1 AND c.order >= ?2 AND c.order > -1")
    List<ChapterActivityCourseEntity> findActionByChapterAndHigherOrderExcept(Long chapterId, Integer toSortIndex);


    @Query("SELECT MIN(c.order) FROM ChapterActivityCourseEntity c WHERE c.id.chapterId = ?1")
    Integer findMinSortIndexByChapter(Long chapterId);

    @Query("SELECT COALESCE(MAX(c.order), 0) FROM ChapterActivityCourseEntity c WHERE c.id.chapterId = ?1 AND c.order <> -1")
    Integer findMaxSortIndexByChapter(Long chapterId);

    @Query("SELECT c FROM ChapterActivityCourseEntity c WHERE c.chapter.id = ?1 " +
            "AND c.order = (SELECT COALESCE(MAX(w.order), 0) FROM ChapterActivityCourseEntity w WHERE w.chapter.id = ?1)")
    Optional<ChapterActivityCourseEntity> findMaxActivityByChapter(Long chapterId);


    @Query("SELECT c FROM ChapterActivityCourseEntity c WHERE c.id.activityId = ?1 AND c.id.chapterId = ?2 AND c.order = ?3")
    Optional<ChapterActivityCourseEntity> findActionByIdSortIndexAndChapter(Long id, Long chapterId, Integer sortIndex);
}
