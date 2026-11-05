package dev.luan.digitalbank.service;

import dev.luan.digitalbank.DTO.AccountDTO;
import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.domain.UserRole;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.User;
import dev.luan.digitalbank.exceptions.*;
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

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void withdrawalVerify(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(100));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.withdraw(1L, new BigDecimal(50));

        Assertions.assertEquals(new BigDecimal(50), account.getBalance());
    }

    @Test
    void withdrawalInvalidAmountExceptionNull(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(100));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        Assertions.assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(1L, null);
        });
    }

    @Test
    void withdrawalInvalidAmountException(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(100));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        Assertions.assertThrows(InvalidAmountException.class, () -> {
            accountService.withdraw(1L, new BigDecimal(0));
        });
    }

    @Test
    void withdrawalAccountBlockedException(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(100));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.block(1L);

        Assertions.assertThrows(AccountBlockedException.class, () -> {
            accountService.withdraw(1L, new BigDecimal(50));
        });
    }

    @Test
    void withdrawalAccountClosedException(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );

        accountService.closed(1L);

        Assertions.assertThrows(AccountClosedException.class, () -> {
            accountService.withdraw(1L, new BigDecimal(50));
        });
    }

    @Test
    void withdrawalInsufficientBalanceException(){
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

        Assertions.assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(1L, new BigDecimal(100));
        });
    }

    @Test
    void closedVerify(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.closed(1L);

        Assertions.assertEquals(AccountStatus.CLOSED, account.getStatus());
    }

    @Test
    void closedBalanceException(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(100));
        account.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));


        Assertions.assertThrows(AccountHasBalanceException.class, () -> {
            accountService.closed(1L);
        });
    }

    @Test
    void accountClosedException(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Assertions.assertThrows(AccountClosedException.class, () -> {
            accountService.closed(1L);
        });
    }

    @Test
    void blockedAccount(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.block(1L);

        Assertions.assertEquals(AccountStatus.BLOCKED, account.getStatus());
    }

    @Test
    void blockAccountClosedException(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Assertions.assertThrows(AccountClosedException.class, () -> {
            accountService.block(1L);
        });
    }

    @Test
    void unblockAccount(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.BLOCKED);

        when(accountRepository.save(account)).thenReturn(account);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        accountService.unblock(1L);

        Assertions.assertEquals(AccountStatus.ACTIVE, account.getStatus());
    }

    @Test
    void unblockAccountClosedException(){
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.CLOSED);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        Assertions.assertThrows(AccountClosedException.class, () -> {
            accountService.unblock(1L);
        });
    }

    @Test
    void findById(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, null)
        );

        AccountDTO accountDTO = accountService.findById(1L);

        Assertions.assertEquals(account.getId(), accountDTO.getId());
    }

    @Test
    void findByIdException(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setId(1L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);

        when(accountRepository.findById(1L)).thenReturn(Optional.empty());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, null)
        );

        Assertions.assertThrows(AccountNotFoundException.class, () -> {
            accountService.findById(1L);
        });
    }

    @Test
    void createAccountVerify(){
        User user = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account = new Account();
        account.setDocument("1234");
        account.setHolderName("luanVedovoto");
        account.setAccountNumber(5435435L);
        account.setBalance(new BigDecimal(0));
        account.setStatus(AccountStatus.ACTIVE);
        account.setUser(user);
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setDocument("1234");
        accountDTO.setHolderName("luanVedovoto");
        accountDTO.setAccountNumber(5435435L);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, null)
        );
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO dto = accountService.createAccount(accountDTO);

        Assertions.assertEquals(account.getHolderName(), dto.getHolderName());
    }
}
