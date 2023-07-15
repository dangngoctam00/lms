package com.example.lmsbackend.repository;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, QuerydslPredicateExecutor<UserEntity>,
        UserRepositoryCustom {

    Optional<UserEntity> findByUsername(String username);

    List<UserEntity> findUserEntitiesByIdIn(Collection<Long> idList);

    @Query("SELECT " +
            "new com.example.lmsbackend.domain.UserEntity(u.id," +
            " u.username," +
            " u.password," +
            " u.firstName," +
            " u.lastName," +
            " u.email," +
            " u.phone," +
            " u.avatar," +
            " u.accountType) FROM UserEntity u WHERE u.username = ?1")
    Optional<UserEntity> findAuthUserByUserName(String username);
}
