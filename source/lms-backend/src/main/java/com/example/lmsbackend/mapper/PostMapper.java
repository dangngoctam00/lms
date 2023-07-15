package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.discussion.PostEntity;
import com.example.lmsbackend.dto.post.PostDto;
import com.example.lmsbackend.mapper.exam.AuditMapper;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = AuditMapper.class)
public interface PostMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "comments", ignore = true)
    PostEntity mapToPostEntity(PostDto dto);

    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "interactions", ignore = true)
    @Mapping(target = "classEntity", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void mapToPostEntity(@MappingTarget PostEntity entity, PostDto dto);

    @Mapping(target = "comments", ignore = true)
    PostDto mapToPostDto(PostEntity entity);
}
