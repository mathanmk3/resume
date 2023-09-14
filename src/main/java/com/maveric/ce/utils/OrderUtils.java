package com.maveric.ce.utils;

import java.math.BigDecimal;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.SQLExceptions;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;

@Component
public class OrderUtils {
	@Value("${currencyApi}")
	String currencyApi;
	@Value("${currencyJsonKey}")
	String rateKey;
	@Autowired
	CurrencyExchangeOrdersRepo orderRepo;
	@Autowired
	IAccountRepository customerAccountRepo;
	@Autowired
	RestTemplate restTemplate;

	public OrderDto currencyRateFromApi(OrderDto orderDto) throws ServiceException,NullPointerException {
		try {
			if (currencyApi != null && !currencyApi.isEmpty()) {
				String apiJson = restTemplate
						.getForEntity(currencyApi + orderDto.getOrderFromCurrencyType(), String.class).getBody();
				if (Boolean.FALSE.equals(CommonUtils.checkNullableAndEmpty(apiJson)
						.orElseThrow(() -> new ServiceException(ErrorCodes.INVALID_CURRENCY_API)))) {
					JSONObject rateJson = new JSONObject(apiJson);
					if ((Boolean.TRUE.equals(Optional.of(rateJson.has(rateKey))
							.orElseThrow(() -> new NullPointerException(ErrorCodes.INVALID_CURRENCY_API))))
							&& (Boolean.TRUE.equals(
							Optional.of(rateJson.getJSONObject(rateKey).has(orderDto.getOrderToCurrencyType()))
									.orElseThrow(() -> new ServiceException(ErrorCodes.API_EMPTY_RESPONSE))))) {

						BigDecimal currencyRate = rateJson.getJSONObject(rateKey)
								.getBigDecimal(orderDto.getOrderToCurrencyType());
						orderDto.setCurrencyRate(currencyRate);
						String lastDateTime = rateJson.getString("time_last_update_utc") != null
								? DateUtils.dateFormat(rateJson.getString("time_last_update_utc"))
								: DateUtils.currentDateTimeFormat();
						orderDto.setCurrencyRateDateTime(lastDateTime);
					}
				}
			} else {
				throw new ServiceException(ErrorCodes.INVALID_CURRENCY_API);
			}
		} catch (Exception he) {
			he.printStackTrace();
			throw new ServiceException(he.getMessage());
		}
		return orderDto;
	}

	public boolean checkSufficientBalance(BigDecimal sellingAmount, Long customerId, Long accountId)
			throws ServiceException {
		try {
			Long checkSufficientBalance = customerAccountRepo
					.checkSufficientAmmount(customerId, accountId, sellingAmount)
					.orElseThrow(() -> new ServiceException(ErrorCodes.INSUFFICIENT_BALANCE, HttpStatus.BAD_REQUEST));
			return checkSufficientBalance > 0;
		} catch (DataAccessException he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}

	}

	public boolean updateCustomerAccountBalance(OrderDto orderDto) throws ServiceException {
		try {
			BigDecimal orderFromExistingBalance = customerAccountRepo
					.getAccountBalance(orderDto.getCustomerId(), orderDto.getOrderFromAccountId())
					.orElseThrow(() -> new ServiceException(ErrorCodes.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST));


			boolean debitid = (customerAccountRepo
					.updateDebitDetails(orderDto.getCustomerId(), orderDto.getOrderFromAccountId(),
							orderFromExistingBalance.subtract(orderDto.getSellingValue()),
							DateUtils.currentDateTimeFormat())
					.orElseThrow(() -> new ServiceException(ErrorCodes.ERROR_IN_UPDATING_BALANCE,
							HttpStatus.INTERNAL_SERVER_ERROR))) > 0;

			BigDecimal orderToExistingBalance = customerAccountRepo
					.getAccountBalance(orderDto.getCustomerId(), orderDto.getOrderToAccountId())
					.orElseThrow(() -> new ServiceException(ErrorCodes.BALANCE_NOT_FOUND, HttpStatus.BAD_REQUEST));


			boolean creditid = (customerAccountRepo
					.updateCreditDetails(orderDto.getCustomerId(), orderDto.getOrderToAccountId(),
							orderToExistingBalance.add(orderDto.getBuyingValue()), DateUtils.currentDateTimeFormat())
					.orElseThrow(() -> new ServiceException(ErrorCodes.ERROR_IN_UPDATING_BALANCE,
							HttpStatus.INTERNAL_SERVER_ERROR))) > 0;

			return debitid && creditid;
		} catch (DataAccessException he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}
	}

	public void updateOrderExchangeTime(Long id) {
		try {
			orderRepo.updateCurrencyExchangeDateTime(DateUtils.currentDateTimeFormat(), id);
		} catch (DataAccessException he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}
	}

}
