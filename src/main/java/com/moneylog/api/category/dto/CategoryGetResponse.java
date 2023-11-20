package com.moneylog.api.category.dto;

import com.moneylog.api.category.domain.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryGetResponse {

    @Schema(description = "카테고리 아이디", example = "1")
    private final Long categoryId;

    @Schema(description = "카테고리 이름", example = "FOOT")
    private final String categoryName;

    public static CategoryGetResponse from(Category category) {
        return CategoryGetResponse.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }
}
