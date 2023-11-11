package com.moneylog.api.auth.infrastructure;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.moneylog.api.auth.dto.TokenCreateResponse;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.repository.MemberRepository;
import java.lang.reflect.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setUp() {
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email("test@test.com")
                .password("12345678")
                .build();
        member = memberRepository.save(Member.of(memberCreateRequest, passwordEncoder));
    }

    @Test
    @DisplayName("JWT를 생성할 수 있다.")
    void generateToken() {
        //when
        TokenCreateResponse tokenCreateResponse = jwtTokenProvider.generateToken(member);

        //then
        assertThat(tokenCreateResponse).isNotNull();
    }

    @Test
    @DisplayName("accessToken의 유효기간을 검증할 수 있다.")
    void validateToken_accessToken() throws NoSuchFieldException, IllegalAccessException {
        //given
        setExpirationTimeMillis(-100000L);
        TokenCreateResponse tokenCreateResponse = jwtTokenProvider.generateToken(member);

        //when
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(tokenCreateResponse.getAccessToken()))
                .isInstanceOf(CustomException.class)
                .hasMessage(AUTH_JWT_EXPIRED.getMessage());
    }

    @Test
    @DisplayName("refreshToken의 유효기간을 검증할 수 있다.")
    void validateToken_refreshToken() throws NoSuchFieldException, IllegalAccessException {
        //given
        setExpirationTimeMillis(-10000L);
        TokenCreateResponse tokenCreateResponse = jwtTokenProvider.generateToken(member);

        //when
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(tokenCreateResponse.getRefreshToken()))
                .isInstanceOf(CustomException.class)
                .hasMessage(AUTH_JWT_EXPIRED.getMessage());
    }

    /**
     * 리플렉션을 사용하여 JwtTokenProvider.expirationTimeMillis 변경
     */
    private void setExpirationTimeMillis(Long expirationTimeMillis) throws NoSuchFieldException, IllegalAccessException {
        Field field = JwtTokenProvider.class.getDeclaredField("expirationTimeMillis");
        field.setAccessible(true);
        field.set(jwtTokenProvider, expirationTimeMillis);
    }
}
