package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.ChapterClassEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterClassRepository extends JpaRepository<ChapterClassEntity, Long> {

    @Query("SELECT DISTINCT c FROM ChapterClassEntity c LEFT JOIN FETCH c.actions " +
            "WHERE c.learningContent.id = ?1 " +
            "ORDER BY c.order ASC, c.id ASC")
    List<ChapterClassEntity> findFetchActionsByLearningContent(Long id);

    @Query("SELECT c FROM ChapterClassEntity c LEFT JOIN FETCH c.actions WHERE c.id = ?1")
    Optional<ChapterClassEntity> findFetchActionsById(Long id);

    @Query("SELECT c FROM ChapterClassEntity c LEFT JOIN FETCH c.actions WHERE c.learningContent.id = ?1 AND c.chapterCourse.id = ?2")
    Optional<ChapterClassEntity> findFetchActivitiesByClassAndChapterCourse(Long classId, Long chapterCourseId);

    @Query("SELECT c.id FROM ChapterClassEntity c WHERE c.learningContent.id = ?1 AND c.chapterCourse.id = ?2")
    Optional<Long> findIdClassAndChapterCourse(Long classId, Long chapterCourseId);


    boolean existsById(Long id);

    @Query("SELECT c FROM ChapterClassEntity c WHERE c.learningContent.id = ?1 AND c.order > ?2 AND c.order <= ?3 ")
    List<ChapterClassEntity> findByClassInRange(Long classId, Integer fromSortIndex, Integer toSortIndex);

    @Query("SELECT MAX(c.order) FROM ChapterClassEntity c WHERE c.learningContent.id = ?1")
    Optional<Integer> findMaxCurrentSortIndex(Long learningContentId);

    @Query("SELECT c FROM ChapterClassEntity c " +
            "LEFT JOIN FETCH c.learningContent " +
            "WHERE c.chapterCourse.id = ?1 AND c.learningContent.id IN ?2")
    List<ChapterClassEntity> findByChapterCourseAndClassIn(Long chapterCourseId, List<Long> classList);

    @Query("SELECT c.id FROM ChapterClassEntity c WHERE c.learningContent.id IN ?1")
    List<Long> findByClassIn(List<Long> classList);

    @Query("SELECT c FROM ChapterClassEntity c WHERE c.chapterCourse.id = ?1 AND c.learningContent.classEntity.status <> 'ENDED'")
    List<ChapterClassEntity> findByChapterCourseIdNotInEndedClass(Long chapterCourseId);

    @Query("SELECT c FROM ChapterClassEntity c WHERE c.learningContent.id = ?1 AND c.chapterCourse.id = ?2")
    Optional<ChapterClassEntity> findByClassAndChapterCourse(Long classId, Long fromChapterCourseId);
}
