package com.example.lmsbackend.repository.custom;

import java.util.List;

public interface VotingChoiceRepositoryCustom {

    void deleteByIdIn(List<Long> ids);
}
