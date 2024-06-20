package com.maveric.ce.repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import com.maveric.ce.entity.CurrencyExchangeOrders;

public interface CurrencyExchangeOrdersRepo extends JpaRepository<CurrencyExchangeOrders, Long> {

	@Query(value = "SELECT distinct new com.maveric.ce.entity.CurrencyExchangeOrders(ce.id,ce.customer,ce.orderToCurrencyType,ce.orderFromCurrencyType,ce.currencyRate,ce.orderAmount," +
			"	ce.orderFromAccountId, ce.orderToAccountId,ce.orderExchangeDateTime) from CurrencyExchangeOrders ce"
			+ " INNER JOIN CustomerDetails cd ON cd.customerId= ce.customer.customerId" +
			"   WHERE  cd.email=:customerMailId AND ce.status=1 ORDER BY ce.id DESC" )
	Optional<List<CurrencyExchangeOrders>> getWatchList(@Param("customerMailId") String customerMailId);

	@Transactional
	@Modifying
	@Query("UPDATE CurrencyExchangeOrders SET orderExchangeDateTime=:orderExchangeDateTime WHERE id=:id")
	int updateCurrencyExchangeDateTime(@Param("orderExchangeDateTime") String orderExchangeDateTime,
			@Param("id") Long id);


	@Transactional
	@Modifying
	@Query("UPDATE CurrencyExchangeOrders SET status=0,orderFromAccountId=null,orderToAccountId=null WHERE customer=:customerId AND (orderToAccountId=:orderToAccountId OR orderFromAccountId=:orderFromAccountId) ")
	Long updateOrderStatus(@Param("customerId") Long customerId, @Param("orderToAccountId") Long orderToAccountId,
									   @Param("orderFromAccountId") Long orderFromAccountId);




}
