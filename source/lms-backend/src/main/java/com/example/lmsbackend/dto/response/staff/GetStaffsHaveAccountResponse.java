package com.example.lmsbackend.dto.response.staff;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.staff.StaffSimple;
import lombok.Data;

import java.util.List;

@Data
public class GetStaffsHaveAccountResponse extends BaseResponse {
    private List<StaffSimple> staffs;
}
