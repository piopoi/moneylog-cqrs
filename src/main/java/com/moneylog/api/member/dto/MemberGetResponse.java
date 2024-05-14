package com.moneylog.api.member.dto;

import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberGetResponse {

    @Schema(description = "사용자 아이디", example = "1")
    private Long id;

    @Schema(description = "사용자 이메일", example = "test@test.com")
    private String email;

    @Schema(description = "사용자 권한")
    private Role role;

    protected MemberGetResponse() {
    }

    @Builder
    public MemberGetResponse(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public static MemberGetResponse of(Member member) {
        return MemberGetResponse.builder()
                .id(member.getId())
                .email(member.getEmail())
                .role(member.getRole())
                .build();
    }
}
