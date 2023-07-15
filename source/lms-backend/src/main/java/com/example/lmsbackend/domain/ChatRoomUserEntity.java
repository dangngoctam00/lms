package com.example.lmsbackend.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chat_room_user")
@Getter
@Setter
public class ChatRoomUserEntity {

    @EmbeddedId
    private ChatRoomUserKey id = new ChatRoomUserKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("chatRoomId")
    @JoinColumn(name = "chat_room_id")
    private ChatRoomEntity chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "number_of_unseen_messages", columnDefinition = "default 0")
    private Integer numberOfUnSeenMessages;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoomUserEntity that = (ChatRoomUserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(numberOfUnSeenMessages, that.numberOfUnSeenMessages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numberOfUnSeenMessages);
    }
}
