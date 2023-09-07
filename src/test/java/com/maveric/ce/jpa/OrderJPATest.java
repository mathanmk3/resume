package com.maveric.ce.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CurrencyExchangeOrders;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.serviceImpl.CurrencyExchangeOrderServiceImpl;
import com.maveric.ce.userenum.CurrencyType;
import com.maveric.ce.userenum.RolesEnum;
import com.maveric.ce.utils.DateUtils;
@DataJpaTest
 class OrderJPATest {

	@Autowired
	IAccountRepository customerAccountRepo;
	@Autowired
	ICustomerRepository customerRespo;
	@Autowired
	CurrencyExchangeOrdersRepo ordeRepo;

	@InjectMocks
	@Spy
	CurrencyExchangeOrderServiceImpl service;
	AccountDetails saveDeatils;
	
	CurrencyExchangeOrders orderdetails;

	@BeforeEach
	void setOrderValue() {
 
		CustomerDetails customerDetails = CustomerDetails.builder().username("mathanmkTEST").email("mathan33@gmail.com")
				.firstName("MathanKumarTEST").lastName("KumarTEST").phoneNumber("9488101204").password("mathan33")
				.rolesENum(RolesEnum.CUSTOMER).build();

		CustomerDetails customerDeatilsSaved = customerRespo.save(customerDetails);

		AccountDetails accDetails = AccountDetails.builder().accHolderName("Mathan").balance(BigDecimal.valueOf(1000))
				.accountNumber(Long.valueOf(1111)).currencyType(CurrencyType.INR).customer(customerDeatilsSaved)
				.build();

		AccountDetails accDetail2 = AccountDetails.builder().accHolderName("Mathan").balance(BigDecimal.valueOf(1000))
				.accountNumber(Long.valueOf(2222)).currencyType(CurrencyType.AED).customer(customerDeatilsSaved)
				.build();

		AccountDetails saveDeatils = customerAccountRepo.save(accDetails);
		AccountDetails saveDeatils2 = customerAccountRepo.save(accDetail2);

		orderdetails = CurrencyExchangeOrders.builder().buyingValue(Double.valueOf(100.0))
				.currencyRate(BigDecimal.valueOf(7.8)).orderAmount(Double.valueOf(100))
				.sellingValue(Double.valueOf(100)).orderFromCurrencyType("INR").orderToCurrencyType("AED")
				.orderFromAccountId(saveDeatils).orderToAccountId(saveDeatils2).customer(customerDeatilsSaved).build();
	
		ordeRepo.save(orderdetails);
	}

	@Test
	void testGetCurrencyPair() {
		String mailId = "mathan33@gmail.com";
		Optional<List<CurrencyExchangeOrders>> expectedValue = ordeRepo.getLatestCurrencyPair(mailId);
		boolean expected = expectedValue.get().size() > 0;
		assertThat(expected).isTrue();
	}
	@Test
	void testUpdateExportedDateTime() {
		int i=ordeRepo.updateCurrencyExchangeDateTime(DateUtils.currentDateTimeFormat(), orderdetails.getId());
		boolean expected = i > 0;
		assertThat(expected).isTrue();

 
	}
}
