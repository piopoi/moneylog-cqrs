package com.moneylog.api.expense.service;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.service.CategoryService;
import com.moneylog.api.expense.domain.Expense;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    @Transactional
    public Long createExpense(ExpenseCreateRequest expenseCreateRequest) {
        Category category = categoryService.findCategoryById(expenseCreateRequest.getCategoryId());
        Expense expense = Expense.of(expenseCreateRequest, category);
        return expenseRepository.save(expense).getId();
    }
}
