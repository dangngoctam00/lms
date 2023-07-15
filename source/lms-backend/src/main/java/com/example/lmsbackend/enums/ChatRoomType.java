package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum ChatRoomType {
    USER("USER"),
    GROUP("GROUP");

    ChatRoomType(String value) {
        this.value = value;
    }

    private final String value;
}
