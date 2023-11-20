package com.moneylog.api.expense.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
public class ExpenseGetListRequest {

    @Schema(description = "필터: 지출일시 - 시작", example = "2023-11-01T12:30")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedStartAt;

    @Schema(description = "필터: 지출일시 - 종료", example = "2023-11-01T12:30")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime expendedEndAt;

    @Schema(description = "필터: 지출액 - 최소금액", example = "10000", minimum = "0")
    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmountMin;

    @Schema(description = "필터: 지출액 - 최대금액", example = "50000", minimum = "0")
    @Min(value = 0, message = "EXPENSE_EXPENSEAMOUNT_MINUS")
    private Long expenseAmountMax;

    @Schema(description = "필터: 카테고리 아이디", example = "1")
    private Long categoryId;
}
