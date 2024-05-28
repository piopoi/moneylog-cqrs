package com.moneylog.api.member.domain;

import static com.moneylog.api.exception.domain.ErrorCode.MEMBER_ROLE_INVALID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.moneylog.api.exception.domain.CustomException;
import java.util.stream.Stream;

public enum Role {
    ROLE_USER,
    ROLE_ADMIN;

    @JsonCreator
    public static Role parsing(String inputString) {
        return Stream.of(values())
                .filter(role -> role.name().equals(inputString))
                .findFirst()
                .orElseThrow(() -> new CustomException(MEMBER_ROLE_INVALID));
    }
}
