package com.example.lmsbackend.repository.custom.impl;

import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.querydsl.BlazeJPAQuery;
import com.example.lmsbackend.domain.QUserEntity;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.repository.custom.UserRepositoryCustom;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserRepositoryCustomImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    private final CriteriaBuilderFactory criteriaBuilderFactory;

    @Override
    public Set<UserEntity> findUsersAndChatRoomByIds(List<Long> usersId) {
        var user = QUserEntity.userEntity;
        var users = new BlazeJPAQuery<UserEntity>(entityManager, criteriaBuilderFactory)
                .from(user)
                .leftJoin(user.chatRooms)
                .fetchJoin()
                .where(user.id.in(usersId))
                .select(user)
                .distinct()
                .fetch();
        return Set.copyOf(users);
    }

    @Override
    public PagedList<UserEntity> findByKeyword(String keyword, Integer page, Integer size) {
        var user = QUserEntity.userEntity;
        return new BlazeJPAQuery<UserEntity>(entityManager, criteriaBuilderFactory)
                .from(user)
                .where(user.firstName.containsIgnoreCase(keyword)
                        .or(user.lastName.containsIgnoreCase(keyword))
                        .or(user.username.containsIgnoreCase(keyword)))
                .select(user)
                .orderBy(user.id.asc())
                .fetchPage((page - 1) * size, size);
    }

    @Override
    public Optional<UserEntity> findFetchVotingChoicesUsername(String username) {
        var user = QUserEntity.userEntity;
        var userEntity = new BlazeJPAQuery<UserEntity>(entityManager, criteriaBuilderFactory)
                .from(user)
                .leftJoin(user.votingChoices)
                .fetchJoin()
                .where(user.username.eq(username))
                .select(user)
                .fetchFirst();
        return Optional.ofNullable(userEntity);
    }
}
