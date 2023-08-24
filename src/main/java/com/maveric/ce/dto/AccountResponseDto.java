package com.maveric.ce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maveric.ce.userenum.CurrencyType;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AccountResponseDto {

    private Long accountNumber;

    //    private Long customerId;
    private String accHolderName;
    private CurrencyType currencyType;

    private BigDecimal balance;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accCreatedAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime accLastUpdatedAt;


}
