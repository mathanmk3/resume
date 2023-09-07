package com.maveric.ce.utils;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderUtilsUnitTest {

	@Mock
	CurrencyExchangeOrdersRepo ordeRepo;
	@Mock
	IAccountRepository customerAccountRepo;
	@Autowired
	private RestTemplate restTemplate;
	@InjectMocks
	@Spy
	OrderUtils utils;

	static MockedStatic<CommonUtils> utilities;
	@BeforeAll
	static void setup() {
		utilities = Mockito.mockStatic(CommonUtils.class);
	}

/*	@Test
	void testCurrencyRateFromApi() throws JSONException {
		String currencyApi="https://open.er-api.com/v6/latest/";
		OrderDto orderdetails = mock(OrderDto.class);
		utilities.when(() -> CommonUtils.checkNullableAndEmpty(Mockito.anyString())).thenReturn(Optional.of(Boolean.FALSE));
		when(orderdetails.getOrderToCurrencyType()).thenReturn("INR");
		when(orderdetails.getOrderFromCurrencyType()).thenReturn("AED");
		utilities.when(() -> CommonUtils.checkNullableAndEmpty(Mockito.anyString())).thenReturn(Optional.of(Boolean.FALSE));
		utils.currencyRateFromApi(orderdetails);

	}*/

	@Test
	void testCheckSufficientBalance() {
		when(customerAccountRepo.checkSufficientAmmount(Mockito.anyLong(), Mockito.anyLong(), Mockito.any()))
				.thenReturn(Optional.of(Long.valueOf("11")));
		boolean expected = utils.checkSufficientBalance(Mockito.any(BigDecimal.class), Mockito.anyLong(), Mockito.anyLong());
		assertThat(expected).isTrue();

	}

	@Test
	void updateCustomerAccountBalance() {
		OrderDto orderdetails = mock(OrderDto.class);
		when(customerAccountRepo.getAccountBalance(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(1000)));
		when(customerAccountRepo.getAccountBalance(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Optional.of(BigDecimal.valueOf(1100)));
		when(customerAccountRepo.updateCreditDetails(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(BigDecimal.class),Mockito.anyString())).thenReturn(Optional.of(Integer.valueOf(11)));
		when(customerAccountRepo.updateDebitDetails(Mockito.anyLong(),Mockito.anyLong(),Mockito.any(BigDecimal.class),Mockito.anyString())).thenReturn(Optional.of(Integer.valueOf(11)));
		when(orderdetails.getBuyingValue()).thenReturn(BigDecimal.valueOf(110));
		when(orderdetails.getSellingValue()).thenReturn(BigDecimal.valueOf(110));
		boolean expected = utils.updateCustomerAccountBalance(orderdetails);
		assertThat(expected).isTrue();

	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
