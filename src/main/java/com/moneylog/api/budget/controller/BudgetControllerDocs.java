package com.moneylog.api.budget.controller;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetRecommendRequest;
import com.moneylog.api.budget.dto.BudgetRecommendResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
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

    @Operation(summary = "예산 추천", description = "사용자에게 총 예산액을 받아서 카테고리 별 예산을 추천한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<List<BudgetRecommendResponse>> recommendBudget(BudgetRecommendRequest budgetRecommendRequest);
}
