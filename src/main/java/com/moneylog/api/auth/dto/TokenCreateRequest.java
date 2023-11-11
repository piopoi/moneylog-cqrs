package com.moneylog.api.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenCreateRequest {

    @NotBlank(message = "AUTH_EMAIL_EMPTY")
    @Email(message = "AUTH_EMAIL_INVALID", regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private final String email;

    @NotBlank(message = "AUTH_PASSWORD_EMPTY")
    private final String password;
}
