package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.OrderPageDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CurrencyExchangeOrders;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.serviceImpl.CurrencyExchangeOrderServiceImpl;
import com.maveric.ce.userenum.CurrencyType;
import com.maveric.ce.userenum.RolesEnum;
import com.maveric.ce.utils.CommonUtils;
import com.maveric.ce.utils.OrderUtils;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

	@Mock
	OrderUtils orderUtils;

	@Mock
	CurrencyExchangeOrdersRepo ordeRepo;

	@Mock
	IAccountRepository customerAccountRepo;

	@InjectMocks
	@Spy
	CurrencyExchangeOrderServiceImpl service;
	@Mock
	ModelMapper modelMapper;

	static MockedStatic<CommonUtils> utilities;

	@BeforeAll
	static void setup() {
		utilities = Mockito.mockStatic(CommonUtils.class);
	}

	@Test
	void testPlaceNewOrder() {

		OrderDto orderdetails = mock(OrderDto.class);
		OrderDto orderdetailss = mock(OrderDto.class);
		CurrencyExchangeOrders orderSaved = mock(CurrencyExchangeOrders.class);
		when(orderUtils.checkSufficientBalance(orderdetailss.getOrderAmount(), orderdetailss.getCustomerId(),
				orderdetailss.getOrderFromAccountId())).thenReturn(true);
		when(ordeRepo.save(Mockito.any(CurrencyExchangeOrders.class))).thenReturn(orderSaved);
		when(orderUtils.updateCustomerAccountBalance(orderdetails)).thenReturn(true);
		// MockedStatic<CommonUtils> utilities = Mockito.mockStatic(CommonUtils.class);
		utilities.when(() -> CommonUtils.checkNullable(orderdetails)).thenReturn(false);
		utilities.when(() -> CommonUtils.getMapper(orderdetails, CurrencyExchangeOrders.class)).thenReturn(orderSaved);
		utilities.when(() -> CommonUtils.getMapper(orderSaved, OrderDto.class)).thenReturn(orderdetails);
		System.out.println("serv---> "+orderdetails);
		OrderDto expected = service.newOrder(orderdetails);
		assertNotNull(expected);
		verify(ordeRepo).save(orderSaved);

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
	void testGetOrderPageDetails() {
		AccountDetails accountDto = mock(AccountDetails.class);
		List<AccountDetails> AccountList = new LinkedList<>();
		AccountList.add(accountDto);
		OrderPageDto orderPageDto = mock(OrderPageDto.class);
		LinkedList<OrderPageDto> orderPage = new LinkedList<>();
		orderPage.add(orderPageDto);
		when(customerAccountRepo.getCustomerAccount(Mockito.anyString())).thenReturn(Optional.of(AccountList));
		// MockedStatic<CommonUtils> utilities = Mockito.mockStatic(CommonUtils.class);
		utilities.when(() -> CommonUtils.getMapper(accountDto, OrderPageDto.class)).thenReturn(orderPageDto);
		List<OrderPageDto> expected = service.getOrderPageDetails(Mockito.anyString());
		assertNotNull(expected.get(0));
		assertEquals(orderPageDto, expected.get(0));
		verify(customerAccountRepo).getCustomerAccount(Mockito.anyString());

	}

}
