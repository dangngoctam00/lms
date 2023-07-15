package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.discussion.PostEntity;

import java.util.Optional;

public interface PostRepositoryCustom {

    Optional<PostEntity> findPostAndInteractionsById(Long postId);

    Optional<PostEntity> findPostAndCommentsById(Long postId);

    com.example.lmsbackend.repository.custom.PagedList<PostEntity> findPosts(Long classId, Integer page, Integer size, String keyword);
}
