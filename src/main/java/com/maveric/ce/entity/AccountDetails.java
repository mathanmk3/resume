package com.maveric.ce.entity;

import com.maveric.ce.userenum.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class AccountDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountNumber;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customers_id")
    private CustomerDetails customer;
    private String accHolderName;

    @Enumerated(EnumType.STRING)
    private CurrencyType currencyType;

    private BigDecimal balance;

    private LocalDateTime accCreatedAt;

    private LocalDateTime accLastUpdatedAt;

    @Override
    public String toString() {
        return "AccountDetails{" +
                "accountNumber=" + accountNumber +
                ", customersId=" + customer +
                ", accHolderName='" + accHolderName + '\'' +
                ", currencyType=" + currencyType +
                ", balance=" + balance +
                ", accCreatedAt=" + accCreatedAt +
                ", accLastUpdatedAt=" + accLastUpdatedAt +
                '}';
    }
}
