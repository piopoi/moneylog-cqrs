package com.moneylog.api.expense.domain;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.common.domain.BaseEntity;
import com.moneylog.api.expense.dto.ExpenseCreateRequest;
import com.moneylog.api.expense.dto.ExpenseUpdateRequest;
import com.moneylog.api.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;
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

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

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
    private Expense(Member member, LocalDateTime expendedAt, Long expenseAmount, Category category, String memo, Boolean isExcludeTotal) {
        this.member = member;
        this.expendedAt = expendedAt;
        this.expenseAmount = expenseAmount;
        this.category = category;
        this.memo = memo;
        this.isExcludeTotal = isExcludeTotal;
    }

    public static Expense of(Member member, ExpenseCreateRequest expenseCreateRequest, Category category) {
        return Expense.builder()
                .member(member)
                .expendedAt(expenseCreateRequest.getExpendedAt())
                .expenseAmount(expenseCreateRequest.getExpenseAmount())
                .category(category)
                .memo(expenseCreateRequest.getMemo())
                .isExcludeTotal(expenseCreateRequest.getIsExcludeTotal())
                .build();
    }

    public boolean isRegister(Member member) {
        return Objects.equals(this.member, member);
    }

    public void update(ExpenseUpdateRequest expenseUpdateRequest, Category category) {
        updateExpendedAt(expenseUpdateRequest.getExpendedAt());
        updateExpenseAmount(expenseUpdateRequest.getExpenseAmount());
        updateCategory(category);
        updateMemo(expenseUpdateRequest.getMemo());
        updateIsExcludeTotal(expenseUpdateRequest.getIsExcludeTotal());
    }

    private void updateExpendedAt(LocalDateTime expendedAt) {
        if (!Objects.isNull(expendedAt)) {
            this.expendedAt = expendedAt;
        }
    }

    private void updateExpenseAmount(Long expenseAmount) {
        if (!Objects.isNull(expenseAmount)) {
            this.expenseAmount = expenseAmount;
        }
    }

    private void updateCategory(Category category) {
        if (!Objects.isNull(category)) {
            this.category = category;
        }
    }

    private void updateMemo(String memo) {
        if (!Objects.isNull(memo)) {
            this.memo = memo;
        }
    }

    private void updateIsExcludeTotal(Boolean isExcludeTotal) {
        if (!Objects.isNull(isExcludeTotal)) {
            this.isExcludeTotal = isExcludeTotal;
        }
    }
}
