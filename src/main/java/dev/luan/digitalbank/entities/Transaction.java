package dev.luan.digitalbank.entities;

import dev.luan.digitalbank.domain.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_account_transaction")
    private Account sourceAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_account_transaction")
    private Account targetAccount;

    private LocalDate createdAt;

    private Transaction(TransactionType type, BigDecimal amount, Account sourceAccount, Account targetAccount, LocalDate createdAt) {
        this.type = type;
        this.amount = amount;
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.createdAt = LocalDate.now();
    }

    public static Transaction deposit(BigDecimal amount, Account sourceAccount, Account targetAccount) {
        return new Transaction(TransactionType.DEPOSIT, amount, targetAccount, sourceAccount, LocalDate.now());
    }

    public static Transaction withdrawal(BigDecimal amount, Account sourceAccount, Account targetAccount) {
        return new Transaction(TransactionType.WITHDRAW, amount, sourceAccount, targetAccount, LocalDate.now());
    }

    public static Transaction transfer(BigDecimal amount, Account sourceAccount, Account targetAccount) {
        return new Transaction(TransactionType.TRANSFER, amount, sourceAccount, targetAccount, LocalDate.now());
    }
}
