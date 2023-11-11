package com.moneylog.api.auth.infrastructure;

import static com.moneylog.api.auth.infrastructure.JwtTokenProvider.CLAIMS_AUTH;
import static com.moneylog.api.auth.infrastructure.JwtTokenProvider.GRANTTYPE_BEARER;
import static com.moneylog.api.exception.domain.ErrorCode.AUTH_JWT_UNPRIVILEGED;

import com.moneylog.api.exception.domain.CustomException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = resolveToken(request);
        if (token != null) {
            jwtTokenProvider.validateToken(token);
            Authentication authentication = getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(GRANTTYPE_BEARER)) {
            return bearerToken.substring(GRANTTYPE_BEARER.length() + 1);
        }
        return null;
    }

    private Authentication getAuthentication(String accessToken) {
        Claims claims = jwtTokenProvider.parseClaims(accessToken);
        if (claims.get(CLAIMS_AUTH) == null) {
            throw new CustomException(AUTH_JWT_UNPRIVILEGED);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }
}
