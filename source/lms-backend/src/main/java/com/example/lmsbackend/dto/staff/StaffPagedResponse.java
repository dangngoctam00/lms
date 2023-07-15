package com.example.lmsbackend.dto.staff;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;

import java.util.List;

@Data
public class StaffPagedResponse extends AbstractPagedDto {
    private List<StaffDTO> listData;
}
