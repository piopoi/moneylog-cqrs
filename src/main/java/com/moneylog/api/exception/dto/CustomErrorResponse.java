package com.moneylog.api.exception.dto;

import com.moneylog.api.exception.domain.ErrorCode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CustomErrorResponse {

    private final Integer code;
    private final String message;

    @Builder
    private CustomErrorResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public static CustomErrorResponse of(ErrorCode errorCode) {
        return CustomErrorResponse.builder()
                .code(errorCode.getHttpStatus().value())
                .message(errorCode.getMessage())
                .build();
    }
}
