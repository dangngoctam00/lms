package com.example.lmsbackend.dto.students;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class StudentPagedDTO extends AbstractPagedDto {
    List<StudentDTO> listData;
}
