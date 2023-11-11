package com.moneylog.api.member.controller;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static com.moneylog.api.member.domain.Role.ROLE_ADMIN;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.domain.Role;
import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class MemberControllerTest {

    private final String requestUri = "/api/members";
    private final String email = "test@test.com";
    private final String password = "12345678";
    private final Role role = ROLE_ADMIN;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Long memberId;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();
        memberId = memberRepository.save(member).getId();
    }

    @Test
    @WithMockUser
    @DisplayName("사용자를 생성할 수 있다.")
    void createMember() throws Exception {
        //given
        String email = "test1@test.com";
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("잘못된 이메일로 사용자를 생성할 수 없다.")
    void createMember_invalidEmail() throws Exception {
        //given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email("test.com")
                .password(password)
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MEMBER_EMAIL_INVALID.name()));
    }

    @Test
    @WithMockUser
    @DisplayName("이메일 없이 사용자를 생성할 수 없다.")
    void createMember_emptyEmail() throws Exception {
        //given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .password(password)
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MEMBER_EMAIL_EMPTY.name()));
    }

    @Test
    @WithMockUser
    @DisplayName("비밀번호 없이 사용자를 생성할 수 없다.")
    void createMember_emptyPassword() throws Exception {
        //given
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MEMBER_PASSWORD_EMPTY.name()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("id로 사용자를 조회할 수 있다.")
    void getMember() throws Exception {
        //when then
        mockMvc.perform(get(requestUri + "/{memberId}", memberId)
                        .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(memberId))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value(role.name()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("존재하지 않는 id로 사용자를 조회할 수 없다.")
    void getMember_invalidId() throws Exception {
        //when then
        mockMvc.perform(get(requestUri + "/{memberId}", 999L)
                        .accept(APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(MEMBER_NOT_EXISTS.name()));
    }
}
