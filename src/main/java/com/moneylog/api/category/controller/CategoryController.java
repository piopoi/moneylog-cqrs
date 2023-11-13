package com.moneylog.api.category.controller;

import com.moneylog.api.category.dto.CategoryGetResponse;
import com.moneylog.api.category.service.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryGetResponse>> getAllCategory() {
        List<CategoryGetResponse> categoryGetResponses = categoryService.getAllCategory();
        return ResponseEntity.ok(categoryGetResponses);
    }
}
