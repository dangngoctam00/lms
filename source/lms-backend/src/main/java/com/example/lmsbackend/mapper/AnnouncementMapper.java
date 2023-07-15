package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.notification.AnnouncementEntity;
import com.example.lmsbackend.dto.notification.CreateAnnouncementRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AnnouncementMapper {

    @Mapping(target = "tags", ignore = true)
    AnnouncementEntity mapToNotification(CreateAnnouncementRequest dto);
}
