package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.discussion.PostEntity;
import com.example.lmsbackend.repository.custom.PostRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface PostRepository extends JpaRepository<PostEntity, Long>,
        QuerydslPredicateExecutor<PostEntity>, PostRepositoryCustom {

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassStudentEntity c WHERE c.classEntity.id = (SELECT p.classEntity.id FROM PostEntity p WHERE p.id = ?2) " +
            "AND c.student.id = ?1")
    Boolean isStudentInClassByPost(Long studentId, Long postId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c WHERE c.classEntity.id = (SELECT p.classEntity.id FROM PostEntity p WHERE p.id = ?2) " +
            "AND c.teacher.id = ?1")
    Boolean isTeacherInClassByPost(Long teacherId, Long postId);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM PostEntity c WHERE c.id = ?1 AND c.createdBy.id = ?2")
    Boolean isPostOwnedByUser(Long postId, Long userId);
}
