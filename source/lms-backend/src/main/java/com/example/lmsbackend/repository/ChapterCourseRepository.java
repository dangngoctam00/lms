package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.coursemodel.ChapterCourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ChapterCourseRepository extends JpaRepository<ChapterCourseEntity, Long> {
    void deleteAllByIdIn(Set<Long> ids);

    @Query("SELECT MAX(c.order) FROM ChapterCourseEntity c WHERE c.courseContent.id = ?1")
    Optional<Integer> findMaxSortIndex(Long learningContentId);

    @Query("SELECT c FROM ChapterCourseEntity c LEFT JOIN FETCH c.actions WHERE c.id = ?1")
    Optional<ChapterCourseEntity> findFetchActionsById(Long id);

    @Query("SELECT DISTINCT c FROM ChapterCourseEntity c LEFT JOIN FETCH c.actions WHERE c.courseContent.id = ?1 " +
            "ORDER BY c.order ASC, c.id ASC")
    List<ChapterCourseEntity> findFetchActionsByLearningContent(Long id);

    @Query("SELECT c FROM ChapterCourseEntity c WHERE c.courseContent.id = ?1 AND c.id <> ?2 AND c.order >= ?3 ")
    List<ChapterCourseEntity> findByCourseAndHigherEqualOrderExcept(Long courseId, Long chapterId, Integer toSortIndex);

    @Query("SELECT c FROM ChapterCourseEntity c WHERE c.courseContent.id = ?1 AND c.order > ?2 AND c.order <= ?3 ")
    List<ChapterCourseEntity> findByCourseInRange(Long courseId, Integer fromSortIndex, Integer toSortIndex);
}
