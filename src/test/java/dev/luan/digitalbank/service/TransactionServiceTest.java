package dev.luan.digitalbank.service;

import dev.luan.digitalbank.entities.Account;
import dev.luan.digitalbank.entities.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class TransactionServiceTest {

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


}
