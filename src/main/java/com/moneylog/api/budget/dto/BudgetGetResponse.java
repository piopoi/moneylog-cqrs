package com.moneylog.api.budget.dto;

import com.moneylog.api.budget.domain.Budget;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.member.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BudgetGetResponse {

    @Schema(description = "예산 아이디", example = "1")
    private Long id;

    @Schema(description = "사용자")
    private Member member;

    @Schema(description = "예산 카테고리")
    private Category category;

    @Schema(description = "예산액", example = "1000000")
    private Long budgetAmount;

    @Schema(description = "평균 예산비율")
    private Double ratio;

    @Builder
    public BudgetGetResponse(Long id, Member member, Category category, Long budgetAmount, Double ratio) {
        this.id = id;
        this.member = member;
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.ratio = ratio;
    }

    public static BudgetGetResponse of(Budget budget) {
        return BudgetGetResponse.builder()
                .id(budget.getId())
                .member(budget.getMember())
                .category(budget.getCategory())
                .budgetAmount(budget.getBudgetAmount())
                .ratio(budget.getRatio())
                .build();
    }

}
