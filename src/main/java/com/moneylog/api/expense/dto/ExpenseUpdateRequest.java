package com.moneylog.api.expense.dto;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class ExpenseUpdateRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedAt;

    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmount;

    private Long categoryId;

    private String memo;

    private Boolean isExcludeTotal;
}
