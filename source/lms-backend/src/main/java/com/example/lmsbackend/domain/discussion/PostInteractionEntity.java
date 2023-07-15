package com.example.lmsbackend.domain.discussion;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.domain.compositekey.PostInteractionKey;
import com.example.lmsbackend.enums.InteractionType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "post_interaction")
public class PostInteractionEntity {

    @EmbeddedId
    private PostInteractionKey id = new PostInteractionKey();

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private InteractionType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setPost(PostEntity post) {
        this.post = post;
        post.getInteractions().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostInteractionEntity that = (PostInteractionEntity) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
