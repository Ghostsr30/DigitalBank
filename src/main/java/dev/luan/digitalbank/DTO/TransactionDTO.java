package dev.luan.digitalbank.DTO;

import dev.luan.digitalbank.domain.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionDTO {

    private Long id;
    private TransactionType type;
    private BigDecimal amount;
    private LocalDateTime createdAt;
    private Long sourceAccountId;
    private Long targetAccountId;
}
