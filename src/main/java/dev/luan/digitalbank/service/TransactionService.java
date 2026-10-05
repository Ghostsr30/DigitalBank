package dev.luan.digitalbank.service;

import dev.luan.digitalbank.DTO.TransactionDTO;
import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.domain.TransactionType;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.Transaction;
import dev.luan.digitalbank.repositories.AccountRepository;
import dev.luan.digitalbank.repositories.TransactionRepository;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    private TransactionDTO toDTO(Transaction transaction){
        return new TransactionDTO(
                transaction.getId(),
                transaction.getType(),
                transaction.getAmount(),
                transaction.getCreatedAt(),
                transaction.getSourceAccount() != null ? transaction.getSourceAccount().getId() : null,
                transaction.getTargetAccount() != null ? transaction.getTargetAccount().getId() : null
        );
    }

    @Transactional(readOnly = true)
    public List<TransactionDTO> extract(Long id){

        if(accountRepository.findById(id).isEmpty()) {
            throw new RuntimeException("Account not found");
        }

        return transactionRepository.findBySourceAccountIdOrTargetAccountIdOrderByCreatedAtDesc(id, id)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    public void registerDeposit(BigDecimal amount, Account account){
        Transaction transaction = Transaction.deposit(amount, account, null);
        transactionRepository.save(transaction);
    }

    public void registerWithdrawal(Long id, BigDecimal amount, Account account){
        Transaction transaction = Transaction.withdrawal(amount, account, null);
        transactionRepository.save(transaction);
    }

    public void registerTransfer(BigDecimal amount, Account sourceAccount, Account targetAccount){
        Transaction transaction = Transaction.transfer(amount, sourceAccount, targetAccount);
        transactionRepository.save(transaction);
    }

}
