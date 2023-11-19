package com.moneylog.api.expense.dto;

import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class ExpenseGetListRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedEndAt;

    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmountMin;

    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmountMax;

    private Long categoryId;
}
