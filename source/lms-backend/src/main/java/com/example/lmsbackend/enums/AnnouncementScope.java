package com.example.lmsbackend.enums;

import lombok.Getter;

@Getter
public enum AnnouncementScope {
    CLASS("CLASS"),
    COURSE("COURSE"),
    BRANCH("BRANCH"),
    CENTER("CENTER");

    final String scope;

    AnnouncementScope(String scope) {
        this.scope = scope;
    }
}
