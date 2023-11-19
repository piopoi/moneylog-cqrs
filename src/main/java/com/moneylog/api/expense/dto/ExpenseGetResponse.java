package com.moneylog.api.expense.dto;

import com.moneylog.api.category.dto.CategoryGetResponse;
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
    private final CategoryGetResponse category;
    private final String memo;
    private final Boolean isExcludeTotal;

    public static ExpenseGetResponse from(Expense expense) {
        return ExpenseGetResponse.builder()
                .expenseId(expense.getId())
                .expendedAt(expense.getExpendedAt())
                .expenseAmount(expense.getExpenseAmount())
                .category(CategoryGetResponse.from(expense.getCategory()))
                .memo(expense.getMemo())
                .isExcludeTotal(expense.getIsExcludeTotal())
                .build();
    }
}
