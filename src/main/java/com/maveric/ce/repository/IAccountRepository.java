package com.maveric.ce.repository;

import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.userenum.CurrencyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IAccountRepository extends JpaRepository<AccountDetails, Long> {

	
    List<AccountDetails> findByCustomer_CustomerId(Long customerId);

    AccountDetails findByAccountNumber(Long accountNumber);

    @Query(value = " select * from account_details a where a.account_number =?1 and a.customers_id=?2",nativeQuery = true)
    AccountDetails findByAccountNumberAndCustomersId(Long accountNumber, Long customerId);


    @Transactional
    void deleteByAccountNumber(Long accountNumber);

    AccountDetails findByCustomer_CustomerIdAndCurrencyType(Long customerId, CurrencyType currencyType);
	
	// Mathan Custom Queries for Orders
    
	@Query("SELECT id from AccountDetails where customer.id=:customerid AND id=:id AND balance>=:balance")
	Optional<Long> checkSufficientAmmount(@Param("customerid") Long customerid, @Param("id") Long accountId,
			@Param("balance") BigDecimal balance);

	@Transactional
	@Modifying
	@Query("UPDATE AccountDetails SET balance=:balance,amountCrediteddDateTime=:amountCrediteddDateTime where customer.id=:customerid AND id=:id")
	Optional<Integer> updateCreditDetails(@Param("customerid") Long customerid, @Param("id") Long accountId,
			@Param("balance") BigDecimal balance, @Param("amountCrediteddDateTime") String amountCrediteddDateTime);

	@Transactional
	@Modifying
	@Query("UPDATE AccountDetails SET balance=:balance,accountDebitedDateTime=:accountDebitedDateTime where customer.id=:customerid AND id=:id")
	Optional<Integer> updateDebitDetails(@Param("customerid") Long customerid, @Param("id") Long accountId,
			@Param("balance") BigDecimal balance, @Param("accountDebitedDateTime") String accountDebitedDateTime);

	@Query("SELECT balance from AccountDetails where customer.id=:customerid AND id=:id")
	Optional<BigDecimal> getAccountBalance(@Param("customerid") Long customerid, @Param("id") Long accountId);

	@Query("SELECT new com.maveric.ce.entity.AccountDetails(ad.id,ad.accountNumber,ad.currencyType) from AccountDetails ad"
			+ " INNER JOIN CustomerDetails cd ON cd.customerId=ad.customer.id where cd.email=:customerMailid")
	Optional<List<AccountDetails>> getCustomerAccount(@Param("customerMailid") String customerMailid);
}


