package com.example.lmsbackend.domain.discussion;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "post")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@FieldNameConstants
@NamedEntityGraph(
        name = "post",
        attributeNodes = {
                @NamedAttributeNode(value = "interactions", subgraph = "interactions-sub"),
                @NamedAttributeNode(value = "comments")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "interactions-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "user")
                        }
                )
        }
)
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "created_by", updatable = false)
    private UserEntity createdBy;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_id")
    private ClassEntity classEntity;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
    private Set<PostInteractionEntity> interactions = new HashSet<>();

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL, mappedBy = "post")
    private Set<CommentEntity> comments = new HashSet<>();

    public void setClassEntity(ClassEntity classEntity) {
        this.classEntity = classEntity;
        classEntity.getPosts().add(this);
    }
}
