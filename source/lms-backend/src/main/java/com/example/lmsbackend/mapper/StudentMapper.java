package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.StudentEntity;
import com.example.lmsbackend.dto.students.StudentDTO;
import com.example.lmsbackend.dto.students.StudentInfo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentDTO mapToStudentDTO(StudentEntity studentEntity);
    StudentEntity mapToStudentEntity(StudentDTO studentDTO);

    StudentInfo mapToStudentInfo(StudentEntity entity);
}
