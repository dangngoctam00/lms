package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.discussion.CommentEntity;
import com.example.lmsbackend.dto.classes.CommentDto;
import com.example.lmsbackend.mapper.exam.AuditMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface CommentMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)

    @Mapping(target = "childComments", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CommentEntity mapToCommentEntity(CommentDto dto);


    @Mapping(target = "childrenComments", ignore = true)
    CommentDto mapToCommentDtoIgnoreChildrenComments(CommentEntity entity);
}
