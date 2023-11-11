package com.moneylog.api.member.domain;

import static com.moneylog.api.member.domain.Role.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.moneylog.api.member.dto.MemberCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class MemberTest {

    private final String email = "test@test.com";
    private final String password = "12345678#";
    private final Role role = ROLE_ADMIN;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("DTO로 사용자를 생성할 수 있다.")
    void createMember_of() {
        //given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .role(ROLE_ADMIN)
                .build();

        //when
        Member actualMember = Member.of(memberCreateRequest, passwordEncoder);

        //then
        assertThat(actualMember).isNotNull();
        assertThat(actualMember.getEmail()).isEqualTo(email);
        assertThat(actualMember.getRole()).isEqualTo(role);
        boolean isMatchedPassword = passwordEncoder.matches(password, actualMember.getPassword());
        assertThat(isMatchedPassword).isTrue();
    }

    @Test
    @DisplayName("DTO로 Role 없이 사용자를 생성할 수 있다.")
    void createMember_of_emptyRole() {
        //given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when
        Member actualMember = Member.of(memberCreateRequest, passwordEncoder);

        //then
        assertThat(actualMember).isNotNull();
        assertThat(actualMember.getRole()).isEqualTo(ROLE_USER);
    }
}
