package com.moneylog.api.category.service;

import com.moneylog.api.category.dto.CategoryGetResponse;
import com.moneylog.api.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public List<CategoryGetResponse> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryGetResponse::from)
                .toList();
    }
}
