package com.moneylog.api.member.service;

import static com.moneylog.api.exception.domain.ErrorCode.*;

import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.dto.MemberGetResponse;
import com.moneylog.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long createMember(MemberCreateRequest memberCreateRequest) {
        Member member = Member.of(memberCreateRequest, passwordEncoder);
        return memberRepository.save(member).getId();
    }

    @Transactional(readOnly = true)
    public MemberGetResponse getMember(Long memberId) {
        Member member = findMemberById(memberId);
        return MemberGetResponse.of(member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_EXISTS));
    }
}
