package com.moneylog.api.category.service;

import com.moneylog.api.category.dto.CategoryGetResponse;
import com.moneylog.api.category.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<CategoryGetResponse> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryGetResponse::from)
                .toList();
    }
}
