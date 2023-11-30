package com.moneylog.api.category.service;

import static com.moneylog.api.exception.domain.ErrorCode.CATEGORY_NOT_EXISTS;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.dto.CategoryGetResponse;
import com.moneylog.api.category.repository.CategoryRepository;
import com.moneylog.api.exception.domain.CustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryGetResponse> getAllCategories() {
        return findAllCategories().stream()
                .map(CategoryGetResponse::from)
                .toList();
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public Category findCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(CATEGORY_NOT_EXISTS));
    }
}
