package com.maveric.ce.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class OrderDto {

	private Long id;
	private Long customerId;
	private Long orderToAccountId;

	@NotNull(message = "orderToCurrencyType is null")
	@NotEmpty(message = "orderToCurrencyType is Empty")
	private String orderToCurrencyType;
	private Long orderFromAccountId;
	@NotNull(message = "orderFromCurrencyType is null")
	@NotEmpty(message = "orderFromCurrencyType is Empty")
	private String orderFromCurrencyType;
	@NotNull(message = "Amount is null")
	private BigDecimal orderAmount;

	private BigDecimal currencyRate;
	private String currencyRateDateTime;
	private BigDecimal sellingValue;
	private BigDecimal buyingValue;
	private String status;
	private String orderPlacedDateTime;
	private String orderExchangeDateTime;
}
