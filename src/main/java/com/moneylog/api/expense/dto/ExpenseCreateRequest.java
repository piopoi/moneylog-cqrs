package com.moneylog.api.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class ExpenseCreateRequest {

    @Schema(description = "지출일시", example = "2023-11-01T12:30")
    @NotNull(message = "EXPENSE_EXPENDEDAT_EMPTY")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedAt;

    @Schema(description = "지출액", example = "10000", minimum = "0")
    @NotNull(message = "EXPENSE_EXPENSEAMOUNT_EMPTY")
    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmount;

    @Schema(description = "카테고리 아이디", example = "1")
    @NotNull(message = "CATEGORY_EMPTY")
    private Long categoryId;

    @Schema(description = "메모")
    private String memo;

    @Schema(description = "합계 제외 여부", defaultValue = "false")
    @NotNull(message = "EXPENSE_ISEXCLUDETOTAL_EMPTY")
    private Boolean isExcludeTotal;
}
