package com.moneylog.api.category.service;

import com.moneylog.api.budget.service.BudgetService;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryBatchService {

    private final CategoryRepository categoryRepository;
    private final BudgetService budgetService;

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void updateAverageRatioForAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
            Double averageRatio = budgetService.getAverageRatioForCategory(category);
            category.updateAverageRatio(Math.floor(averageRatio)); //소수점 이하 버림
        }
    }
}
