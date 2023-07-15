package com.example.lmsbackend.dto.post;

import lombok.Getter;

@Getter
public class CommentCountDto {
    private Long commentId;
    private Long numberOfChildrenComments;

    public CommentCountDto(Long commentId, Long numberOfChildrenComments) {
        this.commentId = commentId;
        this.numberOfChildrenComments = numberOfChildrenComments;
    }
}
