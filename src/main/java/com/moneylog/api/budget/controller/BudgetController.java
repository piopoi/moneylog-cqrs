package com.moneylog.api.budget.controller;

import com.moneylog.api.auth.domain.MemberAdapter;
import com.moneylog.api.budget.dto.BudgetCreateRequest;
import com.moneylog.api.budget.dto.BudgetRecommendRequest;
import com.moneylog.api.budget.dto.BudgetRecommendResponse;
import com.moneylog.api.budget.service.BudgetRecommendService;
import com.moneylog.api.budget.service.BudgetService;
import com.moneylog.api.member.domain.Member;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BudgetController implements BudgetControllerDocs {

    private final BudgetService budgetService;
    private final BudgetRecommendService budgetRecommendService;

    @PostMapping("/api/budget")
    public ResponseEntity<Void> createBudget(@RequestBody @Valid BudgetCreateRequest budgetCreateRequest,
                                             @AuthenticationPrincipal MemberAdapter memberAdapter) {
        Member member = memberAdapter.getMember();
        budgetService.createBudget(budgetCreateRequest, member);
        return ResponseEntity.created(URI.create("/api/budget")).build();
    }

    @GetMapping("/api/budget/recommend")
    public ResponseEntity<List<BudgetRecommendResponse>> recommendBudget(@RequestBody @Valid BudgetRecommendRequest budgetRecommendRequest) {
        List<BudgetRecommendResponse> budgetRecommendResponses = budgetRecommendService.recommendBudget(budgetRecommendRequest);
        return ResponseEntity.ok(budgetRecommendResponses);
    }
}
