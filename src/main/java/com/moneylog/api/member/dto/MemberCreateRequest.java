package com.moneylog.api.member.dto;

import com.moneylog.api.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberCreateRequest {

    @Schema(description = "사용자 이메일", example = "member@moneylog.com")
    @NotBlank(message = "MEMBER_EMAIL_EMPTY")
    @Email(message = "MEMBER_EMAIL_INVALID",
            regexp = "^[\\w!#$%&amp;'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&amp;'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @Schema(description = "사용자 비밀번호")
    @NotBlank(message = "MEMBER_PASSWORD_EMPTY")
    private String password;

    @Schema(description = "사용자 권한", allowableValues = {"ROLE_USER", "ROLE_ADMIN"})
    private Role role;
}
