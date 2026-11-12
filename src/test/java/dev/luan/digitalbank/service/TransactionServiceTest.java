package dev.luan.digitalbank.service;

import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.domain.UserRole;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.Transaction;
import dev.luan.digitalbank.entities.User;
import dev.luan.digitalbank.exceptions.AccountNotFoundException;
import dev.luan.digitalbank.repositories.AccountRepository;
import dev.luan.digitalbank.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {


    @InjectMocks
    TransactionService transactionService;

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @Test
    void registerDeposit(){
        Account account = new Account();

        BigDecimal amount = new BigDecimal("100");
        Transaction transaction = Transaction.deposit(amount, account, null);

        Assertions.assertEquals(transaction.getAmount(), amount);
    }

    @Test
    void registerWithdrawal(){
        Account account = new Account();

        BigDecimal amount = new BigDecimal("100");
        Transaction transaction = Transaction.withdrawal(amount, account, null);

        Assertions.assertEquals(transaction.getAmount(), amount);
    }

    @Test
    void registerTransfer(){
        Account account = new Account();
        Account account2 = new Account();

        Transaction transaction = Transaction.transfer(new BigDecimal("100"), account, account2);

        Assertions.assertEquals(new BigDecimal("100"), transaction.getAmount());
    }

    @Test
    void extractEmptyList(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        transactionService.extract(1L);

        verify(transactionRepository, times(1)).findBySourceAccountIdOrTargetAccountIdOrderByCreatedAtDesc(1L, 1L);
    }

    @Test
    void extractAccountNotFoundException(){
        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            transactionService.extract(1L);
        });
    }
}
