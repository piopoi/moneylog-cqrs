package com.moneylog.api.auth.service;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.auth.dto.TokenCreateRequest;
import com.moneylog.api.auth.dto.TokenCreateResponse;
import com.moneylog.api.auth.infrastructure.JwtTokenProvider;
import com.moneylog.api.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional(readOnly = true)
    public TokenCreateResponse login(TokenCreateRequest tokenCreateRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                tokenCreateRequest.getEmail(),
                tokenCreateRequest.getPassword()
        );
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        Member member = ((MemberAdapter) authentication.getPrincipal()).getMember();
        return jwtTokenProvider.generateToken(member);
    }
}
