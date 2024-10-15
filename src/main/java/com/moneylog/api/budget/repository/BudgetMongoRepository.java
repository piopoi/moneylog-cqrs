package com.moneylog.api.budget.repository;

import com.moneylog.api.budget.domain.BudgetDocument;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BudgetMongoRepository extends MongoRepository<BudgetDocument, Long> {

    Optional<BudgetDocument> findById(Long memberId);
}
