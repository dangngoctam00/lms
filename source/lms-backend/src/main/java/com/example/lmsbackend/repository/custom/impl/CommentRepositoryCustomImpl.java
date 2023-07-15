package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.discussion.CommentEntity;
import com.example.lmsbackend.domain.discussion.QCommentEntity;
import com.example.lmsbackend.repository.custom.CommentRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Optional<CommentEntity> findCommentAndListChildById(Long id) {
        var result = entityManager.createQuery("SELECT c FROM CommentEntity c LEFT JOIN FETCH c.childComments " +
                        "WHERE c.id = (:id)", CommentEntity.class)
                .setParameter("id", id)
                .getResultList();
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public PagedList<CommentEntity> findCommentsByPost(Long postId, Integer page, Integer size) {
        var comment = QCommentEntity.commentEntity;
        return new BlazeJPAQuery<CommentEntity>(entityManager, criteriaBuilderFactory)
                .from(comment)
                .leftJoin(comment.createdBy)
                .fetchJoin()
                .where(comment.post.id.eq(postId).and(comment.parentComment.isNull()))
                .orderBy(comment.updatedAt.desc())
                .orderBy(comment.id.desc())
                .select(comment)
                .fetchPage(0, page * size);
    }

    @Override
    public PagedList<CommentEntity> findCommentsByComment(Long commentId, Integer page, Integer size) {
        var comment = QCommentEntity.commentEntity;
        return new BlazeJPAQuery<CommentEntity>(entityManager, criteriaBuilderFactory)
                .from(comment)
                .leftJoin(comment.createdBy)
                .fetchJoin()
                .leftJoin(comment.parentComment)
                .fetchJoin()
                .where(comment.parentComment.id.eq(commentId))
                .orderBy(comment.updatedAt.desc())
                .orderBy(comment.id.desc())
                .select(comment)
                .fetchPage(0, size * page);
    }
}
