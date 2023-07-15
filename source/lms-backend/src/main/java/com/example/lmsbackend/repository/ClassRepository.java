package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.StaffEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.ClassLearningContentEntity;
import com.example.lmsbackend.dto.classes.ClassDto;
import com.example.lmsbackend.repository.custom.ClassRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<ClassEntity, Long>,
        QuerydslPredicateExecutor<ClassEntity>, ClassRepositoryCustom {

    @Query("SELECT new com.example.lmsbackend.dto.classes.ClassDto(c.id, c.name) FROM ClassEntity c " +
            "WHERE c.course.id = ?1 AND c.status = 'ONGOING'")
    List<ClassDto> findOngoingClassByCourse(Long id);

    @Query("SELECT c.id FROM ClassEntity c " +
            "WHERE c.course.id = ?1 AND (c.status = 'ONGOING' OR c.status = 'CREATED')")
    List<Long> findCreatedOrOngoingIdClassByCourse(Long id);

    @Query("SELECT COALESCE(COUNT(c), 0) FROM ClassEntity c " +
            "WHERE c.course.id = ?1 AND (c.status = 'ONGOING') OR c.status = 'CREATED'")
    Long countCreatedOngoingClassByCourse(Long id);

    Boolean existsByCode(String code);

    @Query("SELECT content FROM ClassLearningContentEntity content " +
            "LEFT JOIN FETCH content.chapters " +
            "LEFT JOIN FETCH content.classEntity " +
            "WHERE content.classEntity.id = ?1")
    Optional<ClassLearningContentEntity> findLearningContentById(Long id);

    @Query("SELECT c FROM ClassEntity c LEFT JOIN FETCH c.sessions WHERE c.id = ?1")
    Optional<ClassEntity> findFetchSessionsById(Long classId);

    @Query("SELECT c.id FROM ClassEntity c INNER JOIN ClassLearningContentEntity l ON l.classEntity.id = c.id " +
            "INNER JOIN ChapterClassEntity chapter ON chapter.learningContent.id = l.id WHERE chapter.id = ?1")
    Optional<Long> findClassIdByChapter(Long chapterId);

    @Query("SELECT c.id FROM ClassEntity c WHERE c.course.id = ?1")
    List<Long> findClassIdByCourseId(Long courseId);

    boolean existsById(Long id);

    @Query("SELECT s FROM StaffEntity s WHERE s.id NOT IN (" +
            "SELECT t.id.teacherId FROM ClassTeacherEntity t WHERE t.id.classId = ?1)")
    List<StaffEntity> findTeacherCandidatesByClassId(Long classId);

    @Query("SELECT c.name FROM ClassEntity c WHERE c.id = ?1")
    String getClassName(Long id);

    @Query("SELECT c.daysOfWeek FROM ClassEntity c WHERE c.id = ?1")
    String getScheduler(Long classId);
}
