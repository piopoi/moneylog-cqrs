package com.moneylog.api.auth.exception;

import static com.moneylog.api.exception.domain.ErrorCode.AUTH_AUTHORIZATION_FAILED;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneylog.api.exception.dto.CustomErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Encoding;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        log.error(accessDeniedException.getClass().getSimpleName(), accessDeniedException.getMessage());

        CustomErrorResponse customErrorResponse = CustomErrorResponse.of(AUTH_AUTHORIZATION_FAILED);
        String responseBody = objectMapper.writeValueAsString(customErrorResponse);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(Encoding.DEFAULT_CHARSET.name());
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(responseBody);
    }
}
