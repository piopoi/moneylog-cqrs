package com.moneylog.api.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.auth.dto.TokenCreateRequest;
import com.moneylog.api.auth.dto.TokenCreateResponse;
import com.moneylog.api.auth.infrastructure.JwtTokenProvider;
import com.moneylog.api.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthServiceMockTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private Authentication authentication;

    @Test
    @DisplayName("로그인 할 수 있다")
    void login() {
        //given
        TokenCreateRequest tokenCreateRequest = TokenCreateRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        TokenCreateResponse expectedResponse = TokenCreateResponse.builder()
                .grantType("Bearer")
                .accessToken("TOKEN")
                .refreshToken("TOKEN")
                .build();
        Member member = Member.builder()
                .email("test@example.com")
                .password("password")
                .build();
        given(authenticationManagerBuilder.getObject()).willReturn(authenticationManager);
        given(authenticationManager.authenticate(any())).willReturn(authentication);
        given(jwtTokenProvider.generateToken(any())).willReturn(expectedResponse);
        given(authentication.getPrincipal()).willReturn(MemberAdapter.from(member));

        //when
        TokenCreateResponse actualResponse = authService.login(tokenCreateRequest);

        //then
        assertThat(actualResponse.getGrantType()).isEqualTo(expectedResponse.getGrantType());
        assertThat(actualResponse.getAccessToken()).isEqualTo(expectedResponse.getAccessToken());
        assertThat(actualResponse.getRefreshToken()).isEqualTo(expectedResponse.getRefreshToken());
    }
}
