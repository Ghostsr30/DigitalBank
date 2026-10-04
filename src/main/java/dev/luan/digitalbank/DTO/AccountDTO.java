package dev.luan.digitalbank.DTO;

import dev.luan.digitalbank.domain.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AccountDTO {

    private Long id;
    private Long accountNumber;
    private String holderName;
    private BigDecimal balance;
    private AccountStatus status;
    private String document;
}
