package dev.luan.digitalbank.service;

import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.exceptions.*;
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

        if (java.util.Objects.equals(sourceId, targetId)) {
            throw new SameAccountTransferException("Source and target accounts must be different");
        }

        Account sourceAccount = accountRepository.findById(sourceId)
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));
        Account targetAccount = accountRepository.findById(targetId)
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));

        if(sourceAccount.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new AccountBlockedException("Source account is blocked");
        }

        if (sourceAccount.getStatus().equals(AccountStatus.CLOSED)){
            throw new AccountClosedException("Source account is closed");
        }

        if(targetAccount.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new AccountBlockedException("Target account is blocked");
        }
        if(targetAccount.getStatus().equals(AccountStatus.CLOSED)){
            throw new AccountClosedException("Target account is closed");
        }

        if(sourceAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Source account balance is not enough");
        }

        sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
        targetAccount.setBalance(targetAccount.getBalance().add(amount));

        transactionService.registerTransfer(amount, sourceAccount, targetAccount);

        accountRepository.save(sourceAccount);
        accountRepository.save(targetAccount);
    }
}
