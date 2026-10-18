package dev.luan.digitalbank.DTO;

import dev.luan.digitalbank.domain.AccountStatus;
import jakarta.validation.constraints.*;
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

    @NotNull(message = "Account number is required")
    @Positive(message = "Account number must be greater than zero")
    private Long accountNumber;

    @NotBlank(message = "Holder name is required")
    @Size(min = 3, max = 100, message = "Holder name must be between 3 and 100 characters")
    private String holderName;

    @Null(message = "Balance cannot be set manually")
    private BigDecimal balance;

    private AccountStatus status;

    @NotBlank(message = "Document is required")
    private String document;
}
