package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.dto.response.BaseResponse;
import lombok.Data;

@Data
public class GetUserInfoResponse extends BaseResponse {
    private UserInfo userInfo;

    @Data
    public static class UserInfo {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String avatar;
        private String address;
    }
}
