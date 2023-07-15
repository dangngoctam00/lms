package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.domain.discussion.CommentEntity;

import java.util.Optional;

public interface CommentRepositoryCustom {

    Optional<CommentEntity> findCommentAndListChildById(Long id);

    PagedList<CommentEntity> findCommentsByPost(Long postId, Integer page, Integer size);

    PagedList<CommentEntity> findCommentsByComment(Long commentId, Integer page, Integer size);
}
