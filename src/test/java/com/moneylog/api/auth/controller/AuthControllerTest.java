package com.moneylog.api.auth.controller;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneylog.api.auth.dto.TokenCreateRequest;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    private final String email = "test@test.com";
    private final String password = "12345678";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();
        memberRepository.save(Member.of(memberCreateRequest, passwordEncoder));
    }

    @Test
    @DisplayName("로그인 요청 시 jwt를 발급받는다.")
    void login() throws Exception {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenCreateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.grantType").value("Bearer"))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    @DisplayName("잘못된 이메일로 로그인할 수 없다.")
    void login_invalidEmail() throws Exception {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .email(email + "t")
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenCreateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(AUTH_MEMBER_NOT_EXISTS.getMessage()));
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인할 수 없다.")
    void login_invalidPassword() throws Exception {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .email(email)
                .password(password + "t")
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenCreateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value(AUTH_AUTHENTICATION_FAILED.getMessage()));
    }

    @Test
    @DisplayName("이메일 없이 로그인할 수 없다.")
    void login_emptyEmail() throws Exception {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .password(password)
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(AUTH_EMAIL_EMPTY.getMessage()));
    }

    @Test
    @DisplayName("비밀번호 없이 로그인할 수 없다.")
    void login_emptyPassword() throws Exception {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .email(email)
                .build();

        //when then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenCreateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(AUTH_PASSWORD_EMPTY.getMessage()));
    }
}
