package com.moneylog.api.budget.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneylog.api.budget.dto.BudgetRecommendRequest;
import com.moneylog.api.budget.dto.BudgetRecommendResponse;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class BudgetRecommendServiceTest {

    @Autowired
    private BudgetRecommendService budgetRecommendService;

    @Test
    @DisplayName("카테고리 별 예산을 추천할 수 있다.")
    void recommendBudget() {
        //given
        BudgetRecommendRequest budgetRecommendRequest = BudgetRecommendRequest.builder()
                .totalAmount(1000000L)
                .build();

        //when
        List<BudgetRecommendResponse> actualResponses = budgetRecommendService.recommendBudget(budgetRecommendRequest);

        //then
        assertThat(actualResponses.size()).isEqualTo(5);
        assertThat(calculateTotalBudgetAmount(actualResponses)).isEqualTo(1000000L);
    }

    private static double calculateTotalBudgetAmount(List<BudgetRecommendResponse> budgetRecommendResponses) {
        return budgetRecommendResponses.stream()
                .mapToLong(BudgetRecommendResponse::getBudgetAmount)
                .sum();
    }
}
