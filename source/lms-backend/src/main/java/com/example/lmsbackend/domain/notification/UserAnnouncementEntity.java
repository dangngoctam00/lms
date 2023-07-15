package com.example.lmsbackend.domain.notification;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.compositekey.UserAnnouncementKey;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;

@Table(name = "announcement_user")
@Entity
@Setter
@Getter
@FieldNameConstants
@NamedEntityGraph(
        name = "user-announcement",
        attributeNodes = {
                @NamedAttributeNode(value = "user"),
                @NamedAttributeNode(value = "announcement", subgraph = "user-notification-subgraph")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "user-notification-subgraph",
                        attributeNodes = {
                                @NamedAttributeNode("tags"),
                                @NamedAttributeNode("sender")
                        }
                ),
        }
)
public class UserAnnouncementEntity {

    @EmbeddedId
    private UserAnnouncementKey id = new UserAnnouncementKey();

    @Column(name = "seen")
    private Boolean seen = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("receiverId")
    @JoinColumn(name = "receiver_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("announcementId")
    @JoinColumn(name = "announcement_id")
    private AnnouncementEntity announcement;

    public void setNotification(AnnouncementEntity announcement) {
        this.announcement = announcement;
        announcement.getReceivers().add(this);
    }

    public void setUser(UserEntity user) {
        this.user = user;
        user.getNotifications().add(this);
    }
}