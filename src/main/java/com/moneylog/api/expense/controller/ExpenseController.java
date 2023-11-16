package com.moneylog.api.expense.controller;

import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.service.ExpenseService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping("/api/expenses")
    public ResponseEntity<Void> createExpense(@RequestBody @Valid ExpenseCreateRequest expenseCreateRequest) {
        Long expenseId = expenseService.createExpense(expenseCreateRequest);
        return ResponseEntity.created(URI.create("/api/expenses/" + expenseId)).build();
    }
}
