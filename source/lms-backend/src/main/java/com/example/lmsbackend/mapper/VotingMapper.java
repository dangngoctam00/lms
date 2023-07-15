package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.classmodel.VotingChoiceEntity;
import com.example.lmsbackend.domain.classmodel.VotingEntity;
import com.example.lmsbackend.dto.classes.VotingChoiceDto;
import com.example.lmsbackend.dto.classes.VotingDto;
import com.example.lmsbackend.mapper.exam.AuditMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface VotingMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(target = "choices", ignore = true)
    VotingDto mapToVotingDto(VotingEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "choices", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void mapToVotingEntity(@MappingTarget VotingEntity entity, VotingDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "choices", ignore = true)
    VotingEntity mapToVotingEntity(VotingDto dto);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    VotingChoiceEntity mapToVotingChoiceEntity(VotingChoiceDto dto);

    VotingChoiceDto mapToVotingChoiceDto(VotingChoiceEntity entity);
}
