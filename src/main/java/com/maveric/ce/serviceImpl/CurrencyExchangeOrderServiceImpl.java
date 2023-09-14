package com.maveric.ce.serviceImpl;

/**
 * @author mathanm
 */
import java.util.LinkedList;
import java.util.List;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.SQLExceptions;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.utils.CommonUtils;
import com.maveric.ce.utils.DateUtils;
import com.maveric.ce.utils.OrderUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.maveric.ce.entity.CurrencyExchangeOrders;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.service.CurrencyExchangeOrderService;

@Service
public class CurrencyExchangeOrderServiceImpl implements CurrencyExchangeOrderService {
	private static final Logger logger = LoggerFactory.getLogger(CurrencyExchangeOrderServiceImpl.class);

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
	 * @param orderDto is passed to place the exchange orders
	 * @return Order Details
	 * @apiNote Call the currency Date API to get the updated currency date
	 * @apiNote Update the Customer account balance once order is placed.
	 * @throws ServiceException when Account not found
	 * @throws NullPointerException when Empty response from Currency API.
	 *
	 */
	@Override
	public OrderDto newOrder(OrderDto orderDto) throws ServiceException {
		logger.info("called newOrder() in Impl");
		logger.info("Orderdto object:"+orderDto);
		OrderDto ordermap = new OrderDto();
		try {
			if (orderUtils.checkSufficientBalance(orderDto.getOrderAmount(), orderDto.getCustomerId(),
					orderDto.getOrderFromAccountId())) {
				logger.info("inside if newOrder()");
				orderDto.setOrderPlacedDateTime(DateUtils.currentDateTimeFormat());
				// check if both currency same
				if(orderDto.getOrderToCurrencyType().equalsIgnoreCase(orderDto.getOrderFromCurrencyType())){
					throw new ServiceException(ErrorCodes.SAME_CURRENCY_FOUND);
				}
				orderUtils.currencyRateFromApi(orderDto);
				orderDto.setSellingValue(orderDto.getOrderAmount());
				logger.info("before multiply in newOrder()");
				orderDto.setBuyingValue(orderDto.getOrderAmount().multiply(orderDto.getCurrencyRate()));
				logger.info("after multiply in newOrder()");
				orderDto.setStatus("1");
				CurrencyExchangeOrders map = CommonUtils.getMapper(orderDto, CurrencyExchangeOrders.class);

				CurrencyExchangeOrders order = orderRepo.save(map);
				ordermap = CommonUtils.getMapper(order, OrderDto.class);

				if (!CommonUtils.checkNullable(ordermap) && (orderUtils.updateCustomerAccountBalance(orderDto))) {
					orderUtils.updateOrderExchangeTime(order.getId());
				}
			}
			return ordermap;
		} catch (DataAccessException he) {
			he.printStackTrace();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		} catch (NullPointerException e) {
			throw new ServiceException(e.getLocalizedMessage());
		}
	}

	/**
	 * @param customerMailId is passed to retrieve the lasted order.
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
		} catch (DataAccessException he) {
			he.printStackTrace();
			throw new SQLExceptions(ErrorCodes.CONNECTION_ISSUE);
		}

	}

}
