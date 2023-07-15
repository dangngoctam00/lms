package com.example.lmsbackend.dto.classes;

import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.staff.StaffDTO;
import lombok.Data;

import java.util.List;

@Data
public class TeacherCandidatesResponse extends BaseResponse {
    private List<StaffDTO> staffs;
}
