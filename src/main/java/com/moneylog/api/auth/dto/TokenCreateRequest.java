package com.moneylog.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenCreateRequest {

    @Schema(description = "사용자 이메일", example = "test@test.com")
    @NotBlank(message = "AUTH_EMAIL_EMPTY")
    @Email(message = "AUTH_EMAIL_INVALID", regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private final String email;

    @Schema(description = "사용자 비밀번호", example = "password")
    @NotBlank(message = "AUTH_PASSWORD_EMPTY")
    private final String password;
}
