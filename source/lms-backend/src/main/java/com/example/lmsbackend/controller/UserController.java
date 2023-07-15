package com.example.lmsbackend.controller;

import com.example.lmsbackend.dto.request.user.ChangePasswordRequest;
import com.example.lmsbackend.dto.response.BaseResponse;
import com.example.lmsbackend.dto.response.auth.UserDto;
import com.example.lmsbackend.dto.response.user.*;
import com.example.lmsbackend.service.UserAuthService;
import com.example.lmsbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.lmsbackend.constant.AppConstant.API_PREFIX;

@Slf4j
@RestController
@RequestMapping(API_PREFIX)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserAuthService userAuthService;

    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users")
    public ResponseEntity<UsersPagedDto> findUserByName(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userService.findUserByKeyword(keyword, page, size));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UsersPagedDto.UserInformation> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/users/{userId}/roles")
    public ResponseEntity<GetRolesOfUserResponse> getRoles(@PathVariable(name = "userId") Long userId) {
        List<RoleOfUserDTO> roles = userAuthService.getRoles(userId);
        GetRolesOfUserResponse response = new GetRolesOfUserResponse();
        response.setRoles(roles);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/permissions")
    public ResponseEntity<GetPermissionsOfUserResponse> getPermissions(@PathVariable(name = "userId") Long userId) {
        List<PermissionOfUserDTO> permissions = userAuthService.getPermissions(userId);
        GetPermissionsOfUserResponse response = new GetPermissionsOfUserResponse();
        response.setPermissions(permissions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/getLimitPermissions")
    public ResponseEntity<GetLimitPermissionOfUserResponse> getLimitPermissionsOfUser(){
        List<LimitPermissionOfUserDTO> permissions = userAuthService.getLimitPermissionsOfUser();
        GetLimitPermissionOfUserResponse response = new GetLimitPermissionOfUserResponse();
        response.setPermissions(permissions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/getUserInfo")
    public ResponseEntity<GetUserInfoResponse> getUserInfoResponse(@PathVariable("userId") Long userId){
        GetUserInfoResponse.UserInfo userInfo = userService.getUserInfo(userId);
        GetUserInfoResponse response = new GetUserInfoResponse();
        response.setUserInfo(userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/users/changePassword")
    public ResponseEntity<BaseResponse> changePassword(@RequestBody ChangePasswordRequest request){
        userService.changePassword(request);
        return ResponseEntity.ok(new BaseResponse());
    }
}
