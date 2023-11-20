package com.moneylog.api.category.controller;

import com.moneylog.api.category.dto.CategoryGetResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "카테고리")
public interface CategoryControllerDocs {

    @Operation(summary = "전체 카테고리 조회", description = "모든 카테고리를 조회한다.")
    @ApiResponse(responseCode = "200", description = "OK")
    ResponseEntity<List<CategoryGetResponse>> getAllCategory();
}
