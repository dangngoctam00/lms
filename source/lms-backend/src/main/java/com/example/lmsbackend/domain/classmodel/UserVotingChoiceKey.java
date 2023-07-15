package com.example.lmsbackend.domain.classmodel;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class UserVotingChoiceKey implements Serializable {

    private static final long serialVersionUID = -8281151546644913020L;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "voting_choice_id")
    private Long votingChoiceId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVotingChoiceKey that = (UserVotingChoiceKey) o;
        return Objects.equals(userId, that.userId) && Objects.equals(votingChoiceId, that.votingChoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, votingChoiceId);
    }
}
