package com.moneylog.api.expense.service;

import static com.moneylog.api.exception.domain.ErrorCode.EXPENSE_NOT_EXISTS;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.service.CategoryService;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.expense.domain.Expense;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.dto.ExpenseGetResponse;
import com.moneylog.api.expense.dto.ExpenseUpdateRequest;
import com.moneylog.api.expense.repository.ExpenseRepository;
import com.moneylog.api.member.domain.Member;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryService categoryService;

    @Transactional
    public Long createExpense(Member member, ExpenseCreateRequest expenseCreateRequest) {
        Category category = findCategoryById(expenseCreateRequest.getCategoryId());
        Expense expense = Expense.of(member, expenseCreateRequest, category);
        return expenseRepository.save(expense).getId();
    }

    @Transactional
    public void updateExpense(Long expenseId, ExpenseUpdateRequest expenseUpdateRequest) {
        Expense expense = findExpenseById(expenseId);
        Category category = findCategoryById(expenseUpdateRequest.getCategoryId());
        expense.update(expenseUpdateRequest, category);
    }

    @Transactional(readOnly = true)
    public ExpenseGetResponse getExpense(Long expenseId) {
        Expense expense = findExpenseById(expenseId);
        return ExpenseGetResponse.from(expense);
    }

    @Transactional(readOnly = true)
    public boolean hasAuthManageExpense(Member member, Long expenseId) {
        Expense expense = findExpenseById(expenseId);
        return member.isAdmin() || expense.isRegister(member);
    }

    private Category findCategoryById(Long categoryId) {
        if (Objects.isNull(categoryId)) {
            return null;
        }
        return categoryService.findCategoryById(categoryId);
    }

    private Expense findExpenseById(Long expenseId) {
        return expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomException(EXPENSE_NOT_EXISTS));
    }
}
