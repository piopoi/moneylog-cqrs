package com.moneylog.api.budget.service;

import static com.moneylog.api.exception.domain.ErrorCode.BUDGET_CATEGORY_NOT_EXISTS;

import com.moneylog.api.budget.domain.Budget;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetCreateRequest.BudgetRequest;
import com.moneylog.api.budget.repository.BudgetRepository;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    public void createBudget(BudgetCreateRequest budgetCreateRequest, Member member) {
        List<Budget> budgets = makeBudgets(budgetCreateRequest, member);
        budgetRepository.saveAll(budgets);
    }

    private List<Budget> makeBudgets(BudgetCreateRequest budgetCreateRequest, Member member) {
        List<BudgetRequest> budgetRequests = budgetCreateRequest.getBudgetRequests();
        return budgetRequests.stream()
                .map(budgetRequest -> {
                    Category category = findCategoryById(budgetRequest.getCategoryId());
                    return Budget.of(member, category, budgetRequest.getBudgetAmount());
                })
                .toList();
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(BUDGET_CATEGORY_NOT_EXISTS));
    }
}
