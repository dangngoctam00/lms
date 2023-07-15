package com.example.lmsbackend.repository.custom;

import com.blazebit.persistence.PagedList;
import com.example.lmsbackend.domain.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserRepositoryCustom {
    Set<UserEntity> findUsersAndChatRoomByIds(List<Long> usersId);

    PagedList<UserEntity> findByKeyword(String keyword, Integer page, Integer size);

    Optional<UserEntity> findFetchVotingChoicesUsername(String username);
}
