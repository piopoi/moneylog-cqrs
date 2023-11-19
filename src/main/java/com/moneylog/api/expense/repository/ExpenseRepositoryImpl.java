package com.moneylog.api.expense.repository;

import com.moneylog.api.expense.domain.Expense;
import com.moneylog.api.expense.domain.QExpense;
import com.moneylog.api.expense.dto.ExpenseGetListRequest;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ExpenseRepositoryImpl implements ExpenseRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Expense> findByFilter(Long memberId, ExpenseGetListRequest request) {
        QExpense expense = QExpense.expense;
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(expense.member.id.eq(memberId));

        Long categoryId = request.getCategoryId();
        if (Objects.nonNull(categoryId)) {
            builder.and(expense.category.id.eq(categoryId));
        }

        Long amountMin = request.getExpenseAmountMin();
        if (Objects.nonNull(amountMin)) {
            builder.and(expense.expenseAmount.goe(amountMin));
        }

        Long amountMax = request.getExpenseAmountMax();
        if (Objects.nonNull(amountMax)) {
            builder.and(expense.expenseAmount.loe(amountMax));
        }

        LocalDateTime startAt = request.getExpendedStartAt();
        if (Objects.nonNull(startAt)) {
            builder.and(expense.expendedAt.goe(startAt));
        }

        LocalDateTime endAt = request.getExpendedEndAt();
        if (Objects.nonNull(endAt)) {
            builder.and(expense.expendedAt.loe(endAt));
        }

        return jpaQueryFactory
                .selectFrom(expense)
                .where(builder)
                .orderBy(expense.expendedAt.desc())
                .fetch();
    }
}
