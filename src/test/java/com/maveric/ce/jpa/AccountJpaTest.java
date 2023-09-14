
package com.maveric.ce.jpa;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.maveric.ce.utils.OrderUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.serviceImpl.CurrencyExchangeOrderServiceImpl;
import com.maveric.ce.userenum.CurrencyType;
import com.maveric.ce.userenum.RolesEnum;

@DataJpaTest
@Disabled
class AccountJpaTest {

	@Mock
	CurrencyExchangeOrdersRepo ordeRepo;

	@Mock
	OrderUtils orderUtils;

	@Autowired
	IAccountRepository customerAccountRepo;

	@Autowired
	ICustomerRepository customerRespo;

	@InjectMocks

	@Spy
	CurrencyExchangeOrderServiceImpl service;
	AccountDetails saveDeatils;

	@BeforeEach
	void setCustomerAndAccountDetails() {
		CustomerDetails customerDetails = CustomerDetails.builder().username("mathanmkTEST").email("mathan33@gmail.com")
				.firstName("MathanKumarTEST").lastName("KumarTEST").phoneNumber("9488101204").password("mathan33")
				.rolesENum(RolesEnum.CUSTOMER).build();
		CustomerDetails customerDeatilsSaved = customerRespo.save(customerDetails);
		AccountDetails accDetails = AccountDetails.builder().accHolderName("Mathan").balance(BigDecimal.valueOf(1000))
				.accountNumber(Long.valueOf(1)).currencyType(CurrencyType.INR).customer(customerDeatilsSaved).build();

		saveDeatils = customerAccountRepo.save(accDetails);
	}

	@Test
	void getOrderPageDetails() {

		String findByMailId = "mathan33@gmail.com";
		Optional<List<AccountDetails>> expectedList = customerAccountRepo.getCustomerAccount(findByMailId);
		boolean expected = !expectedList.isEmpty();
		assertThat(expected).isTrue();
	}

	@Test
	void testCheckSufficientBalance() {

		BigDecimal checkBalance = BigDecimal.valueOf(500.00);
		Optional<Long> expectedBalance = customerAccountRepo
				.checkSufficientAmmount(saveDeatils.getCustomer().getCustomerId(), saveDeatils.getId(), checkBalance);
		boolean expected = expectedBalance.get() <= 1;
		assertThat(expected).isTrue();
	}

	@Test
	void TestgetAccountBalance() {
		Optional<BigDecimal> expectedBalance = customerAccountRepo
				.getAccountBalance(saveDeatils.getCustomer().getCustomerId(), saveDeatils.getId());

		boolean expected = expectedBalance.get() != null;
		assertThat(expected).isTrue();
	}

	@Test
	void TestUpdatingDebitDetails() {
		BigDecimal updateBalance = BigDecimal.valueOf(500.00);
		boolean expected = customerAccountRepo
				.updateDebitDetails(saveDeatils.getCustomer().getCustomerId(), saveDeatils.getId(), updateBalance, null)
				.isPresent();
		assertThat(expected).isTrue();
	}

	@Test
	void TestUpdateCreditDetails() {
		BigDecimal updateBalance = BigDecimal.valueOf(500.00);
		boolean expected = customerAccountRepo.updateCreditDetails(saveDeatils.getCustomer().getCustomerId(),
				saveDeatils.getId(), updateBalance, null).isPresent();
		assertThat(expected).isTrue();
	}

}
