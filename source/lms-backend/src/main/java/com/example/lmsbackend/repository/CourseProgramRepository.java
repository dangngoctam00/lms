package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.CourseProgramEntity;
import com.example.lmsbackend.domain.CourseProgramKey;
import com.example.lmsbackend.repository.custom.CourseProgramRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface CourseProgramRepository extends JpaRepository<CourseProgramEntity, CourseProgramKey>,
        QuerydslPredicateExecutor<CourseProgramEntity>, CourseProgramRepositoryCustom {
}
