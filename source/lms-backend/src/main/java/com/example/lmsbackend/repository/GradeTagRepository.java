package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.GradeTag;
import com.example.lmsbackend.domain.exam.GradeTagScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeTagRepository extends JpaRepository<GradeTag, Long> {
    @Query("select gradeTag from GradeTag gradeTag where gradeTag.scope = :scope and gradeTag.scopeId = :scopeId order by gradeTag.gradedAt desc, gradeTag.updatedAt desc")
    List<GradeTag> findAllByScopeAndScopeId(@Param("scope") GradeTagScope scope, @Param("scopeId") Long scopeId);

    @Query("select gradeTag from GradeTag gradeTag where gradeTag.scope = :scope and gradeTag.scopeId = :scopeId")
    List<GradeTag> getAllTagsInScope(@Param("scope") GradeTagScope scope, @Param("scopeId") Long scopeId);

    @Query("select e from GradeTag e where (e.scope = 'CLASS' and e.scopeId = :classId) " +
            "or (e.scope = 'COURSE' and e.scopeId = :courseId) " +
            "order by e.scope desc, e.id desc")
    List<GradeTag> getAllGradeTagInClass(@Param("classId") Long classId, @Param("courseId") Long courseId);

    @Query("select e from GradeTag e where e.isPublic = true and ((e.scope = 'CLASS' and e.scopeId = :classId) or (e.scope = 'COURSE' and e.scopeId = :courseId))")
    List<GradeTag> getAllGradeTagShowedInClass(@Param("classId") Long classId, @Param("courseId") Long courseId);

    @Query("SELECT g FROM GradeTag g WHERE g.title = ?1 AND g.scope = ?2 AND g.scopeId = ?3")
    Optional<GradeTag> findTagByTitleAndScope(String title, GradeTagScope scope, Long scopeId);

    int deleteByScopeAndScopeId(GradeTagScope scope, Long scopeId);

    @Query("SELECT case when count(e) > 0 then true else false end FROM ClassTeacherEntity e " +
            "where e.teacher.id = :teacherId and e.classEntity.id in (select gradeTag.scopeId from GradeTag gradeTag where gradeTag.id = :tagId)")
    boolean isTeachingClassByGradeTag(@Param("tagId") long tagId, @Param("teacherId") Long teacherId);

    @Query("SELECT case when count(e) > 0 then true else false end FROM ClassStudentEntity e " +
            "where e.student.id = :studentId and e.classEntity.id in (select gradeTag.scopeId from GradeTag gradeTag where gradeTag.id = :tagId)")
    boolean isLearningByGradeTag(@Param("tagId") long tagId, @Param("studentId") Long studentId);

    @Query("SELECT g FROM GradeTag g WHERE g.id IN ?1 ORDER BY g.id ASC")
    List<GradeTag> findAllByIdInOrderById(List<Long> idList);
}
