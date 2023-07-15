package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MemberPagedDto extends AbstractPagedDto {
    private List<MemberDto> listData;
}
