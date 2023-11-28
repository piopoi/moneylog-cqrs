package com.moneylog.api.category.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneylog.api.category.dto.CategoryGetResponse;
import com.moneylog.api.category.repository.CategoryRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("모든 카테고리를 조회할 수 있다.")
    void getAllCategory() {
        //when
        List<CategoryGetResponse> categoryGetResponses = categoryService.getAllCategories();

        //then
        int categoriesSize = categoryRepository.findAll().size();
        assertThat(categoryGetResponses.size()).isEqualTo(categoriesSize);
    }
}
