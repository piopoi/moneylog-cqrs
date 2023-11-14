package com.moneylog.api.budget.dto;

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

    @NotNull(message = "BUDGET_REQUEST_EMPTY")
    @Valid
    private List<BudgetRequest> budgetRequests;

    @Getter
    @Builder
    public static class BudgetRequest {

        @NotNull(message = "BUDGET_CATEGORY_EMPTY")
        private Long categoryId;

        @NotNull(message = "BUDGET_BUDGETAMOUNT_EMPTY")
        @Min(value = 0, message = "BUDGET_BUDGETAMOUNT_INVALID")
        private Long budgetAmount;
    }
}
