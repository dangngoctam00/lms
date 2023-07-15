package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.UnitClassEntity;
import com.example.lmsbackend.repository.custom.UnitClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UnitClassRepository extends JpaRepository<UnitClassEntity, Long>,
        QuerydslPredicateExecutor<UnitClassEntity>, UnitClassRepositoryCustom {

    @Query("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.sessions WHERE u.id = ?1")
    Optional<UnitClassEntity> findFetchSessionsById(Long id);

    @Query("SELECT c.id.activityId FROM ChapterActivityClassEntity c WHERE c.id.activityType = ?2 AND c.chapter.id IN " +
            "(SELECT chapter.id FROM ChapterClassEntity chapter WHERE chapter.learningContent.id = ?1)")
    List<Long> findActionIdByClassAndType(Long classId, String type);

    @Query("SELECT q.id FROM UnitClassEntity q WHERE q.unitCourse.id = ?1")
    List<Long> findUnitsIdByUnitCourseId(Long unitCourseId);

    @Query("SELECT q.id FROM UnitClassEntity q WHERE q.unitCourse.id = ?1 AND q.classEntity.id = ?2")
    Optional<Long> findUnitsIdByUnitCourseIdAndClassId(Long unitCourseId, Long classId);

    @Query("SELECT q.id FROM UnitClassEntity q WHERE q.id IN ?1 AND q.unitCourse IS NULL")
    List<Long> getUnitClassIdNotInCourseIn(List<Long> id);

    @Query("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.textbooks WHERE u.id IN ?1")
    Set<UnitClassEntity> findFetchTextbookUnitsDtoByIdIn(List<Long> idList);

    @Query("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.textbooks WHERE u.id IN ?1 AND u.state = 'PUBLIC'")
    Set<UnitClassEntity> findPublishedFetchTextbookUnitsDtoByIdIn(List<Long> idList);

    @Query("SELECT u.title FROM UnitClassEntity u WHERE u.id = ?1")
    String getNameById(Long id);

    @Query("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.textbooks WHERE u.classEntity.id = ?1 ORDER BY u.id ASC")
    Set<UnitClassEntity> findFetchByClassId(Long classId);

    @Query("SELECT u FROM UnitClassEntity u LEFT JOIN FETCH u.textbooks WHERE u.classEntity.id = ?1 AND u.state = 'PUBLIC' ORDER BY u.id ASC")
    Set<UnitClassEntity> findPublishedFetchByClassId(Long classId);
}
