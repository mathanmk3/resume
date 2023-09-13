package com.maveric.ce.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountUpdateDto {

    @Positive(message = "Amount should be Positive")
    @NotNull(message = "Balance is mandatory")
    private BigDecimal balance;


    @NotNull(message = "account number is mandatory to update balance")
    private Long accountNumber;
}
