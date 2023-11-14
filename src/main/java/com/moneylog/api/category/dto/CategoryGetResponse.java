package com.moneylog.api.category.dto;

import com.moneylog.api.category.domain.Category;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryGetResponse {

    private final Long categoryId;
    private final String categoryName;

    public static CategoryGetResponse from(Category category) {
        return CategoryGetResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }
}
