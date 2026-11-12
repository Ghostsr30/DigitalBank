package dev.luan.digitalbank.service;

import dev.luan.digitalbank.domain.AccountStatus;
import dev.luan.digitalbank.domain.UserRole;
import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.User;
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

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferServiceTest {

    @InjectMocks
    private TransferService transferService;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void transferVerify(){
        User user1 = new User(1L, "luan@gmail.com", "1234", UserRole.USER);
        Account account1 = new Account();
        account1.setId(1L);
        account1.setBalance(new BigDecimal(100));
        account1.setStatus(AccountStatus.ACTIVE);
        account1.setUser(user1);

        User user2 = new User(2L, "maria@gmail.com", "456", UserRole.USER);
        Account account2 = new Account();
        account2.setId(2L);
        account2.setBalance(new BigDecimal(0));
        account2.setStatus(AccountStatus.ACTIVE);
        account2.setUser(user2);

        when(accountRepository.findById(1L)).thenReturn(Optional.of(account1));
        when(accountRepository.findById(2L)).thenReturn(Optional.of(account2));

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user1, null, user1.getAuthorities())
        );


        transferService.transfer(1L, 2L, new BigDecimal(100));

        Assertions.assertEquals(new BigDecimal(100), account2.getBalance());
        verify(transactionService, times(1))
                .registerTransfer(new BigDecimal(100),account1,account2);
    }

}
