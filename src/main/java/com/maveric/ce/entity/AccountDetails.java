package com.maveric.ce.entity;

import com.maveric.ce.userenum.CurrencyType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
public class AccountDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   /* @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "account_sequence", initialValue = 1011200, allocationSize = 1)*/


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

    @Column(name = "amountcreditedddatetime")
    private String amountCrediteddDateTime;

    @Column(name = "accountdebiteddatetime")
    private String accountDebitedDateTime;

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

    public AccountDetails(Long id,Long accountNumber, CurrencyType currencyType) {
        super();
        this.id = id;
        this.accountNumber=accountNumber;
        this.currencyType = currencyType;
    }

    public AccountDetails() {
    }
}
