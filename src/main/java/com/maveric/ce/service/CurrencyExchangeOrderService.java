package com.maveric.ce.service;

import java.sql.SQLException;
import java.util.List;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.OrderPageDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.exceptions.ServiceException;
import org.springframework.web.client.HttpServerErrorException;


public interface CurrencyExchangeOrderService {
	
	
	OrderDto newOrder(OrderDto orderDto) throws ServiceException, Exception;
	
	List<WatchListDto> getOrderWatchList(String customerId) throws ServiceException, HttpServerErrorException, SQLException;
	
	List <OrderPageDto> getOrderPageDetails(String mailId) throws ServiceException,ServiceException;

	List <OrderPageDto> getOrderPageDetails(Long customerId) throws ServiceException,ServiceException;



}
 
