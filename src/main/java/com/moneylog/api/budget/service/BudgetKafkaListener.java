package com.moneylog.api.budget.service;

import com.moneylog.api.budget.domain.BudgetDocument;
import com.moneylog.api.budget.repository.BudgetMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BudgetKafkaListener {

    private final BudgetMongoRepository budgetMongoRepository; // MongoDB Repository

    @KafkaListener(topics = "budget-events", groupId = "budget-group")
    public void handleBudgetEvent(BudgetDocument budgetDocument) {
        // Kafka로부터 수신한 Budget 데이터를 MongoDB에 저장
        budgetMongoRepository.save(budgetDocument);
        System.out.println("## BudgetDocument synced to MongoDB: " + budgetDocument);
    }
}
