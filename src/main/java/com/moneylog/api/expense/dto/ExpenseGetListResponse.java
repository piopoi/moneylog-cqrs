package com.moneylog.api.expense.dto;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.dto.CategoryGetResponse;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExpenseGetListResponse {

    private final Long totalAmount;
    private final List<ExpenseGetResponse> expenses;
    private final List<CategoryAmountResponse> categoryAmount;

    @Getter
    @Builder
    public static class CategoryAmountResponse {
        private final CategoryGetResponse category;
        private final Long categoryAmount;

        public static CategoryAmountResponse of(Category category, Long categoryAmount) {
            return CategoryAmountResponse.builder()
                    .category(CategoryGetResponse.from(category))
                    .categoryAmount(categoryAmount)
                    .build();
        }
    }

    public static ExpenseGetListResponse of(Long totalAmount, List<ExpenseGetResponse> expenses, List<CategoryAmountResponse> categoryAmount) {
        return ExpenseGetListResponse.builder()
                .totalAmount(totalAmount)
                .expenses(expenses)
                .categoryAmount(categoryAmount)
                .build();
    }
}
