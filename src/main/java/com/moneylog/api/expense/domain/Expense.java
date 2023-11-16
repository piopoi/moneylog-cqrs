package com.moneylog.api.expense.domain;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.common.domain.BaseEntity;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Expense extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime expendedAt;

    @Column(nullable = false)
    private Long expenseAmount;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    private String memo;

    @Column(nullable = false)
    private Boolean isExcludeTotal = false;

    public Expense() {
    }

    @Builder
    private Expense(LocalDateTime expendedAt, Long expenseAmount, Category category, String memo, Boolean isExcludeTotal) {
        this.expendedAt = expendedAt;
        this.expenseAmount = expenseAmount;
        this.category = category;
        this.memo = memo;
        this.isExcludeTotal = isExcludeTotal;
    }

    public static Expense of(ExpenseCreateRequest expenseCreateRequest, Category category) {
        return Expense.builder()
                .expendedAt(expenseCreateRequest.getExpendedAt())
                .expenseAmount(expenseCreateRequest.getExpenseAmount())
                .category(category)
                .memo(expenseCreateRequest.getMemo())
                .isExcludeTotal(expenseCreateRequest.getIsExcludeTotal())
                .build();
    }
}
