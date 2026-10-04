package dev.luan.digitalbank.service;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.repositories.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

        return new AccountDTO(
            savedAccount.getId(),
            savedAccount.getAccountNumber(),
            savedAccount.getHolderName(),
            savedAccount.getBalance(),
            savedAccount.getStatus(),
            savedAccount.getDocument()
        );
    }

    public AccountDTO findById(Long id) {
        return toDTO(findEntityById(id));
    }

    private Account findEntityById(Long id) {
        Optional<Account> obj = accountRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public AccountDTO block(Long id){
        Account account = findEntityById(id);
        account.setStatus(AccountStatus.BLOCKED);

        return toDTO(accountRepository.save(account));
    }

    public AccountDTO unblock(Long id){
        Account account = findEntityById(id);
        account.setStatus(AccountStatus.ACTIVE);

        return toDTO(accountRepository.save(account));
    }

    @Transactional
    public AccountDTO deposit(Long id, BigDecimal amount) {
        Account account = findEntityById(id);
        if(amount == null) {
            throw new RuntimeException("Amount cannot be null");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        if(account.getStatus().equals(AccountStatus.BLOCKED) || account.getStatus().equals(AccountStatus.CLOSED)) {
            throw new RuntimeException("Account is blocked");
        }
        account.setBalance(account.getBalance().add(amount));
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
        if(account.getStatus().equals(AccountStatus.BLOCKED) || account.getStatus().equals(AccountStatus.CLOSED)) {
            throw new RuntimeException("Account is blocked");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return toDTO(accountRepository.save(account));
    }
}
