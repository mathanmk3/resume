package com.maveric.ce.entity;

import com.maveric.ce.userenum.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
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

    @OneToMany(mappedBy = "orderFromAccountId",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<CurrencyExchangeOrders> listOfaccountFrom;


    @OneToMany(mappedBy = "orderToAccountId",cascade = CascadeType.REMOVE,fetch = FetchType.EAGER)
    private List<CurrencyExchangeOrders> listOfaccountTo;

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
