package com.moneylog.api.expense.dto;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.expense.domain.Expense;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpenseGetResponse {

    private final Long expenseId;
    private final LocalDateTime expendedAt;
    private final Long expenseAmount;
    private final CategoryResponse category;
    private final String memo;
    private final Boolean isExcludeTotal;

    @Getter
    @Builder
    public static class CategoryResponse {
        private final Long categoryId;
        private final String categoryName;

        public static CategoryResponse from(Category category) {
            return CategoryResponse.builder()
                    .categoryId(category.getId())
                    .categoryName(category.getName())
                    .build();
        }
    }

    public static ExpenseGetResponse from(Expense expense) {
        return ExpenseGetResponse.builder()
                .expenseId(expense.getId())
                .expendedAt(expense.getExpendedAt())
                .expenseAmount(expense.getExpenseAmount())
                .category(CategoryResponse.from(expense.getCategory()))
                .memo(expense.getMemo())
                .isExcludeTotal(expense.getIsExcludeTotal())
                .build();
    }
}
