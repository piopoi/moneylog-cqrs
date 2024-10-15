package com.moneylog.api.budget.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
public class BudgetDocument {

    @Id
    private Long memberId;
    private List<Budget> budgets;

    protected BudgetDocument() {}

    @Builder
    public BudgetDocument(Long memberId, List<Budget> budgets) {
        this.memberId = memberId;
        this.budgets = budgets;
    }
}
