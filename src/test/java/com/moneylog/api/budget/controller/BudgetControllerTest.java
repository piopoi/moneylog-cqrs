package com.moneylog.api.budget.controller;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetCreateRequest.BudgetRequest;
import com.moneylog.api.category.domain.Category;
import com.moneylog.api.category.repository.CategoryRepository;
import com.moneylog.api.member.domain.Member;
import com.moneylog.api.member.repository.MemberRepository;
import java.util.Arrays;
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

    private Category foodCategory;
    private Category transportationCategory;
    private Category etcCategory;

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
                .andExpect(jsonPath("$.code").value(BUDGET_REQUEST_EMPTY.name()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("존재하지 않는 카테고리로 예산을 설정할 수 없다.")
    void createBudget_notExistsCategory() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(999L)
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
                .andExpect(jsonPath("$.code").value(BUDGET_CATEGORY_NOT_EXISTS.name()));
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
                .andExpect(jsonPath("$.code").value(BUDGET_CATEGORY_EMPTY.name()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("예산액 없이 예산을 설정할 수 없다.")
    void createBudget_emptyBudgetAmount() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(foodCategory.getId())
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
                .andExpect(jsonPath("$.code").value(BUDGET_BUDGETAMOUNT_EMPTY.name()));
    }

    @Test
    @WithUserDetails(value = email, setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @DisplayName("0 미만의 예산액으로 예산을 설정할 수 없다.")
    void createBudget_minusBudgetAmount() throws Exception {
        //given
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(foodCategory.getId())
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
                .andExpect(jsonPath("$.code").value(BUDGET_BUDGETAMOUNT_INVALID.name()));
    }

    private BudgetCreateRequest createBudgetCreateRequest() {
        BudgetRequest budgetRequest1 = BudgetRequest.builder()
                .categoryId(foodCategory.getId())
                .budgetAmount(300000L)
                .build();
        BudgetRequest budgetRequest2 = BudgetRequest.builder()
                .categoryId(transportationCategory.getId())
                .budgetAmount(200000L)
                .build();
        BudgetRequest budgetRequest3 = BudgetRequest.builder()
                .categoryId(etcCategory.getId())
                .budgetAmount(500000L)
                .build();
        return BudgetCreateRequest.builder()
                .budgetRequests(Arrays.asList(budgetRequest1, budgetRequest2, budgetRequest3))
                .build();
    }

    private void getTestCategories() {
        foodCategory = categoryRepository.findById(1L).orElseThrow();
        transportationCategory = categoryRepository.findById(2L).orElseThrow();
        etcCategory = categoryRepository.findById(7L).orElseThrow();
    }

    private void createTestMember() {
        memberRepository.save(Member.builder()
                .email(email)
                .password(passwordEncoder.encode("password"))
                .build());
    }
}
