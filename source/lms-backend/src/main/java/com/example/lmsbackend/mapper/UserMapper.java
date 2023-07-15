package com.example.lmsbackend.mapper;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.dto.response.auth.UserDto;
import com.example.lmsbackend.dto.response.user.GetUserInfoResponse;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UsersPagedDto.UserInformation mapToUserInformation(UserEntity entity);

    UserDto mapToUserDto(UserEntity entity);

    GetUserInfoResponse.UserInfo mapToUserInfo(UserEntity userEntity);
}
