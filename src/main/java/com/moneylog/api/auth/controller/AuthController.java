package com.moneylog.api.auth.controller;

import com.moneylog.api.auth.dto.TokenCreateRequest;
import com.moneylog.api.auth.dto.TokenCreateResponse;
import com.moneylog.api.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController implements AuthControllerDocs {

    private final AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<TokenCreateResponse> login(@RequestBody @Valid TokenCreateRequest tokenCreateRequest) {
        TokenCreateResponse tokenCreateResponse = authService.login(tokenCreateRequest);
        return ResponseEntity.ok().body(tokenCreateResponse);
    }
}
