package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.ProgramEntity;
import com.example.lmsbackend.repository.custom.ProgramRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ProgramRepository extends JpaRepository<ProgramEntity, Long>, QuerydslPredicateExecutor<ProgramEntity>,
        ProgramRepositoryCustom {

    Optional<ProgramEntity> findByCode(String code);

    @EntityGraph("program-course")
    Optional<ProgramEntity> findById(Long id);
}
