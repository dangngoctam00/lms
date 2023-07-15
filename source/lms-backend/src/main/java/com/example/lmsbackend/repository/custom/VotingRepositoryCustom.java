package com.example.lmsbackend.repository.custom;

import com.example.lmsbackend.domain.classmodel.VotingEntity;

import java.util.Optional;

public interface VotingRepositoryCustom {

    Optional<VotingEntity> findFetchUserVotingChoicesById(Long id);

    Optional<VotingEntity> findFetchChoicesById(Long id);
}
