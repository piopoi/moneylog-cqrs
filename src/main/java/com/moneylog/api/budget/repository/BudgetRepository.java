package com.moneylog.api.budget.repository;

import com.moneylog.api.budget.domain.Budget;
import com.moneylog.api.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    @Query("SELECT AVG(b.ratio) FROM Budget b WHERE b.category = :category")
    Double getAverageRatioForCategory(@Param("category") Category category);
}
