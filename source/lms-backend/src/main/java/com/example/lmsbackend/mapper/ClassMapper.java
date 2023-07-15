package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.domain.classmodel.ClassEntity;
import com.example.lmsbackend.domain.classmodel.ClassTeacherEntity;
import com.example.lmsbackend.domain.classmodel.SessionAttendanceTimeEntity;
import com.example.lmsbackend.domain.scheduler.ClassSessionEntity;
import com.example.lmsbackend.dto.classes.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        uses = {
                ChapterMapper.class,
                QuizMapper.class,
                UnitMapper.class
        }
)
public interface ClassMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ClassEntity mapToClassEntity(CreateClassRequestDto dto);

    ClassDto mapToClassDto(ClassEntity entity);

    void mapUpdateClassInformation(@MappingTarget ClassEntity entity, UpdateClassRequestDto dto);

    @Mapping(target = "firstName", source = "entity.teacher.firstName")
    @Mapping(target = "lastName", source = "entity.teacher.lastName")
    @Mapping(target = "username", source = "entity.teacher.username")
    @Mapping(target = "userId", source = "entity.teacher.id")
    @Mapping(target = "avatar", source = "entity.teacher.avatar")
    @Mapping(target = "email", source = "entity.teacher.email")
    @Mapping(target = "phone", source = "entity.teacher.phone")
    MemberDto mapToMemberDto(ClassTeacherEntity entity);

    @Mapping(target = "firstName", source = "entity.firstName")
    @Mapping(target = "lastName", source = "entity.lastName")
    @Mapping(target = "username", source = "entity.username")
    @Mapping(target = "userId", source = "entity.id")
    @Mapping(target = "avatar", source = "entity.avatar")
    @Mapping(target = "email", source = "entity.email")
    @Mapping(target = "phone", source = "entity.phone")
    MemberDto mapToMemberDto(StudentEntity entity);

    @Mapping(target = "id", ignore = true)
    ClassSessionEntity mapToClassSession(ClassSessionRequestDto dto);

    ClassSessionRequestDto mapToClassSessionDto(ClassSessionEntity entity);

    @Mapping(target = "unit.textbooks", ignore = true)
    @Mapping(target = "unit.content", ignore = true)
    ClassSessionResponseDto mapToClassSessionResponseDto(ClassSessionEntity entity);

    void mapToClassSession(@MappingTarget ClassSessionEntity entity, ClassSessionRequestDto dto);

    @Mapping(target = "strategy", source = "entity.strategy")
    @Mapping(target = "note", source = "entity.noteInSession")
    AttendanceMetaData mapToAttendanceMetaData(ClassSessionEntity entity);

    AttendanceTimeMetaData mapToAttendanceTimeMetaData(SessionAttendanceTimeEntity entity);
}
