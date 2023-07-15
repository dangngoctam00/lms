package com.example.lmsbackend.domain.classmodel;

import com.example.lmsbackend.domain.UserEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "user_voting_choice")
@Entity
@Getter
@Setter
@FieldNameConstants
public class UserVotingChoiceEntity {

    @EmbeddedId
    private UserVotingChoiceKey id = new UserVotingChoiceKey();

    @ManyToOne
    @MapsId("votingChoiceId")
    @JoinColumn(name = "voting_choice_id")
    private VotingChoiceEntity votingChoice;


    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public void setVotingChoice(VotingChoiceEntity votingChoice) {
        this.votingChoice = votingChoice;
        votingChoice.getChosenBy().add(this);
    }

    public void setUser(UserEntity user) {
        this.user = user;
        user.getVotingChoices().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserVotingChoiceEntity that = (UserVotingChoiceEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(votingChoice, that.votingChoice) && Objects.equals(user, that.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, votingChoice, user);
    }
}
