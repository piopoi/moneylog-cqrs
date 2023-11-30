package com.moneylog.api.budget.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BudgetRecommendRequest {

    @Schema(description = "총 예산액", example = "1000000")
    @NotNull(message = "BUDGET_TOTALAMOUNT_EMPTY")
    @Min(value = 10000L, message = "BUDGET_TOTALAMOUNT_INVALID")
    private Long totalAmount;
}
