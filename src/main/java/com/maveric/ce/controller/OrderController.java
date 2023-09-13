package com.maveric.ce.controller;

import java.sql.SQLException;
import java.util.List;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.OrderPageDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.exceptions.SQLExceptions;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.utils.JWTUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maveric.ce.response.OrderResponse;
import com.maveric.ce.serviceImpl.CurrencyExchangeOrderServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
@Validated
public class OrderController {

	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	@Autowired
	CurrencyExchangeOrderServiceImpl currencyExchangeOrderService;
	/*@Autowired
	OrderResponse response;*/

	/*@GetMapping
	public ResponseEntity<List<OrderPageDto>> getOrderPageDetails(HttpServletRequest request)
			throws ServiceException, SQLException {
		String token = request.getHeader("Authorization");

		// String getUserMailId = JWTUtils.extractUserMailId(token);
		String getUserMailId = "mathan@gamil.com";
		List<OrderPageDto> listOfCutsomerAccounts = orderService.getOrderPageDetails(getUserMailId);
		return new ResponseEntity<>(listOfCutsomerAccounts, HttpStatus.OK);
	}*/

	@PostMapping("/placeorder")
	public ResponseEntity<OrderResponse> placeOrder(@Valid @RequestBody OrderDto orderDto, HttpServletRequest request)
			throws ServiceException, SQLExceptions {
		OrderDto orderDetails = currencyExchangeOrderService.newOrder(orderDto);
		OrderResponse response = new OrderResponse();
		if (orderDetails != null) {
			logger.info("response object:"+response);
			logger.info("Called inside if place order");
			response.setMessage("Currency Exchanges");
			response.setOrderData(orderDetails);

			logger.info("response:"+response.getMessage());
			logger.info(""+response.getOrderData());
		}
		logger.info("before returning placeorder");
		logger.info("response:"+response.getMessage());
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@GetMapping("/watchlist")
	public ResponseEntity<List<WatchListDto>> getWatchList(HttpServletRequest request)
			throws ServiceException, SQLExceptions {
		String token = request.getHeader("Authorization");
		String getUserMailId = JWTUtils.extractUserMailId(token);
		logger.info("emaild:"+getUserMailId);
		List<WatchListDto> watchListDetails = currencyExchangeOrderService.getOrderWatchList(getUserMailId);
		return new ResponseEntity<>(watchListDetails, HttpStatus.OK);

	}
}
