package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.UserVotingChoiceEntity;
import com.example.lmsbackend.domain.classmodel.UserVotingChoiceKey;
import com.example.lmsbackend.dto.classes.VotingChoiceCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserVotingChoiceRepository extends JpaRepository<UserVotingChoiceEntity, UserVotingChoiceKey> {

    @Query("SELECT new com.example.lmsbackend.dto.classes.VotingChoiceCount(u.votingChoice.id, count(u.votingChoice.id))" +
            "FROM UserVotingChoiceEntity AS u GROUP BY u.votingChoice.id")
    List<VotingChoiceCount> countNumberOfChosenByVotingChoice();
}
