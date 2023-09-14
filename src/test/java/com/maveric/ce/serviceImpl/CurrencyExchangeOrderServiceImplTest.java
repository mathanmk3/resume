package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.*;

import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.SQLExceptions;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.utils.OrderUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.entity.CurrencyExchangeOrders;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = {CurrencyExchangeOrderServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CurrencyExchangeOrderServiceImplTest {

	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeOrderServiceImplTest.class);

	@Autowired
	CurrencyExchangeOrderServiceImpl service;
	@MockBean
	OrderUtils orderUtils;

	@MockBean
	CurrencyExchangeOrdersRepo ordeRepo;

	@MockBean
	IAccountRepository customerAccountRepo;


	@MockBean
	ModelMapper modelMapper;

	static MockedStatic<CommonUtils> utilities;

	@BeforeAll
	static void setup() {
		utilities = Mockito.mockStatic(CommonUtils.class);
	}

	@Test
	void testPlaceNewOrder() {

		//OrderDto orderdetails = mock(OrderDto.class);
		OrderDto orderdetails = new OrderDto();
		//orderdetails.setCurrencyRate(new BigDecimal(8));
		orderdetails.setCustomerId(1L);
		orderdetails.setOrderAmount(new BigDecimal(1000));
		orderdetails.setOrderFromAccountId(10200L);
		orderdetails.setOrderToAccountId(10201L);
		orderdetails.setOrderFromCurrencyType("INR");
		orderdetails.setOrderToCurrencyType("USD");
		orderdetails.setCurrencyRate(new BigDecimal(27));


		OrderDto orderdetailss = mock(OrderDto.class);
		CurrencyExchangeOrders orderSaved = mock(CurrencyExchangeOrders.class);
		when(orderUtils.checkSufficientBalance(orderdetails.getOrderAmount(), orderdetails.getCustomerId(),
				orderdetails.getOrderFromAccountId())).thenReturn(true);
		when(ordeRepo.save(Mockito.any(CurrencyExchangeOrders.class))).thenReturn(orderSaved);
		when(orderUtils.updateCustomerAccountBalance(orderdetails)).thenReturn(true);
		// MockedStatic<CommonUtils> utilities = Mockito.mockStatic(CommonUtils.class);
		utilities.when(() -> CommonUtils.checkNullable(orderdetails)).thenReturn(false);
		utilities.when(() -> CommonUtils.getMapper(orderdetails, CurrencyExchangeOrders.class)).thenReturn(orderSaved);
		utilities.when(() -> CommonUtils.getMapper(orderSaved, OrderDto.class)).thenReturn(orderdetails);
		OrderDto expected = service.newOrder(orderdetails);
		assertNotNull(expected);
		verify(ordeRepo).save(orderSaved);

	}

	@Test
	void testPlaceNewOrderWhenNoConnection() throws SQLExceptions, ServiceException {
		OrderDto orderdetails = new OrderDto();
		//orderdetails.setCurrencyRate(new BigDecimal(8));
		orderdetails.setCustomerId(1L);
		orderdetails.setOrderAmount(new BigDecimal(1000));
		orderdetails.setOrderFromAccountId(10200L);
		orderdetails.setOrderToAccountId(10201L);
		orderdetails.setOrderFromCurrencyType("INR");
		orderdetails.setOrderToCurrencyType("USD");
		orderdetails.setCurrencyRate(new BigDecimal(27));
		try {
			when(orderUtils.checkSufficientBalance(orderdetails.getOrderAmount(), orderdetails.getCustomerId(),
					orderdetails.getOrderFromAccountId())).thenThrow(new DataAccessException("Database error") {
			});
			service.newOrder(orderdetails);
		} catch (DataAccessException e) {
			assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
			verify(orderUtils).checkSufficientBalance(orderdetails.getOrderAmount(), orderdetails.getCustomerId(),
					orderdetails.getOrderFromAccountId());
		}
	}

	@Test
	void testPlaceNewOrderWithSameCurrencyType() throws SQLExceptions {
		OrderDto orderdetails = new OrderDto();
		//orderdetails.setCurrencyRate(new BigDecimal(8));
		orderdetails.setCustomerId(1L);
		orderdetails.setOrderAmount(new BigDecimal(1000));
		orderdetails.setOrderFromAccountId(10200L);
		orderdetails.setOrderToAccountId(10201L);
		orderdetails.setOrderFromCurrencyType("INR");
		orderdetails.setOrderToCurrencyType("INR");
		orderdetails.setCurrencyRate(new BigDecimal(27));
		CurrencyExchangeOrders orderSaved = mock(CurrencyExchangeOrders.class);
		when(orderUtils.checkSufficientBalance(orderdetails.getOrderAmount(), orderdetails.getCustomerId(),
				orderdetails.getOrderFromAccountId())).thenReturn(true);
		try {
			service.newOrder(orderdetails);
		}catch (ServiceException e){
			assertEquals(ErrorCodes.SAME_CURRENCY_FOUND, e.getMessage());
		}

	}

	@Test
	void testPlaceNewOrderWithNullPointer() throws SQLExceptions {
		OrderDto orderdetails = new OrderDto();
		//orderdetails.setCurrencyRate(new BigDecimal(8));
		orderdetails.setCustomerId(1L);
		orderdetails.setOrderAmount(new BigDecimal(1000));
		orderdetails.setOrderFromAccountId(10200L);
		orderdetails.setOrderToAccountId(10201L);
		orderdetails.setCurrencyRate(new BigDecimal(27));
		CurrencyExchangeOrders orderSaved = mock(CurrencyExchangeOrders.class);
		when(orderUtils.checkSufficientBalance(orderdetails.getOrderAmount(), orderdetails.getCustomerId(),
				orderdetails.getOrderFromAccountId())).thenReturn(true);
		try {
			service.newOrder(orderdetails);
		}catch (ServiceException e){
			String value="\"com.maveric.ce.dto.OrderDto.getOrderToCurrencyType()\" is null";
			assertTrue(e.getMessage().contains(value));
		}

	}

	@Test
	void testWatchList() {
		WatchListDto watchListDto = mock(WatchListDto.class);
		LinkedList<WatchListDto> watchList = new LinkedList<>();
		watchList.add(watchListDto);
		CurrencyExchangeOrders listPair = mock(CurrencyExchangeOrders.class);
		LinkedList<CurrencyExchangeOrders> orderList = new LinkedList<>();
		orderList.add(listPair);
		when(ordeRepo.getLatestCurrencyPair(Mockito.anyString())).thenReturn(Optional.of(orderList));
		utilities.when(() -> CommonUtils.getMapper(listPair, WatchListDto.class)).thenReturn(watchListDto);
		List<WatchListDto> expected = service.getOrderWatchList(Mockito.anyString());
		assertNotNull(expected.get(0));
		assertEquals(watchListDto, expected.get(0));
		verify(ordeRepo).getLatestCurrencyPair(Mockito.anyString());
	}


	@Test
	void testLatestCurrencyPairWhenNoConnection() throws SQLExceptions, ServiceException {
		String mailId ="mathan33@gamil.com";
		try {
			when(ordeRepo.getLatestCurrencyPair(Mockito.anyString())).thenThrow(new DataAccessException("Database error") {
			});
			service.getOrderWatchList(mailId);
		} catch (DataAccessException e) {
			assertEquals(ErrorCodes.CONNECTION_ISSUE, e.getMessage());
			verify(ordeRepo).getLatestCurrencyPair(Mockito.<String>any());
		}
	}

	@Test
	public void testGetOrderWatchListNoOrderFound() throws ServiceException {
		String customerMailId = "mathan@gamil.com";
		LinkedList<CurrencyExchangeOrders> orderList = new LinkedList<>();
		when(ordeRepo.getLatestCurrencyPair(customerMailId)).thenReturn(Optional.of(orderList));
		try {
			List<WatchListDto> orderWatchList = service.getOrderWatchList(customerMailId);
		} catch (ServiceException e) {
			assertEquals(ErrorCodes.NO_ORDER_FOUND, e.getMessage());
		}
	}


}
