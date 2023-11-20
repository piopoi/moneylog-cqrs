package com.moneylog.api.member.controller;

import com.moneylog.api.member.dto.MemberCreateRequest;
import com.moneylog.api.member.dto.MemberGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "사용자")
public interface MemberControllerDocs {

    @Operation(summary = "사용자 생성", description = "사용자를 생성한다.")
    @ApiResponse(
            responseCode = "201", description = "CREATED",
            headers = @Header(name = "Location", description = "생성한 사용자 조회 URI")
    )
    ResponseEntity<Void> createMember(MemberCreateRequest memberCreateRequest);

    @Operation(summary = "사용자 상세 조회", description = "사용자를 상세 조회한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<MemberGetResponse> getMember(@Parameter(description = "사용자 아이디") Long memberId);
}
