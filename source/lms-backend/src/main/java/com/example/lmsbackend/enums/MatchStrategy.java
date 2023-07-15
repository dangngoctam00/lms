package com.example.lmsbackend.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MatchStrategy {
    EXACT("EXACT"),
    CONTAIN("CONTAIN"),
    REGEX("REGEX");

    final String type;
}
