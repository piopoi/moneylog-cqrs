package com.moneylog.api.budget.repository;

import com.moneylog.api.budget.domain.Budget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
