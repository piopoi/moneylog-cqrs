package com.moneylog.api.member.dto;

import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.domain.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberGetResponse {

    private final Long id;
    private final String email;
    private final Role role;

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
