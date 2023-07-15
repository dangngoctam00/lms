package com.example.lmsbackend.domain.notification;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.enums.AnnouncementScope;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "announcement")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"tags"})
@EntityListeners(AuditingEntityListener.class)
@FieldNameConstants
@NamedEntityGraph(
        name = "notification-user-tags",
        attributeNodes = {
                @NamedAttributeNode(value = "sender"),
                @NamedAttributeNode(value = "tags")
        }
)
public class AnnouncementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private UserEntity sender;

    @Column(name = "subject")
    private String subject;

    @Column(name = "content")
    private String content;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private AnnouncementScope scope;

    @Column(name = "scope_id")
    private Long scopeId;

    @Column(name = "attachment")
    private String attachment;

    @Column(name = "sent_at")
    @CreatedDate
    private LocalDateTime sentAt;

    @Column(name = "is_visible_for_sender")
    private Boolean isVisibleForSender = true;

    @Column(name = "to_all_members")
    private Boolean toAllMembers = false;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "announcement")
    private Set<UserAnnouncementEntity> receivers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "announcement_tag",
            joinColumns = @JoinColumn(name = "announcement_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags = new HashSet<>();
}
