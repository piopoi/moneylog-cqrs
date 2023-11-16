package com.moneylog.api.expense.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class ExpenseCreateRequest {

    @NotNull(message = "EXPENSE_EXPENDEDAT_EMPTY")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedAt;

    @NotNull(message = "EXPENSE_EXPENSEAMOUNT_EMPTY")
    @Min(0)
    private Long expenseAmount;

    @NotNull(message = "CATEGORY_EMPTY")
    private Long categoryId;

    private String memo;

    @NotNull(message = "EXPENSE_ISEXCLUDETOTAL_EMPTY")
    private Boolean isExcludeTotal;
}
