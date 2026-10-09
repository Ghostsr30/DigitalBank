package dev.luan.digitalbank.service;

import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.exceptions.InsufficientBalanceException;
import dev.luan.digitalbank.exceptions.InvalidAmountException;
import dev.luan.digitalbank.exceptions.SameAccountTransferException;
import dev.luan.digitalbank.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransferService {

    final TransactionService transactionService;
    final AccountRepository accountRepository;

    public TransferService(TransactionService transactionService, AccountRepository accountRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void transfer(Long sourceId, Long targetId, BigDecimal amount) {

        if(amount == null){
            throw new InvalidAmountException("Amount must not be null");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }

        if (sourceId.equals(targetId)) {
            throw new SameAccountTransferException("Source and target accounts must be different");
        }

        Account sourceAccount = accountRepository.findById(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("Source account not found"));
        Account targetAccount = accountRepository.findById(targetId)
                .orElseThrow(() -> new IllegalArgumentException("Target account not found"));

        if(sourceAccount.getStatus().equals(AccountStatus.BLOCKED) || sourceAccount.getStatus().equals(AccountStatus.CLOSED)) {
            throw new IllegalArgumentException("Source or target account is not active");
        }

        if(targetAccount.getStatus().equals(AccountStatus.BLOCKED) || targetAccount.getStatus().equals(AccountStatus.CLOSED)) {
            throw new IllegalArgumentException("Source or target account is not active");
        }

        if(sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Source account balance is not enough");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        transactionService.registerTransfer(amount, sourceAccount, targetAccount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
