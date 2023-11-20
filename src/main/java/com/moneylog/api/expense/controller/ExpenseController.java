package com.moneylog.api.expense.controller;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.dto.ExpenseGetListRequest;
import com.moneylog.api.expense.dto.ExpenseGetListResponse;
import com.moneylog.api.expense.dto.ExpenseGetResponse;
import com.moneylog.api.expense.dto.ExpenseUpdateRequest;
import com.moneylog.api.expense.service.ExpenseService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController implements ExpenseControllerDocs {

    private final ExpenseService expenseService;

    @PostMapping("/api/expenses")
    public ResponseEntity<Void> createExpense(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                              @RequestBody @Valid ExpenseCreateRequest expenseCreateRequest) {
        Long expenseId = expenseService.createExpense(memberAdapter.getMember(), expenseCreateRequest);
        return ResponseEntity.created(URI.create("/api/expenses/" + expenseId)).build();
    }

    @PatchMapping("/api/expenses/{expenseId}")
    @PreAuthorize("@expenseService.hasAuthManageExpense(#memberAdapter.getMember(), #expenseId)")
    public ResponseEntity<Void> updateExpense(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                              @PathVariable Long expenseId,
                                              @RequestBody @Valid ExpenseUpdateRequest expenseUpdateRequest) {
        expenseService.updateExpense(expenseId, expenseUpdateRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/expenses/{expenseId}")
    @PreAuthorize("@expenseService.hasAuthManageExpense(#memberAdapter.getMember(), #expenseId)")
    public ResponseEntity<ExpenseGetResponse> getExpense(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                         @PathVariable Long expenseId) {
        ExpenseGetResponse expenseGetResponse = expenseService.getExpense(expenseId);
        return ResponseEntity.ok(expenseGetResponse);
    }

    @GetMapping("/api/expenses")
    public ResponseEntity<ExpenseGetListResponse> getExpenses(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                              @RequestBody @Valid ExpenseGetListRequest expenseGetListRequest) {
        Long memberId = memberAdapter.getMember().getId();
        ExpenseGetListResponse expenseGetListResponse = expenseService.getExpenses(memberId, expenseGetListRequest);
        return ResponseEntity.ok(expenseGetListResponse);
    }

    @DeleteMapping("/api/expenses/{expenseId}")
    @PreAuthorize("@expenseService.hasAuthManageExpense(#memberAdapter.getMember(), #expenseId)")
    public ResponseEntity<Void> deleteExpense(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                              @PathVariable Long expenseId) {
        expenseService.deleteExpense(expenseId);
        return ResponseEntity.ok().build();
    }
}
