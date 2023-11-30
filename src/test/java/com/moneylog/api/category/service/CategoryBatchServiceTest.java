package com.moneylog.api.category.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

@Sql(value = "/db/categoryBatchServiceData.sql")
@Transactional
@SpringBootTest
class CategoryBatchServiceTest {

    @Autowired
    private CategoryBatchService categoryBatchService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("모든 카테고리의 평균비율을 업데이트할 수 있다.")
    void updateAverageRatioForAllCategories() {
        //when
        categoryBatchService.updateAverageRatioForAllCategories();

        //then
        List<Category> categories = categoryRepository.findAll();
        assertThat(categories).allSatisfy(category -> assertThat(category.getAverageRatio()).isNotNull());
        assertThat(getSumAverageRatio(categories)).isLessThan(100L);
    }

    private static double getSumAverageRatio(List<Category> categories) {
        return categories.stream()
                .mapToLong(Category::getAverageRatio)
                .sum();
    }
}
