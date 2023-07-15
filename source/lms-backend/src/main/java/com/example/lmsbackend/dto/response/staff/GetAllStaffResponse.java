package com.example.lmsbackend.dto.response.staff;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.staff.StaffDTO;
import lombok.Data;

import java.util.List;

@Data
public class GetAllStaffResponse extends BaseResponse {
    private List<StaffDTO> staffs;
}
