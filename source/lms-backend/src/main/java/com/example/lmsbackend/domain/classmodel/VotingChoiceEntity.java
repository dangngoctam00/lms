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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Table(name = "voting_choice")
@Entity
@Getter
@Setter
@FieldNameConstants
@EntityListeners(AuditingEntityListener.class)
public class VotingChoiceEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "content")
    private String content;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "voting_id")
    private VotingEntity voting;

    @OneToMany(fetch = LAZY, cascade = ALL, mappedBy = "votingChoice")
    private Set<UserVotingChoiceEntity> chosenBy = new HashSet<>();

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

    public void setVoting(VotingEntity voting) {
        this.voting = voting;
        voting.getChoices().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VotingChoiceEntity that = (VotingChoiceEntity) o;
        if (id == null) return false;
        return Objects.equals(id, that.id) && Objects.equals(content, that.content) && Objects.equals(createdBy, that.createdBy);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
