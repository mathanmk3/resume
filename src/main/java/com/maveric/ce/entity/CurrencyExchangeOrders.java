package com.maveric.ce.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor; 
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "currencyexchangeorders")
public class CurrencyExchangeOrders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	

	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="customerid")
    private CustomerDetails customer;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ordertoaccountid")
    private AccountDetails orderToAccountId;
	
	
	@Column(name = "ordertocurrencytype")
	private String orderToCurrencyType;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="orderfromaccountid")
    private AccountDetails orderFromAccountId;
	
	@Column(name = "orderfromcurrencytype")
	private String orderFromCurrencyType;
	@Column(name = "orderamount")
	private double orderAmount;
	@Column(name = "currencyrate")
	private BigDecimal currencyRate;
	@Column(name = "currencyratedatetime")
	private String currencyRateDateTime;
	@Column(name = "sellingvalue")
	private double sellingValue;
	@Column(name = "buyingvalue")
	private double buyingValue;
	@Column(name = "status")
	private String status;
	@Column(name = "orderplaceddatetime")
	private String orderPlacedDateTime;
	@Column(name = "orderexchangedatetime")
	private String orderExchangeDateTime;

	public CurrencyExchangeOrders(Long id,CustomerDetails customer, String orderToCurrencyType,
			String orderFromCurrencyType, BigDecimal currencyRate, double orderAmount) {
		super();
		this.id=id;
		this.customer = customer;
		this.orderToCurrencyType = orderToCurrencyType;
		this.orderFromCurrencyType = orderFromCurrencyType;
		this.currencyRate = currencyRate;
		this.orderAmount = orderAmount;
 
	}

}
