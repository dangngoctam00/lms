package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.classmodel.VotingEntity;
import com.example.lmsbackend.dto.classes.VotingDto;
import com.example.lmsbackend.repository.custom.VotingRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface VotingRepository extends JpaRepository<VotingEntity, Long>,
        QuerydslPredicateExecutor<VotingEntity>, VotingRepositoryCustom {

    @EntityGraph(attributePaths = {"choices"})
    Optional<VotingEntity> findById(Long id);

    @Query("SELECT new com.example.lmsbackend.dto.classes.VotingDto(u.id, u.title) FROM VotingEntity u WHERE u.id IN ?1")
    List<VotingDto> findVotesDtoByIdIn(List<Long> idList);

    @Query("SELECT q.id FROM VotingEntity q WHERE q.classEntity.id = ?1")
    List<Long> getVotingId(Long id);
}
