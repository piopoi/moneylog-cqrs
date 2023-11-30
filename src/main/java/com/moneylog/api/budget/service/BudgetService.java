package com.moneylog.api.budget.service;

import static com.moneylog.api.exception.domain.ErrorCode.CATEGORY_NOT_EXISTS;

import com.moneylog.api.budget.domain.Budget;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetCreateRequest.BudgetRequest;
import com.moneylog.api.budget.repository.BudgetRepository;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.service.CategoryService;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.member.domain.Member;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {

    private final BudgetRepository budgetRepository;
    private final CategoryService categoryService;

    @Transactional
    public void createBudget(BudgetCreateRequest budgetCreateRequest, Member member) {
        List<Budget> budgets = makeBudgets(budgetCreateRequest, member);
        budgetRepository.saveAll(budgets);
    }

    @Transactional(readOnly = true)
    public Double getAverageRatioForCategory(Category category) {
        return budgetRepository.getAverageRatioForCategory(category);
    }

    private List<Budget> makeBudgets(BudgetCreateRequest budgetCreateRequest, Member member) {
        List<BudgetRequest> budgetRequests = budgetCreateRequest.getBudgetRequests();
        Long totalBudgetAmount = getTotalBudgetAmount(budgetRequests);
        List<Category> categories = categoryService.findAllCategories();
        return categories.stream()
                .map(category -> generateBudget(member, category, budgetRequests, totalBudgetAmount))
                .toList();
    }

    private Budget generateBudget(Member member, Category category, List<BudgetRequest> budgetRequests, Long totalBudgetAmount) {
        if (isRequestCategory(category, budgetRequests)) {
            BudgetRequest budgetRequest = getBudgetRequestByCategory(category, budgetRequests);
            return Budget.of(member, category, budgetRequest.getBudgetAmount(), totalBudgetAmount);
        }
        return Budget.of(member, category);
    }

    private BudgetRequest getBudgetRequestByCategory(Category category, List<BudgetRequest> budgetRequests) {
        return budgetRequests.stream()
                .filter(budgetRequest -> budgetRequest.getCategoryId().equals(category.getId()))
                .findFirst()
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_EXISTS));
    }

    private boolean isRequestCategory(Category category, List<BudgetRequest> budgetRequests) {
        return budgetRequests.stream()
                .anyMatch(budgetRequest -> budgetRequest.getCategoryId().equals(category.getId()));
    }

    private long getTotalBudgetAmount(List<BudgetRequest> budgetRequests) {
        return budgetRequests.stream()
                .mapToLong(BudgetRequest::getBudgetAmount)
                .sum();
    }
}
