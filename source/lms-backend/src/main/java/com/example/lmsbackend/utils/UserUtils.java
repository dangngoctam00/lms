package com.example.lmsbackend.utils;

import com.example.lmsbackend.domain.UserEntity;

public class UserUtils {

    public static String getNameOfUser(UserEntity user) {
        return String.format("%s %s", user.getLastName(), user.getFirstName());
    }
}
