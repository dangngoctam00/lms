package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.VotingChoiceEntity;
import com.example.lmsbackend.repository.custom.VotingChoiceRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface VotingChoiceRepository extends JpaRepository<VotingChoiceEntity, Long>,
        QuerydslPredicateExecutor<VotingChoiceEntity>, VotingChoiceRepositoryCustom {

    @Query("SELECT v FROM VotingChoiceEntity v LEFT JOIN FETCH v.voting WHERE v.id = ?1")
    Optional<VotingChoiceEntity> findFetchVotingById(Long id);
}
