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

    protected Budget() {
    }

    @Builder
    private Budget(Member member, Category category, Long budgetAmount) {
        this.member = member;
        this.category = category;
        this.budgetAmount = budgetAmount;
    }

    public static Budget of(Member member, Category category, Long budgetAmount) {
        return Budget.builder()
                .member(member)
                .category(category)
                .budgetAmount(budgetAmount)
                .build();
    }
}
