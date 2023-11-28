package com.moneylog.api.budget.domain;

import com.moneylog.api.category.domain.Category;
import com.moneylog.api.common.domain.BaseEntity;
import com.moneylog.api.member.domain.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
@EqualsAndHashCode(of = {"id"}, callSuper = false)
public class Budget extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Category category;

    @Column(nullable = false)
    private Long budgetAmount;

    @Column(nullable = false)
    private Double ratio;

    protected Budget() {
    }

    @Builder
    private Budget(Member member, Category category, Long budgetAmount, Double ratio) {
        this.member = member;
        this.category = category;
        this.budgetAmount = budgetAmount;
        this.ratio = ratio;
    }

    public static Budget of(Member member, Category category, Long budgetAmount, Long totalBudgetAmount) {
        Double ratio = (double) budgetAmount / totalBudgetAmount * 100;
        return Budget.builder()
                .member(member)
                .category(category)
                .budgetAmount(budgetAmount)
                .ratio(ratio)
                .build();
    }

    public static Budget of(Member member, Category category) {
        return Budget.builder()
                .member(member)
                .category(category)
                .budgetAmount(0L)
                .ratio(0.0d)
                .build();
    }
}
