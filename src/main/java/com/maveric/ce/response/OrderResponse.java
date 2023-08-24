package com.maveric.ce.response;

import com.maveric.ce.dto.OrderDto;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class OrderResponse {

	String message;
	OrderDto orderData;
	

}
