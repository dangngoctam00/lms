package com.example.lmsbackend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public class CommonMapper {

    @Named("defaultDateTimeMapper")
    LocalDateTime defaultDateTime() {
        return LocalDateTime.now();
    }
}
