package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.compositekey.PostInteractionKey;
import com.example.lmsbackend.domain.discussion.PostInteractionEntity;
import com.example.lmsbackend.dto.classes.PostInteractionCountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PostInteractionRepository extends JpaRepository<PostInteractionEntity, PostInteractionKey> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM PostInteractionEntity p WHERE p.id.userId = ?1 AND p.id.postId = ?2")
    Optional<PostInteractionEntity> findByUserIdAndPostId(Long userId, Long postId);

    @Query("SELECT new com.example.lmsbackend.dto.classes.PostInteractionCountDto(p.type, count(p)) FROM PostInteractionEntity p " +
            "WHERE p.id.postId = ?1" +
            " GROUP BY p.type")
    List<PostInteractionCountDto> countPostInteractionById(Long postId);

    @Query("SELECT new com.example.lmsbackend.dto.classes.PostInteractionCountDto(p.type, count(p)) FROM PostInteractionEntity p " +
            "WHERE p.id.postId = ?1" +
            " GROUP BY p.type, p.post.id")
    List<PostInteractionCountDto> countPostInteractionByIdIn(List<Long> postsId);
}
