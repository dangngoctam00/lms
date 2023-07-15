package com.example.lmsbackend.domain.discussion;


import com.example.lmsbackend.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "comment")
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "comment-comments",
        attributeNodes = {
                @NamedAttributeNode(value = "createdBy"),
                @NamedAttributeNode(value = "childComments")
        }
)
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private CommentEntity parentComment;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "parentComment")
    private List<CommentEntity> childComments = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    public void setPost(PostEntity post) {
        this.post = post;
        post.getComments().add(this);
    }

    public void setParentComment(CommentEntity parentComment) {
        this.parentComment = parentComment;
        parentComment.getChildComments().add(this);
    }
}
