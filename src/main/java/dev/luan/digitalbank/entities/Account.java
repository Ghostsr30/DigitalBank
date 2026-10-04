package dev.luan.digitalbank.entities;

import dev.luan.digitalbank.domain.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String document;

    private String holderName;

    private Long accountNumber;

    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;
}
