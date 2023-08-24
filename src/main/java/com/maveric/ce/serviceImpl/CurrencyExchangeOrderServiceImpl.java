package com.maveric.ce.serviceImpl;

/**
 * @author mathanm
 */
import java.util.LinkedList;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.OrderPageDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CurrencyExchangeOrders;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.SQLExceptions;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.service.CurrencyExchangeOrderService;
import com.maveric.ce.utils.CommonUtils;
import com.maveric.ce.utils.DateUtils;
import com.maveric.ce.utils.OrderUtils;

@Service
public class CurrencyExchangeOrderServiceImpl implements CurrencyExchangeOrderService {

	@Value("${currencyApi}")
	String currencyApi;

	@Autowired
	CurrencyExchangeOrdersRepo orderRepo;
	@Autowired
	IAccountRepository customerAccountRepo;
	@Autowired
	ModelMapper modelMapper;
	@Autowired
	OrderUtils orderUtils;
	
	/**
	 * @param customerMailId is passed to retrieve the Customer account details
	 * @return list of customer accounts
	 * @throws serviceException when Account not found
	 * @throws SQLException when Connection issue.
	 * 
	 */
	@Override
	public List<OrderPageDto> getOrderPageDetails(String customerMailId) throws ServiceException {
		List<OrderPageDto> listOfAccount = new LinkedList<>();
		try {
			List<AccountDetails> listOfCustomerAccounts = customerAccountRepo.getCustomerAccount(customerMailId)
					.orElseThrow(() -> new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND));
			if (listOfCustomerAccounts.isEmpty()) {
				throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
			}
			for (AccountDetails accounts : listOfCustomerAccounts) {
				OrderPageDto dto = CommonUtils.getMapper(accounts, OrderPageDto.class);
				listOfAccount.add(dto);
			}
			return listOfAccount;
		} catch (SQLExceptions he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}

	}

	/**
	 * @param OrderDto is passed to place the exchange orders
	 * @return Order Details
	 * @apiNote Call the currency Date API to get the updated currency date
	 * @apiNote Update the Customer account balance once order is placed.
	 * @throws serviceException when Account not found
	 * @throws NullPointerException when Empty response from Currency API.
	 * 
	 */
	@Override
	public OrderDto newOrder(OrderDto orderDto) throws ServiceException {
		OrderDto ordermap = new OrderDto();
		try {
			if (orderUtils.checkSufficientBalance(orderDto.getOrderAmount(), orderDto.getCustomerId(),
					orderDto.getOrderFromAccountId())) {
				orderDto.setOrderPlacedDateTime(DateUtils.currentDateTimeFormat());
				orderUtils.currencyRateFromApi(orderDto);
				orderDto.setSellingValue(orderDto.getOrderAmount());
				orderDto.setBuyingValue(orderDto.getOrderAmount().multiply(orderDto.getCurrencyRate()));
				orderDto.setStatus("1");
				CurrencyExchangeOrders map = CommonUtils.getMapper(orderDto, CurrencyExchangeOrders.class);

				CurrencyExchangeOrders order = orderRepo.save(map);
				ordermap = CommonUtils.getMapper(order, OrderDto.class);

				if (!CommonUtils.checkNullable(ordermap) && (orderUtils.updateCustomerAccountBalance(orderDto))) {
					orderUtils.updateOrderExchangeTime(order.getId());
					return ordermap;
				}
			}
		} catch (SQLExceptions he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		} catch (NullPointerException e) {
			throw new ServiceException(e.getLocalizedMessage());

		}
		return ordermap;
	}
	/**
	 * @param customer is passed to retrieve the lasted order.
	 * @return List of customer orders
	 * @throws ServiceException when No orders found
	 * @throws NullPointerException when Empty response from Currency API.
	 * 
	 */
	@Override
	public List<WatchListDto> getOrderWatchList(String customerMailId) throws ServiceException {
		try {
			List<WatchListDto> orderWatchList = new LinkedList<>();
			List<CurrencyExchangeOrders> listOfOrder = orderRepo.getLatestCurrencyPair(customerMailId)
					.orElseThrow(() -> new ServiceException("NO_ORDER_FOUND"));
			if (!listOfOrder.isEmpty()) {
				for (CurrencyExchangeOrders orders : listOfOrder) {
					WatchListDto dto = CommonUtils.getMapper(orders, WatchListDto.class);
					orderWatchList.add(dto);
				}
			} else {
				throw new ServiceException(ErrorCodes.NO_ORDER_FOUND);
			}
			return orderWatchList;
		} catch (SQLExceptions he) {
			he.getRootCause();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}

	}

}
