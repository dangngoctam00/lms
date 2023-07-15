package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ChapterActivityClassEntity;
import com.example.lmsbackend.domain.classmodel.ChapterActivityClassKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChapterActivityClassRepository extends JpaRepository<ChapterActivityClassEntity, ChapterActivityClassKey> {


    @Query("SELECT COALESCE(MAX(c.order), 0) FROM ChapterActivityClassEntity c WHERE c.id.chapterId = ?1")
    Integer findMaxSortIndexByChapter(Long id);


    @Query("SELECT c FROM ChapterActivityClassEntity c WHERE c.id.activityId = ?1 AND c.id.chapterId = ?2 AND c.order = ?3")
    Optional<ChapterActivityClassEntity> findActionByIdSortIndexAndChapter(Long id, Long chapterId, Integer sortIndex);

    @Query("SELECT c FROM ChapterActivityClassEntity c WHERE c.chapter.id = ?1 AND c.order = ?2")
    Optional<ChapterActivityClassEntity> findActionBySortIndexAndChapter(Long chapterId, Integer sortIndex);

    @Query("SELECT c FROM ChapterActivityClassEntity c " +
            "WHERE c.id.chapterId = ?1 AND c.order >= ?2 AND c.order > -1")
    List<ChapterActivityClassEntity> findActivitiesByChapterAndHigherOrderExcept(Long chapterId, Integer toSortIndex);

    @Query("SELECT new com.example.lmsbackend.domain.classmodel.ChapterActivityClassKey(c.id.activityId, c.id.activityType) FROM ChapterActivityClassEntity c " +
            "WHERE c.chapter.id = ?1")
    List<ChapterActivityClassKey> findChapterActionByChapter(Long chapterId);

    @Query("SELECT c.id.activityId " +
            "FROM ChapterActivityClassEntity c " +
            "WHERE c.chapter.learningContent.id = ?1 AND c.id.activityType = ?2")
    List<Long> findActionsByClassAndType(Long classId, String type);

    @Query("SELECT c.chapter.id FROM ChapterActivityClassEntity c WHERE c.id.activityType = ?1 AND c.id.activityId = ?2")
    Optional<Long> findChapterIdByAction(String type, Long id);

    @Query("SELECT c.chapter.id FROM ChapterActivityClassEntity c WHERE c.id.activityType = 'QUIZ' AND c.id.activityId = (" +
            "SELECT q.quiz.id FROM QuizSessionEntity q WHERE q.id = ?1)")
    Optional<Long> findChapterIdByQuizSession(UUID sessionId);

    @Query("SELECT c.chapter.learningContent.id FROM ChapterActivityClassEntity c WHERE c.id.activityId = ?1 AND c.id.activityType = ?2")
    Long findClassIdByActivity(Long id, String type);

    @Query("SELECT c.id.activityId FROM ChapterActivityClassEntity c WHERE c.id.chapterId IN ?1 AND c.id.activityType = ?2")
    List<Long> findActivityIdByChapterIdIn(List<Long> chapterIdList, String type);
}
