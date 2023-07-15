package com.example.lmsbackend.dto.response.user;

import com.example.lmsbackend.dto.response.AbstractPagedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class UsersPagedDto extends AbstractPagedDto {

    List<UserInformation> listData;

    @Data
    public static class UserInformation {
        private Long id;
        private String username;
        private String firstName;
        private String lastName;
        private String avatar;
    }
}
