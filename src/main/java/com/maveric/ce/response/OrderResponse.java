package com.maveric.ce.response;

import org.springframework.stereotype.Component;

import com.maveric.ce.dto.OrderDto;

import lombok.Data;

@Component
@Data
public class OrderResponse {

	String message;
	OrderDto orderData;
	

}
