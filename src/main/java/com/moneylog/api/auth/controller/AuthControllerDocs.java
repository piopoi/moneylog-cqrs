package com.moneylog.api.auth.controller;

import com.moneylog.api.auth.dto.TokenCreateRequest;
import com.moneylog.api.auth.dto.TokenCreateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "인증")
public interface AuthControllerDocs {

    @Operation(summary = "사용자 로그인", description = "사용자가 로그인하여 토큰을 발급받는다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<TokenCreateResponse> login(TokenCreateRequest tokenCreateRequest);
}
