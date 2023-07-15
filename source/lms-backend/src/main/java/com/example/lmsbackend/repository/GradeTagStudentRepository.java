package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.exam.GradeTagScope;
import com.example.lmsbackend.domain.exam.GradeTagStudentEntity;
import com.example.lmsbackend.domain.exam.GradeTagStudentKey;
import com.example.lmsbackend.dto.classes.GradeTagStudentCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GradeTagStudentRepository extends JpaRepository<GradeTagStudentEntity, GradeTagStudentKey> {

    @Query("SELECT g FROM GradeTagStudentEntity g WHERE g.tag.title = ?1 AND g.tag.scope = ?2 AND g.tag.scopeId = ?3")
    List<GradeTagStudentEntity> findByTagAndScope(String title, GradeTagScope scope, Long scopeId);

    @Query("SELECT g FROM GradeTagStudentEntity g WHERE g.tag.title = ?1 AND g.tag.scope = ?2 AND g.tag.scopeId = ?3 AND g.student.id = ?4")
    Optional<GradeTagStudentEntity> findByTagScopeAndStudent(String title, GradeTagScope scope, Long scopeId, Long studentId);

    List<GradeTagStudentEntity> findAllByTag_Id(Long tagId);
    List<GradeTagStudentEntity> findAllByStudent_IdInAndTag_Id(List<Long> studentIds,Long tagId);
    List<GradeTagStudentEntity> findAllByTag_IdAndStudent_Id(Long tagId, Long studentId);


    List<GradeTagStudentEntity> findAllByTag_IdIn(List<Long> tagsId);

    @Query("SELECT new com.example.lmsbackend.dto.classes.GradeTagStudentCount(c.tag.id, COUNT(c.tag.id)) FROM GradeTagStudentEntity c WHERE c.tag.id IN ?1 GROUP BY c.tag.id")
    List<GradeTagStudentCount> countByTag(List<Long> idList);

    @Modifying
    @Query("DELETE FROM GradeTagStudentEntity entity where entity.student.id = :studentId")
    void deleteAllByStudentId(@Param("studentId") Long studentId);

    @Modifying
    @Query("DELETE FROM GradeTagStudentEntity entity where entity.tag.id = :tagId")
    void deleteAllByTagId(@Param("tagId") long tagId);
}
