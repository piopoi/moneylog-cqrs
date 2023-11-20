package com.moneylog.api.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TokenCreateResponse {

    @Schema(example = "Bearer")
    private String grantType;

    private String accessToken;

    private String refreshToken;
}
