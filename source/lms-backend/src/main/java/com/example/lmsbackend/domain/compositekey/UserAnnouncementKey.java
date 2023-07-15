package com.example.lmsbackend.domain.compositekey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldNameConstants
public class UserAnnouncementKey implements Serializable {

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "announcement_id")
    private Long announcementId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAnnouncementKey that = (UserAnnouncementKey) o;
        return Objects.equals(receiverId, that.receiverId) && Objects.equals(announcementId, that.announcementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiverId, announcementId);
    }
}
