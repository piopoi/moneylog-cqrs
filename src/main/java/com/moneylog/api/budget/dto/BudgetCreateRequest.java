package com.moneylog.api.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BudgetCreateRequest {

    @Schema(description = "예산 목록")
    @NotNull(message = "BUDGET_REQUEST_EMPTY")
    @Valid
    private List<BudgetRequest> budgetRequests;

    @Getter
    @Builder
    public static class BudgetRequest {

        @Schema(description = "카테고리 아이디")
        @NotNull(message = "CATEGORY_EMPTY")
        private Long categoryId;

        @Schema(description = "예산 금액")
        @NotNull(message = "BUDGET_BUDGETAMOUNT_EMPTY")
        @Min(value = 0, message = "BUDGET_BUDGETAMOUNT_INVALID")
        private Long budgetAmount;
    }
}
