package com.moneylog.api.expense.repository;

import com.moneylog.api.expense.domain.Expense;
import com.moneylog.api.expense.dto.ExpenseGetListRequest;
import java.util.List;

public interface ExpenseRepositoryCustom {

    List<Expense> findByFilter(Long memberId, ExpenseGetListRequest expenseGetListRequest);
}
