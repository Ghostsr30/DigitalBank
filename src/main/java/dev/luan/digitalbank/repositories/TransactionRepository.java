package dev.luan.digitalbank.repositories;

import dev.luan.digitalbank.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findBySourceAccountIdOrTargetAccountIdOrderByCreatedAtDesc(Long sourceId, Long targetId);
}
