package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.classes.CommentDto;
import com.example.lmsbackend.dto.classes.CommentPagedDto;
import com.example.lmsbackend.dto.classes.PostReactionDto;
import com.example.lmsbackend.dto.post.PostDto;
import com.example.lmsbackend.dto.post.PostPagedDto;
import com.example.lmsbackend.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/classes/{classId}/posts")
    public ResponseEntity<PostPagedDto> getPosts(@PathVariable Long classId,
                                                 @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                 @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                 @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(postService.getPosts(classId, page, size, keyword));
    }

    @PostMapping("/classes/{classId}/posts")
    public ResponseEntity<PostDto> createPost(@PathVariable Long classId, @Valid @RequestBody PostDto dto) {
        return ResponseEntity.ok(postService.createPost(classId, dto));
    }

    @GetMapping("/classes/{classId}/posts/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long classId, @PathVariable Long postId,
                                           @RequestParam(value = "page", defaultValue = "1") Integer page,
                                           @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(postService.getPost(postId, page, size));
    }

    @PutMapping("/classes/{classId}/posts/{postId}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long classId, @PathVariable Long postId, @Valid @RequestBody PostDto dto) {
        return ResponseEntity.ok(postService.updatePost(postId, dto));
    }

    @DeleteMapping("/classes/{classId}/posts/{postId}")
    public ResponseEntity<Long> deletePost(@PathVariable Long classId, @PathVariable Long postId) {
        return ResponseEntity.ok(postService.deletePost(postId));
    }

    @PostMapping("/classes/{classId}/posts/{postId}/interaction")
    public ResponseEntity<PostDto> interactPost(@PathVariable Long classId,
                                                @PathVariable Long postId,
                                                @Valid @RequestBody PostReactionDto dto) {
        return ResponseEntity.ok(postService.interactPost(postId, dto));
    }

    @PostMapping("/classes/{classId}/posts/{postId}/comments")
    public ResponseEntity<CommentDto> commentPost(@PathVariable Long classId,
                                              @PathVariable Long postId,
                                              @Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(postService.commentPost(postId, dto));
    }

    @PutMapping("/classes/{classId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long classId,
                                                    @PathVariable Long postId,
                                                    @PathVariable Long commentId,
                                                    @Valid @RequestBody CommentDto dto) {
        return ResponseEntity.ok(postService.updateComment(commentId, dto));
    }

    @DeleteMapping("/classes/{classId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Long> deleteComment(@PathVariable Long classId,
                                              @PathVariable Long postId,
                                              @PathVariable Long commentId) {
        return ResponseEntity.ok(postService.deleteComment(commentId));
    }

    @GetMapping("/classes/{classId}/posts/{postId}/comments")
    public ResponseEntity<CommentPagedDto> getCommentsByPost(@PathVariable Long classId,
                                                             @PathVariable Long postId,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(postService.getCommentsByPost(postId, page, size));
    }

    @GetMapping("/classes/{classId}/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentPagedDto> getCommentsByComment(@PathVariable Long classId,
                                                                @PathVariable Long postId,
                                                                @PathVariable Long commentId,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                @RequestParam(value = "size", defaultValue = "5") Integer size) {
        return ResponseEntity.ok(postService.getCommentsByComment(postId, commentId, page, size));
    }

}
