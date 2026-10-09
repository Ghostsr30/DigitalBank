package dev.luan.digitalbank.service;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.exceptions.*;
import dev.luan.digitalbank.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }


    private AccountDTO toDTO(Account account) {
        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getHolderName(),
                account.getBalance(),
                account.getStatus(),
                account.getDocument()
        );
    }

    public AccountDTO createAccount(AccountDTO accountDTO) {
        Account account = new Account();
        account.setDocument(accountDTO.getDocument());
        account.setHolderName(accountDTO.getHolderName());
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setBalance(accountDTO.getBalance());
        account.setStatus(AccountStatus.ACTIVE);

        Account savedAccount = accountRepository.save(account);

        return toDTO(savedAccount);
    }

    public AccountDTO findById(Long id) {
            return toDTO(findEntityById(id));

    }

    private Account findEntityById(Long id) {
        Optional<Account> obj = accountRepository.findById(id);
        return obj.orElseThrow(() -> new AccountNotFoundException("Account not found"));
    }

    public AccountDTO block(Long id){
        Account account = findEntityById(id);
        account.setStatus(AccountStatus.BLOCKED);                //arrumar

        return toDTO(accountRepository.save(account));
    }

    public AccountDTO unblock(Long id){
        Account account = findEntityById(id);
        account.setStatus(AccountStatus.ACTIVE);         //arrumar

        return toDTO(accountRepository.save(account));
    }

    public AccountDTO closed(Long id){
        Account account = findEntityById(id);
        account.setStatus(AccountStatus.CLOSED);                //arrumar

        return toDTO(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO deposit(Long id, BigDecimal amount) {
        Account account = findEntityById(id);
        if(amount == null) {
            throw new InvalidAmountException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Amount must be greater than zero");
        }
        if(account.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new AccountBlockedException("Account is blocked");
        }

        if(account.getStatus().equals(AccountStatus.CLOSED)){
            throw new AccountClosedException("Account is closed");
        }
        account.setBalance(account.getBalance().add(amount));

        transactionService.registerDeposit(amount, account);

        return toDTO(accountRepository.save(account));

    }

    @Transactional
    public AccountDTO withdraw(Long id, BigDecimal amount) {
        Account account = findEntityById(id);
        if (amount == null){
            throw  new RuntimeException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        if(account.getStatus().equals(AccountStatus.BLOCKED)) {
            throw new AccountBlockedException("Account is blocked");
        }
        if(account.getStatus().equals(AccountStatus.CLOSED)){
            throw new AccountClosedException("Account is closed");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));

        transactionService.registerWithdrawal(id, amount, account);

        return toDTO(accountRepository.save(account));
    }
}
