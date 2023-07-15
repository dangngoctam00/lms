package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.discussion.CommentEntity;
import com.example.lmsbackend.domain.discussion.PostEntity;
import com.example.lmsbackend.domain.discussion.PostInteractionEntity;
import com.example.lmsbackend.dto.classes.CommentDto;
import com.example.lmsbackend.dto.classes.CommentPagedDto;
import com.example.lmsbackend.dto.classes.PostInteractionCountDto;
import com.example.lmsbackend.dto.classes.PostReactionDto;
import com.example.lmsbackend.dto.post.CommentCountDto;
import com.example.lmsbackend.dto.post.PostDto;
import com.example.lmsbackend.dto.post.PostPagedDto;
import com.example.lmsbackend.enums.InteractionType;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.aclass.ClassNotFoundException;
import com.example.lmsbackend.exceptions.aclass.CommentNotFoundException;
import com.example.lmsbackend.exceptions.aclass.PostNotFoundException;
import com.example.lmsbackend.mapper.CommentMapper;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.PostMapper;
import com.example.lmsbackend.repository.ClassRepository;
import com.example.lmsbackend.repository.CommentRepository;
import com.example.lmsbackend.repository.PostInteractionRepository;
import com.example.lmsbackend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class PostService {
    private final UserService userService;
    private final ClassRepository classRepository;
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;

    private final PostRepository postRepository;

    private final PostInteractionRepository postInteractionRepository;
    private final CommentRepository commentRepository;

    private final RedissonClient redissonClient;

    private int dem = 0;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    @Auth(permission = PermissionEnum.CREATE_POST)
    public PostDto createPost(Long resourceId, PostDto dto) {
        var currentUser = userService.getCurrentUser();
        var classEntity = classRepository.findFetchPostsById(resourceId)
                .orElseThrow(() -> new ClassNotFoundException(resourceId));

        var post = postMapper.mapToPostEntity(dto);
        post.setCreatedBy(currentUser);
        post.setClassEntity(classEntity);

        postRepository.save(post);

        return mapToPostDtoWhenCreating(post);
    }

    private PostDto mapToPostDtoWhenCreating(PostEntity post) {
        var postDto = postMapper.mapToPostDto(post);
        postDto.setDownVoteCount(0);
        postDto.setUpVoteCount(0);
        postDto.setPersonalPreference(InteractionType.NONE.name());
        return postDto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_POST)
    public PostDto updatePost(Long resourceId, PostDto dto) {
        var post = postRepository.findById(resourceId)
                .orElseThrow(() -> new PostNotFoundException(resourceId));
        var currentUserId = userService.getCurrentUserId();
        if (!Objects.equals(currentUserId, post.getCreatedBy().getId())) {
            throw new UnauthorizedException();
        }
        entityManager.detach(post);
        postMapper.mapToPostEntity(post, dto);
        post = entityManager.merge(post);
        entityManager.flush();
        return getPost(resourceId, 1, 5);
    }

    @Transactional
    @Auth(permission = PermissionEnum.INTERACT_POST)
    public PostDto interactPost(Long resourceId, PostReactionDto dto) {
        var user = userService.getCurrentUser();
        RLock lock = redissonClient.getLock("Lock_Post_" + resourceId + "_" + user.getId());
        lock.lock(10, TimeUnit.SECONDS);
        try {
            var post = postRepository.findPostAndInteractionsById(resourceId)
                    .orElseThrow(() -> new PostNotFoundException(resourceId));

            var interactionType = InteractionType.valueOf(dto.getType());
            var postInteractionOpt = postInteractionRepository.findByUserIdAndPostId(user.getId(), resourceId);
            if (postInteractionOpt.isPresent()) {
                var interaction = postInteractionOpt.get();
                if (interaction.getType() == interactionType) {
                    post.getInteractions().remove(interaction);
                } else {
                    interaction.setType(interactionType);
                }
            } else {
                var interaction = new PostInteractionEntity();
                interaction.setPost(post);
                interaction.setType(interactionType);
                interaction.setUser(user);
            }
            postRepository.save(post);

            return mapPostDto(user.getUsername(), post);
        } finally {
            lock.unlock();
        }
    }

    private void setInteractionCount(PostDto dto, List<PostInteractionCountDto> count) {
        dto.setUpVoteCount(getInteractionCountByType(count, InteractionType.UP_VOTE));
        dto.setDownVoteCount(getInteractionCountByType(count, InteractionType.DOWN_VOTE));
    }

    private Integer getInteractionCountByType(Collection<PostInteractionCountDto> count, InteractionType type) {
        var any = count.stream()
                .filter(c -> c.getType() == type)
                .findAny();
        if (any.isPresent()) {
            return Math.toIntExact(any.get().getCount());
        }
        return 0;
    }

    private Integer getInteractionCountByType(Set<PostInteractionEntity> interactions, InteractionType type) {
        return Math.toIntExact(interactions.stream()
                .filter(c -> c.getType() == type)
                .count());
    }

    @Transactional
    @Auth(permission = PermissionEnum.COMMENT_POST)
    public CommentDto commentPost(Long resourceId, CommentDto dto) {
        var post = postRepository.findPostAndCommentsById(resourceId)
                .orElseThrow(() -> new PostNotFoundException(resourceId));


        var user = userService.getCurrentUser();

        var comment = commentMapper.mapToCommentEntity(dto);

        if (dto.getParentCommentId() != null) {
            var parentComment = commentRepository.findCommentAndListChildById(dto.getParentCommentId())
                    .orElseThrow(() -> new CommentNotFoundException(dto.getParentCommentId()));
            comment.setParentComment(parentComment);
        }

        comment.setPost(post);
        // undirectional @manytoone mapping
        comment.setCreatedBy(user);
        entityManager.flush();
        return commentMapper.mapToCommentDtoIgnoreChildrenComments(comment);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_COMMENT)
    public CommentPagedDto getCommentsByPost(Long resourceId, Integer page, Integer size) {
        var comments = commentRepository.findCommentsByPost(resourceId, page, size);
        var commentsId = comments.stream()
                .map(CommentEntity::getId)
                .collect(toList());
        var commentsChildCount = commentRepository.countChildOfCommentIn(commentsId);
        var dto = new CommentPagedDto();
        MapperUtils.mapPagedDto(dto, comments);
        dto.setListData(
                comments.stream()
                        .map(comment -> {
                            var commentDto = commentMapper.mapToCommentDtoIgnoreChildrenComments(comment);
                            commentDto.setNumberOfChildrenComments(
                                    Math.toIntExact(commentsChildCount.stream()
                                            .filter(x -> Objects.equals(x.getCommentId(), comment.getId()))
                                            .findAny()
                                            .map(CommentCountDto::getNumberOfChildrenComments)
                                            .orElse(0L))
                            );
                            return commentDto;
                        })
                        .collect(toList())
        );
        return dto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_COMMENT)
    public CommentPagedDto getCommentsByComment(Long resourceId, Long commentId, Integer page, Integer size) {
        var comments = commentRepository.findCommentsByComment(commentId, page, size);
        var commentsId = comments.stream()
                .map(CommentEntity::getId)
                .collect(toList());
        var commentsChildCount = commentRepository.countChildOfCommentIn(commentsId);
        var dto = new CommentPagedDto();
        MapperUtils.mapPagedDto(dto, comments);
        dto.setListData(
                comments.stream()
                        .map(comment -> {
                            var commentDto = commentMapper.mapToCommentDtoIgnoreChildrenComments(comment);
                            commentDto.setNumberOfChildrenComments(
                                    Math.toIntExact(commentsChildCount.stream()
                                            .filter(x -> Objects.equals(x.getCommentId(), comment.getId()))
                                            .findAny()
                                            .map(CommentCountDto::getNumberOfChildrenComments)
                                            .orElse(0L))
                            );
                            commentDto.setParentCommentId(comment.getParentComment().getId());
                            return commentDto;
                        })
                        .collect(toList())
        );
        return dto;
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_LIST_POST)
    @Cacheable("posts")
    public PostPagedDto getPosts(Long resourceId, Integer page, Integer size, String keyword) {
        var posts = postRepository.findPosts(resourceId, page, size, keyword);

        var currentUser = userService.getCurrentUsername();

        var dto = new PostPagedDto();
        MapperUtils.mapPagedDto(dto, posts);
        dto.setListData(posts.getListData().stream()
                .map(post -> mapPostDto(currentUser, post))
                .collect(toList()));
        return dto;
    }

    private PostDto mapPostDto(String currentUser, PostEntity post) {
        var postDto = postMapper.mapToPostDto(post);
        var interactions = post.getInteractions();
        mapSelfInteraction(postDto, currentUser, interactions);
        postDto.setUpVoteCount(getInteractionCountByType(interactions, InteractionType.UP_VOTE));
        postDto.setDownVoteCount(getInteractionCountByType(interactions, InteractionType.DOWN_VOTE));
        postDto.setRepliesCount(post.getComments().size());
        return postDto;
    }

    private void mapSelfInteraction(PostDto dto, String username, Set<PostInteractionEntity> interactions) {
        if (checkInteraction(interactions, username, InteractionType.UP_VOTE)) {
            dto.setPersonalPreference(InteractionType.UP_VOTE.name());
        } else if (checkInteraction(interactions, username, InteractionType.DOWN_VOTE)) {
            dto.setPersonalPreference(InteractionType.DOWN_VOTE.name());
        } else {
            dto.setPersonalPreference(InteractionType.NONE.name());
        }
    }

    private Boolean checkInteraction(Set<PostInteractionEntity> interactions, String username, InteractionType type) {
        return interactions.stream()
                .anyMatch(interaction -> StringUtils.equals(username, interaction.getUser().getUsername())
                        && interaction.getType() == type);
    }

    @Transactional(readOnly = true)
    @Auth(permission = PermissionEnum.VIEW_POST)
    @Cacheable("post")
    public PostDto getPost(Long resourceId, Integer page, Integer size) {
        var post = postRepository.findPostAndInteractionsById(resourceId)
                .orElseThrow(() -> new PostNotFoundException(resourceId));
        var currentUser = userService.getCurrentUsername();
        var dto = mapPostDto(currentUser, post);
        var comment = getCommentsByPost(resourceId, page, size);
        dto.setComments(comment);
        return dto;
    }

    @Transactional
    @Auth(permission = PermissionEnum.UPDATE_COMMENT)
    public CommentDto updateComment(Long resourceId, CommentDto dto) {
        var comment = commentRepository.findById(resourceId)
                .orElseThrow(() -> new CommentNotFoundException(resourceId));
        var currentUserId = userService.getCurrentUserId();
        if (!Objects.equals(currentUserId, comment.getCreatedBy().getId())) {
            throw new UnauthorizedException();
        }
        comment.setContent(dto.getContent());
        commentRepository.save(comment);
        return commentMapper.mapToCommentDtoIgnoreChildrenComments(comment);
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_COMMENT)
    public Long deleteComment(Long resourceId) {
        entityManager.createQuery("DELETE FROM CommentEntity c WHERE c.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();
        return resourceId;
    }

    @Transactional
    @Auth(permission = PermissionEnum.DELETE_POST)
    public Long deletePost(Long resourceId) {
        entityManager.createQuery("DELETE FROM PostEntity p WHERE p.id = (:id)")
                .setParameter("id", resourceId)
                .executeUpdate();
        return resourceId;
    }
}
