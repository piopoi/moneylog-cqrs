package com.moneylog.api.expense.controller;

import static com.moneylog.api.exception.domain.ErrorCode.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class ExpenseControllerTest {

    private final String requestUri = "/api/expenses";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("지출을 등록할 수 있다.")
    void createExpense() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expendedAt", "2023-11-01T12:30");
        params.put("expenseAmount", 10000);
        params.put("categoryId", 1);
        params.put("memo", "");
        params.put("isExcludeTotal", true);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("잘못된 지출일시로 지출을 등록할 수 없다.")
    void createExpense_invalidExpendedAt() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expendedAt", "2023-11-01T12:99");
        params.put("expenseAmount", 10000);
        params.put("categoryId", 1);
        params.put("memo", "");
        params.put("isExcludeTotal", true);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("지출일시 없이 지출을 등록할 수 없다.")
    void createExpense_emptyExpendedAt() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expenseAmount", 10000);
        params.put("categoryId", 1);
        params.put("memo", "");
        params.put("isExcludeTotal", true);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(EXPENSE_EXPENDEDAT_EMPTY.name()));
    }

    @Test
    @WithMockUser
    @DisplayName("지출액 없이 지출을 등록할 수 없다.")
    void createExpense_emptyExpenseAmount() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expendedAt", "2023-11-01T12:00");
        params.put("categoryId", 1);
        params.put("memo", "");
        params.put("isExcludeTotal", true);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(EXPENSE_EXPENSEAMOUNT_EMPTY.name()));
    }

    @Test
    @WithMockUser
    @DisplayName("카테고리 없이 지출을 등록할 수 없다.")
    void createExpense_emptyCategory() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expendedAt", "2023-11-01T12:00");
        params.put("expenseAmount", 10000);
        params.put("memo", "");
        params.put("isExcludeTotal", true);

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(CATEGORY_EMPTY.name()));
    }

    @Test
    @WithMockUser
    @DisplayName("합계제외여부 없이 지출을 등록할 수 없다.")
    void createExpense_emptyIsExcludeTotal() throws Exception {
        //given
        Map<String, Object> params = new HashMap<>();
        params.put("expendedAt", "2023-11-01T12:00");
        params.put("expenseAmount", 10000);
        params.put("categoryId", 1);
        params.put("memo", "");

        //when then
        mockMvc.perform(post(requestUri)
                        .accept(APPLICATION_JSON)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(params))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(EXPENSE_ISEXCLUDETOTAL_EMPTY.name()));
    }
}
