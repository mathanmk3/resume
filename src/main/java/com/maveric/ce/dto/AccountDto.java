package com.maveric.ce.dto;

import com.maveric.ce.userenum.CurrencyType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;


import java.math.BigDecimal;

@Setter
@Getter
public class AccountDto
{

//    private Long accountNumber;

  //   private String accHolderName;

    @NotNull(message = "Please give currency for account")
    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    @NotNull
    @Positive(message = "Balance should be positive")
    private BigDecimal balance;

}
