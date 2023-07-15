package com.example.lmsbackend.domain.classmodel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "voting")
@Entity
@Getter
@Setter
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
@NamedEntityGraph(
        name = "voting",
        attributeNodes = {
                @NamedAttributeNode(value = "choices", subgraph = "choices-sub")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "choices-sub",
                        attributeNodes = {
                                @NamedAttributeNode(value = "chosenBy")
                        }
                )
        }
)
@NamedEntityGraph(
        name = "voting-choices",
        attributeNodes = {
                @NamedAttributeNode(value = "choices")
        }
)
public class VotingEntity {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "is_allowed_to_add_choice")
    private Boolean isAllowedToAddChoice;

    @Transient
    private Integer order;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "voting")
    private List<VotingChoiceEntity> choices = new ArrayList<>();

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "class_id")
    @NotNull
    private ClassEntity classEntity;

    @Column(name = "create_at")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    @CreatedBy
    private String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    private String updatedBy;
}
