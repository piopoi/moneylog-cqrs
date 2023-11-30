package com.moneylog.api.category.repository;

import com.moneylog.api.category.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String categoryName);

    List<Category> findByAverageRatioGreaterThanAndNameNot(Long averageRatio, String name);
}
