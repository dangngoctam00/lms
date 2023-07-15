package com.example.lmsbackend.mapper.exam;

import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import com.example.lmsbackend.mapper.UserMapper;
import com.example.lmsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditMapper {
    private final UserService userService;
    private final UserMapper userMapper;

    public UsersPagedDto.UserInformation getUserInformation(String username) {
        return userService.getUserInformationByUserName(username);
    }

    public UsersPagedDto.UserInformation getUserInformation(Long userId) {
        return userService.findById(userId);
    }

    public UsersPagedDto.UserInformation getUserInformation(UserEntity user) {
        return userMapper.mapToUserInformation(user);
    }
}
