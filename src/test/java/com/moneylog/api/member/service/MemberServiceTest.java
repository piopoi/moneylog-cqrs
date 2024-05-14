package com.moneylog.api.member.service;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static com.moneylog.api.member.domain.Role.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.domain.Role;
import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.dto.MemberGetResponse;
import com.moneylog.api.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class MemberServiceTest {

    private final String email = "test@test.com";
    private final String password = "12345678";
    private final Role role = ROLE_ADMIN;

    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

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
    @DisplayName("사용자를 생성할 수 있다.")
    void createMember() {
        //given
        String email = "test1@test.com";
        MemberCreateRequest memberCreateRequest = MemberCreateRequest.builder()
                .email(email)
                .password(password)
                .build();

        //when
        memberId = memberService.createMember(memberCreateRequest);

        //then
        Member actualMember = findMemberById(memberId);
        assertThat(actualMember).isNotNull();
        assertThat(actualMember.getEmail()).isEqualTo(email);
    }

    @Test
    @DisplayName("id로 사용자를 조회할 수 있다.")
    void getMember() {
        //when
        MemberGetResponse memberGetResponse = memberService.getMember(memberId);

        //then
        assertThat(memberGetResponse).isNotNull();
        assertThat(memberGetResponse.getId()).isEqualTo(memberId);
        assertThat(memberGetResponse.getEmail()).isEqualTo(email);
        assertThat(memberGetResponse.getRole()).isEqualTo(ROLE_ADMIN);
    }

    @Test
    @DisplayName("id로 캐싱된 사용자를 조회할 수 있다.")
    void getCachedMember() {
        //when
        memberService.getMember(memberId);

        //then: Verify the member is cached
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        MemberGetResponse cachedMemberGetResponse = (MemberGetResponse) valueOperations.get("members::" + memberId);
        assertThat(cachedMemberGetResponse).isNotNull();
        assertThat(cachedMemberGetResponse.getId()).isEqualTo(memberId);
        assertThat(cachedMemberGetResponse.getEmail()).isEqualTo(email);
        assertThat(cachedMemberGetResponse.getRole()).isEqualTo(ROLE_ADMIN);
    }

    @Test
    @DisplayName("존재하지 않는 id로 사용자를 조회할 수 없다.")
    void getMember_invalidId() {
        //when then
        assertThatThrownBy(() -> memberService.getMember(99L))
                .isInstanceOf(CustomException.class)
                .hasMessage(MEMBER_NOT_EXISTS.getMessage());
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(AUTH_MEMBER_NOT_EXISTS));
    }
}
