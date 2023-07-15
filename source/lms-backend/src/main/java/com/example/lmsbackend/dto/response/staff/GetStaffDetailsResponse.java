package com.example.lmsbackend.dto.response.staff;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.staff.StaffDTO;
import lombok.Data;

@Data
public class GetStaffDetailsResponse extends BaseResponse {
    private StaffDTO staff;
}
