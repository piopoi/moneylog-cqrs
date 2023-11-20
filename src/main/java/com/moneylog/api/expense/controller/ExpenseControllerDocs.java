package com.moneylog.api.expense.controller;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.dto.ExpenseGetListRequest;
import com.moneylog.api.expense.dto.ExpenseGetListResponse;
import com.moneylog.api.expense.dto.ExpenseGetResponse;
import com.moneylog.api.expense.dto.ExpenseUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "지출")
public interface ExpenseControllerDocs {

    @Operation(summary = "지출 생성", description = "사용자가 지출을 생성한다.")
    @ApiResponse(responseCode = "201", description = "CREATED")
    ResponseEntity<Void> createExpense(MemberAdapter memberAdapter,
                                       ExpenseCreateRequest expenseCreateRequest);

    @Operation(summary = "지출 수정", description = "지출을 생성한 사용자 또는 관리자가 지출을 수정한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<Void> updateExpense(MemberAdapter memberAdapter,
                                       @Parameter(description = "지출 아이디") Long expenseId,
                                       ExpenseUpdateRequest expenseUpdateRequest);

    @Operation(summary = "지출 상세 조회", description = "지출을 생성한 사용자 또는 관리자가 지출을 상세 조회한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<ExpenseGetResponse> getExpense(MemberAdapter memberAdapter,
                                                  @Parameter(description = "지출 아이디") Long expenseId);

    @Operation(summary = "지출 목록 조회", description = "사용자 본인이 등록한 지출 목록을 조회한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<ExpenseGetListResponse> getExpenses(MemberAdapter memberAdapter,
                                                       ExpenseGetListRequest expenseGetListRequest);

    @Operation(summary = "지출 삭제", description = "지출을 생성한 사용자 또는 관리자가 지출을 삭제한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<Void> deleteExpense(MemberAdapter memberAdapter,
                                       @Parameter(description = "지출 아이디") Long expenseId);
}
