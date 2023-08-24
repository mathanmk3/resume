package com.maveric.ce.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class WatchListDto {

	private Long id;
	private Long customerId;
	private String orderToCurrencyType;
	private String orderFromCurrencyType;
	private double currencyRate;
	private double orderAmount;
	
	
}
