package com.moneylog.api.auth.service;

import static com.moneylog.api.exception.domain.ErrorCode.AUTH_MEMBER_NOT_EXISTS;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(AUTH_MEMBER_NOT_EXISTS));
        return MemberAdapter.from(member);
    }
}
