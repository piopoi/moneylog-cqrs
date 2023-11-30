package com.moneylog.api.budget.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.moneylog.api.budget.domain.Budget;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetCreateRequest.BudgetRequest;
import com.moneylog.api.budget.repository.BudgetRepository;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class BudgetServiceTest {

    @Autowired
    private BudgetService budgetService;
    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Member member;
    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {
        createTestMember();
        getTestCategories();
    }

    @Test
    @DisplayName("예산을 설정할 수 있다.")
    void createBudget() {
        //given
        BudgetCreateRequest budgetCreateRequest = createBudgetCreateRequest();

        //when
        budgetService.createBudget(budgetCreateRequest, member);

        //then
        List<Budget> budgets = budgetRepository.findAll();
        assertThat(budgets.size()).isEqualTo(categoryRepository.count());
    }

    private BudgetCreateRequest createBudgetCreateRequest() {
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(category1.getId())
                .budgetAmount(300000L)
                .build();
        BudgetRequest budgetRequest2 = BudgetRequest.builder()
                .categoryId(category2.getId())
                .budgetAmount(200000L)
                .build();
        BudgetRequest budgetRequest3 = BudgetRequest.builder()
                .categoryId(category3.getId())
                .budgetAmount(500000L)
                .build();
        return BudgetCreateRequest.builder()
                .budgetRequests(Arrays.asList(budgetRequest1, budgetRequest2, budgetRequest3))
                .build();
    }

    private void getTestCategories() {
        List<Category> categories = categoryRepository.findAll();
        category1 = categories.get(0);
        category2 = categories.get(1);
        category3 = categories.get(2);
    }

    private void createTestMember() {
        member = memberRepository.save(Member.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("password"))
                .build());
    }
}
