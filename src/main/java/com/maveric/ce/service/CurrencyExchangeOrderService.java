package com.maveric.ce.service;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.client.HttpServerErrorException;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.OrderPageDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.exceptions.ServiceException;


public interface CurrencyExchangeOrderService {
	
	
	OrderDto newOrder(OrderDto orderDto) throws ServiceException, Exception;
	
	List<WatchListDto> getOrderWatchList(String customerId) throws ServiceException, HttpServerErrorException, SQLException;
	
	List <OrderPageDto> getOrderPageDetails(String customerId) throws ServiceException,ServiceException;


}
 