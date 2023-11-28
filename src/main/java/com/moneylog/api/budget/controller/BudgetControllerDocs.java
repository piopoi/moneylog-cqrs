package com.moneylog.api.budget.controller;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "예산")
public interface BudgetControllerDocs {

    @Operation(summary = "예산 생성", description = "사용자가 예산을 생성한다.")
    @ApiResponse(
            responseCode = "201", description = "CREATED",
            headers = @Header(name = "Location", description = "생성한 예산 조회 URI")
    )
    ResponseEntity<Void> createBudget(BudgetCreateRequest budgetCreateRequest,
                                      MemberAdapter memberAdapter);
}