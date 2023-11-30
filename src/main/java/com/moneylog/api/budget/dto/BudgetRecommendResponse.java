package com.moneylog.api.budget.dto;

import com.moneylog.api.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class BudgetRecommendResponse {

    @Schema(description = "카테고리 아이디", example = "1")
    private final Long categoryId;

    @Schema(description = "카테고리 이름", example = "FOOT")
    private final String categoryName;

    @Schema(description = "예산액", example = "200000")
    private final Long budgetAmount;

    public static BudgetRecommendResponse of(Category category, Long budgetAmount) {
        return BudgetRecommendResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .budgetAmount(budgetAmount)
                .build();
    }
}
