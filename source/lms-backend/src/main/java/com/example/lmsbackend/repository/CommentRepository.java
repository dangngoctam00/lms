package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.discussion.CommentEntity;
import com.example.lmsbackend.dto.classes.CommentCountByParentDto;
import com.example.lmsbackend.dto.post.CommentCountDto;
import com.example.lmsbackend.repository.custom.CommentRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long>,
        QuerydslPredicateExecutor<CommentEntity>, CommentRepositoryCustom {

    @Query("SELECT new com.example.lmsbackend.dto.classes.CommentCountByParentDto(c1.parentComment.id, COUNT(c1)) " +
            "FROM CommentEntity c1 WHERE c1.parentComment.id IN (SELECT c.id " +
            "FROM CommentEntity c WHERE c.parentComment.id IS NULL) " +
            "group by c1.parentComment.id")
    List<CommentCountByParentDto> countChildrenCommentsByParent();

    @Query("SELECT c.createdBy.username FROM CommentEntity c WHERE c.id = ?1")
    String findUsernameCreatedComment(Long commentId);

    @Query("SELECT c FROM CommentEntity c WHERE c.id = ?1 AND c.createdBy = ?2")
    Boolean isCommentOwnedByUser(Long commentId, Long userId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassTeacherEntity c " +
            "WHERE c.classEntity.id = (SELECT p.classEntity.id FROM CommentEntity comment INNER JOIN PostEntity p ON p.id = comment.post.id WHERE comment.id = ?1) " +
            "AND c.teacher.id = ?2")
    Boolean isTeacherInClassByComment(Long commentId, Long teacherId);

    @Query("SELECT CASE WHEN COUNT (c) > 0 THEN true ELSE false END FROM ClassStudentEntity c " +
            "WHERE c.classEntity.id = (SELECT p.classEntity.id FROM CommentEntity comment INNER JOIN PostEntity p ON p.id = comment.post.id WHERE comment.id = ?1) " +
            "AND c.student.id = ?2")
    Boolean isStudentInClassByComment(Long commentId, Long studentId);

    @Query("SELECT new com.example.lmsbackend.dto.post.CommentCountDto(c.parentComment.id, COUNT(c.parentComment.id)) FROM CommentEntity c WHERE c.parentComment.id IN ?1 " +
            "GROUP BY c.parentComment.id")
    List<CommentCountDto> countChildOfCommentIn(List<Long> commentsId);
}