package com.example.lmsbackend.service;

import com.example.lmsbackend.config.security.CustomUserDetails;
import com.example.lmsbackend.config.security.aop.Auth;
import com.example.lmsbackend.domain.UserEntity;
import com.example.lmsbackend.dto.request.user.ChangePasswordRequest;
import com.example.lmsbackend.dto.response.auth.UserDto;
import com.example.lmsbackend.dto.response.user.GetUserInfoResponse;
import com.example.lmsbackend.dto.response.user.UsersPagedDto;
import com.example.lmsbackend.enums.PermissionEnum;
import com.example.lmsbackend.exceptions.IncorrectPasswordException;
import com.example.lmsbackend.exceptions.RePasswordNotEqualsPasswordException;
import com.example.lmsbackend.exceptions.UnauthorizedException;
import com.example.lmsbackend.exceptions.UserNotFoundException;
import com.example.lmsbackend.mapper.MapperUtils;
import com.example.lmsbackend.mapper.UserMapper;
import com.example.lmsbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    public UsersPagedDto findUserByKeyword(String keyword, Integer page, Integer size) {
        var usersByKeyword = userRepository.findByKeyword(keyword, page, size);
        var usersPaged = new UsersPagedDto();
        MapperUtils.mapPagedDto(usersPaged, usersByKeyword);
        usersPaged.setListData(
                usersByKeyword
                        .stream()
                        .map(userMapper::mapToUserInformation)
                        .collect(toList())
        );
        return usersPaged;
    }

    public Long  getCurrentUserId() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnauthorizedException();
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getId();
    }

    public String getCurrentUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UnauthorizedException();
        }
        return ((CustomUserDetails) authentication.getPrincipal()).getUsername();
    }

    /**
     * Get auth information only
    * */
    public UserEntity getCurrentUser() {
        var username = getCurrentUsername();
        var currentUserOpt = userRepository.findAuthUserByUserName(username);
        if (currentUserOpt.isEmpty()) {
            throw new UnauthorizedException();
        }
        return currentUserOpt.get();
    }

    public UserEntity findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public UsersPagedDto.UserInformation findById(Long id) {
        var userEntity = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.mapToUserInformation(userEntity);
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::mapToUserDto)
                .collect(toList());
    }

    public UsersPagedDto.UserInformation getUserInformationByUserName(String username) {
        var byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            return userMapper.mapToUserInformation(byUsername.get());
        }
        var userInformation = new UsersPagedDto.UserInformation();
        userInformation.setUsername(username);
        return userInformation;
    }

    @Auth(permission = PermissionEnum.VIEW_USER_INFO, selfCheck = true)
    public GetUserInfoResponse.UserInfo getUserInfo(Long resourceId) {
        UserEntity userEntity = userRepository.findById(resourceId)
                .orElseThrow(UserNotFoundException::new);
        return userMapper.mapToUserInfo(userEntity);
    }

    public void changePassword(ChangePasswordRequest request) {
        Long currentUserId = getCurrentUserId();
        UserEntity user = userRepository.findById(currentUserId)
                .orElseThrow(UnauthorizedException::new);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new IncorrectPasswordException();
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())){
            throw new RePasswordNotEqualsPasswordException();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
