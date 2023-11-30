package com.moneylog.api.budget.service;

import static com.moneylog.api.exception.domain.ErrorCode.CATEGORY_NOT_EXISTS;

import com.moneylog.api.budget.dto.BudgetRecommendRequest;
import com.moneylog.api.budget.dto.BudgetRecommendResponse;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import com.moneylog.api.exception.domain.CustomException;
import com.moneylog.api.util.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetRecommendService {

    public static final Long MIN_RECOMMEND_RATIO = 10L;
    public static final String ETC_CATEGORY_NAME = "기타";

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<BudgetRecommendResponse> recommendBudget(BudgetRecommendRequest budgetRecommendRequest) {
        Long totalAmount = budgetRecommendRequest.getTotalAmount();
        List<Category> categories = categoryRepository.findByAverageRatioGreaterThanAndNameNot(MIN_RECOMMEND_RATIO, ETC_CATEGORY_NAME);
        List<BudgetRecommendResponse> budgetRecommendResponses = categories.stream()
                .map(category -> generateRecommendBudgetAmount(category, totalAmount))
                .collect(Collectors.toCollection(ArrayList::new));
        addEtcCategoryBudgetAmount(budgetRecommendResponses, totalAmount);
        return budgetRecommendResponses;
    }

    private BudgetRecommendResponse generateRecommendBudgetAmount(Category category, Long totalAmount) {
        Long budgetAmount = calculateBudgetAmountByCategory(category, totalAmount);
        budgetAmount = MathUtils.floorHundredBelow(budgetAmount);
        return BudgetRecommendResponse.of(category, budgetAmount);
    }

    private long calculateBudgetAmountByCategory(Category category, Long totalAmount) {
        return category.getAverageRatio() * totalAmount / 100;
    }

    private void addEtcCategoryBudgetAmount(List<BudgetRecommendResponse> budgetRecommendResponses, Long totalAmount) {
        for (BudgetRecommendResponse budgetRecommendResponse : budgetRecommendResponses) {
            totalAmount -= budgetRecommendResponse.getBudgetAmount();
        }
        Category etcCategory = findEtcCategory();
        BudgetRecommendResponse etcCategoryBudgetRecommendResponse = BudgetRecommendResponse.of(etcCategory, totalAmount);
        budgetRecommendResponses.add(etcCategoryBudgetRecommendResponse);
    }

    private Category findEtcCategory() {
        return categoryRepository.findByName(ETC_CATEGORY_NAME)
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_EXISTS));
    }
}
