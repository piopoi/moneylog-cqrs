package com.moneylog.api.expense.service;

import static com.moneylog.api.exception.domain.ErrorCode.EXPENSE_NOT_EXISTS;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.service.CategoryService;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.expense.domain.Expense;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.dto.ExpenseGetListRequest;
import com.moneylog.api.expense.dto.ExpenseGetListResponse;
import com.moneylog.api.expense.dto.ExpenseGetListResponse.CategoryAmountResponse;
import com.moneylog.api.expense.dto.ExpenseGetResponse;
import com.moneylog.api.expense.dto.ExpenseUpdateRequest;
import com.moneylog.api.expense.repository.ExpenseRepository;
import com.moneylog.api.member.domain.Member;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
    public ExpenseGetListResponse getExpenses(Long memberId, ExpenseGetListRequest expenseGetListRequest) {
        List<Expense> expenses = expenseRepository.findByFilter(memberId, expenseGetListRequest);
        List<ExpenseGetResponse> expenseGetResponses = convertExpensesToExpenseGetResponses(expenses);
        Long totalAmount = calculateTotalAmount(expenses);
        List<CategoryAmountResponse> categoryAmountResponses = calculateCategoryAmount(expenses);
        return ExpenseGetListResponse.of(totalAmount, expenseGetResponses, categoryAmountResponses);
    }

    private List<ExpenseGetResponse> convertExpensesToExpenseGetResponses(List<Expense> expenses) {
        return expenses.stream()
                .map(ExpenseGetResponse::from)
                .toList();
    }

    @Transactional
    public void deleteExpense(Long expenseId) {
        expenseRepository.deleteById(expenseId);
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

    private static long calculateTotalAmount(List<Expense> expenses) {
        return expenses.stream()
                .filter(expense -> !expense.getIsExcludeTotal())
                .mapToLong(Expense::getExpenseAmount)
                .sum();
    }

    private static List<CategoryAmountResponse> calculateCategoryAmount(List<Expense> expenses) {
        return expenses.stream()
                .filter(expense -> !expense.getIsExcludeTotal())
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.summingLong(Expense::getExpenseAmount)))
                .entrySet()
                .stream()
                .map(entry -> CategoryAmountResponse.of(entry.getKey(), entry.getValue()))
                .toList();
    }
}
