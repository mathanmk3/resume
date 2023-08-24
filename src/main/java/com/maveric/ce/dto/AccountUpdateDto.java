package com.maveric.ce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AccountUpdateDto {

    @Positive(message = "Amount should be Positive")
    @NotNull(message = "Balance is mandatory")
    private BigDecimal balance;

    @NotNull(message = "account number is mandatory to update balance")
    private Long accountNumber;
}
