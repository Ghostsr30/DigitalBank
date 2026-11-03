package dev.luan.digitalbank.service;

import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.domain.UserRole;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.User;
import dev.luan.digitalbank.exceptions.AccountBlockedException;
import dev.luan.digitalbank.exceptions.AccountClosedException;
import dev.luan.digitalbank.exceptions.InvalidAmountException;
import dev.luan.digitalbank.repositories.AccountRepository;
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

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void depositVerify() {

        //given
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        //when
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.deposit(1L, new BigDecimal(50));

        //then
        Assertions.assertEquals(new BigDecimal(50), account.getBalance());
    }

    @Test
    void depositInvalidAmountException(){

        //given
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        //when
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        //then
        Assertions.assertThrows(InvalidAmountException.class, () -> {
            accountService.deposit(1L, new BigDecimal(0));
        });
    }

    @Test
    void depositAccountBlocked(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        //when
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.block(1L);

        //then
        Assertions.assertThrows(AccountBlockedException.class, () -> {
            accountService.deposit(1L, new BigDecimal(50));
        });
    }

    @Test
    void depositAccountClosedException(){
        //given
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        //when
        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.closed(1L);

        //then
        Assertions.assertThrows(AccountClosedException.class, () -> {
            accountService.deposit(1L, new BigDecimal(50));
        });
    }

}
