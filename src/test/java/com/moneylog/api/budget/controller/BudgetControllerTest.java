package com.moneylog.api.budget.controller;

import static com.moneylog.api.budget.service.BudgetRecommendService.ETC_CATEGORY_NAME;
import static com.moneylog.api.budget.service.BudgetRecommendService.MIN_RECOMMEND_RATIO;
import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetCreateRequest.BudgetRequest;
import com.moneylog.api.budget.dto.BudgetRecommendRequest;
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
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class BudgetControllerTest {

    private final String requestUri = "/api/budget";
    private final String email = "test@test.com";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Category category1;
    private Category category2;
    private Category category3;

    @BeforeEach
    void setUp() {
        createTestMember();
        getTestCategories();
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("예산을 설정할 수 있다.")
    void createBudget() throws Exception {
        //given
        BudgetCreateRequest budgetCreateRequest = createBudgetCreateRequest();

        //when then
        mockMvc.perform(post(requestUri)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("요청에 포함된 예산 설정이 0건인 경우 없이 예산을 설정할 수 없다.")
    void createBudget_emptyBudgetRequest() throws Exception {
        //given
        BudgetCreateRequest budgetCreateRequest = BudgetCreateRequest.builder()
                .budgetRequests(null)
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BUDGET_REQUEST_EMPTY.getMessage()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("카테고리 없이 예산을 설정할 수 없다.")
    void createBudget_emptyCategory() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .budgetAmount(100000L)
                .build();
        BudgetCreateRequest budgetCreateRequest = BudgetCreateRequest.builder()
                .budgetRequests(Arrays.asList(budgetRequest1))
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(CATEGORY_EMPTY.getMessage()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("예산액 없이 예산을 설정할 수 없다.")
    void createBudget_emptyBudgetAmount() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(category1.getId())
                .build();
        BudgetCreateRequest budgetCreateRequest = BudgetCreateRequest.builder()
                .budgetRequests(Arrays.asList(budgetRequest1))
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BUDGET_BUDGETAMOUNT_EMPTY.getMessage()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("0 미만의 예산액으로 예산을 설정할 수 없다.")
    void createBudget_minusBudgetAmount() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(category1.getId())
                .budgetAmount(-1L)
                .build();
        BudgetCreateRequest budgetCreateRequest = BudgetCreateRequest.builder()
                .budgetRequests(Arrays.asList(budgetRequest1))
                .build();

        //when then
        mockMvc.perform(post(requestUri)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetCreateRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BUDGET_BUDGETAMOUNT_INVALID.getMessage()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("카테고리 별 예산을 추천할 수 있다.")
    void recommendBudget() throws Exception {
        //given
        BudgetRecommendRequest budgetRecommendRequest = BudgetRecommendRequest.builder()
                .totalAmount(1000000L)
                .build();
        List<Category> categories = categoryRepository.findByAverageRatioGreaterThanAndNameNot(MIN_RECOMMEND_RATIO, ETC_CATEGORY_NAME);

        //when then
        mockMvc.perform(get(requestUri + "/recommend")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRecommendRequest))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size() + 1));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("예산총액 없이 카테고리 별 예산을 추천할 수 없다.")
    void recommendBudget_emptyTotalAmount() throws Exception {
        //given
        BudgetRecommendRequest budgetRecommendRequest = BudgetRecommendRequest.builder()
                .build();

        //when then
        mockMvc.perform(get(requestUri + "/recommend")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRecommendRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BUDGET_TOTALAMOUNT_EMPTY.getMessage()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("잘못된 예산총액으로 카테고리 별 예산을 추천할 수 없다.")
    void recommendBudget_invalidTotalAmount() throws Exception {
        //given
        BudgetRecommendRequest budgetRecommendRequest = BudgetRecommendRequest.builder()
                .totalAmount(9999L)
                .build();

        //when then
        mockMvc.perform(get(requestUri + "/recommend")
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(budgetRecommendRequest))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(BUDGET_TOTALAMOUNT_INVALID.getMessage()));
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
        memberRepository.save(Member.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .build());
    }
}
