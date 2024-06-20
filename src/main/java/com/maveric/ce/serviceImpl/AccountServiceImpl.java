package com.maveric.ce.serviceImpl;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.AccountNumGenerator;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.*;
import com.maveric.ce.repository.CurrencyExchangeOrdersRepo;
import com.maveric.ce.repository.IAccNumGenRepository;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.service.AccountService;
import com.maveric.ce.utils.CommonUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
	private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);
	@Autowired
	private ICustomerRepository iCustomerRepository;
	@Autowired
	private IAccountRepository iAccountRepository;

	@Autowired
	IAccNumGenRepository iAccNumGenRepository;

	@Autowired
	CurrencyExchangeOrdersRepo ordersRepo;

	@Autowired
	CommonUtils commonUtils;

	@Autowired
	ModelMapper modelMapper;

	/*
	 * Creating an account for customer....
	 */
	@Override
	public AccountResponseDto createAccount(Long customerId, AccountDto accountDto) throws ServiceException {
		Optional<CustomerDetails> referenceById = iCustomerRepository.findBycustomerId(customerId);
		logger.info("inside createAccount impl:", referenceById);
		if (!referenceById.isEmpty()) {
			logger.info("customer found ");
			AccountDetails accountDetailsByCur = iAccountRepository.findByCustomer_CustomerIdAndCurrencyType(customerId,
					accountDto.getCurrencyType());
			logger.info("finding acc details by currency:" + accountDetailsByCur);
			if (accountDetailsByCur == null) {
				AccountDetails accountDetails = commonUtils.accountDtoToAccountDetails(accountDto);
				accountDetails.setCustomer(referenceById.get()); // setting customer id to identify for customer.
				accountDetails
						.setAccHolderName(referenceById.get().getFirstName() + " " + referenceById.get().getLastName());
				accountDetails.setAccCreatedAt(LocalDateTime.now());
//using below Entity : AccountNumGenerator to generate accountNumber sequence as accountNumnber is non Primary key in AccountDetails.
				AccountNumGenerator accountNumGenerator = iAccNumGenRepository.save(new AccountNumGenerator());
				logger.info("acc generator sequence value:" + accountNumGenerator.getAccSequence());
				accountDetails.setAccountNumber(accountNumGenerator.getAccSequence());
				// everytime we are generating acc no sequence, we are inserting entry into
				// AccountNumGenerator
				// Check either to delete record('s) from AccountNumber to save space since
				// sequence value is generated from sequence names: account_sequence
// ends acc no sequence generation.
				AccountDetails savedAccountDetails = iAccountRepository.save(accountDetails);
				AccountResponseDto accountResponseDto = commonUtils
						.accountDetailsToAccountResponseDto(savedAccountDetails);
				return accountResponseDto;
			} else {
				throw new ServiceException(ErrorCodes.ACCOUNT_ALREADY_EXISTS);
			}
		} else {
			throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
		}

	}

	/*
	 * Fetch account details of a customer by AccountNumber as input..
	 */
	@Override
	public AccountResponseDto fetchByAccountNumber(Long customerId, Long accountNumber) throws ServiceException {

		Optional<CustomerDetails> customerDetails = iCustomerRepository.findBycustomerId(customerId);

		if(!customerDetails.isEmpty()) {


			Optional<AccountDetails> accountDetails = Optional
					.ofNullable(iAccountRepository.findByAccountNumber(accountNumber));
			if (!accountDetails.isEmpty()) {
				AccountResponseDto responseDto = commonUtils.accountDetailsToAccountResponseDto(accountDetails.get());
				return responseDto;
			} else {
				throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
			}
		} else{
			throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
		}
	}

	/*
	 * To fetch list of all accounts of a customer
	 */
	@Override
	public List<AccountResponseDto> fetchAccountByCustomer(Long customerId)
			throws ServiceException {
		Optional<CustomerDetails> fetchedCustomerDetails = iCustomerRepository.findBycustomerId(customerId);
		if (!fetchedCustomerDetails.isEmpty()) {
			List<AccountDetails> fetchedAccounts = iAccountRepository.findByCustomer_CustomerId(customerId);
			if (fetchedAccounts.size() > 0) {
				List<AccountResponseDto> accountDtoList = fetchedAccounts.stream()
						.map(account -> modelMapper.map(account, AccountResponseDto.class))
						.collect(Collectors.toList());

				return accountDtoList;
			} else {
				//throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
			}

		} else {
			throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
		}
		return null;
	}

	/* to Update Account details:Balance for a Customer */
	@Override
	public AccountResponseDto updateAccount(AccountUpdateDto accountDto,Long customerId, Long accountNumber)
			throws ServiceException {
		Optional<CustomerDetails> customerDetails = iCustomerRepository.findBycustomerId(customerId);
		if (!customerDetails.isEmpty()) {

			AccountDetails accountDetails = iAccountRepository.findByAccountNumber(accountDto.getAccountNumber());
			if (accountDetails != null) {

				if (accountDetails.getCustomer().getCustomerId() == customerId) {
					accountDetails.setBalance(accountDto.getBalance());
					accountDetails.setAccLastUpdatedAt(LocalDateTime.now());
					AccountDetails savedAccount = iAccountRepository.save(accountDetails);
					AccountResponseDto accountResponseDto = commonUtils
							.accountDetailsToAccountResponseDto(savedAccount);
					logger.info("responsedto:"+accountResponseDto);
					return accountResponseDto;
				} else {
					throw new ServiceException(ErrorCodes.UN_AUTHORIZED);
				}
			} else {
				throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
			}

		} else {
			throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
		}

	}

	/* To delete account of a customer */
	public Boolean deleteAccount(Long accountNumber, Long customerId) throws ServiceException {
		logger.info("in delete");
		try {
			Optional<CustomerDetails> customerDetails = iCustomerRepository.findBycustomerId(customerId);
			logger.info("customer details in deleteAccount:" + customerDetails);
			if (!customerDetails.isEmpty()) {
				logger.info("AccountNumber to delete:" + accountNumber);
				AccountDetails accountDetails = iAccountRepository.findByAccountNumber(accountNumber);
				logger.info("account details to delete:" + iAccountRepository.findByAccountNumber(accountNumber));
				if (accountDetails != null) {
					if (accountDetails.getCustomer().getCustomerId().compareTo(customerId)==0 ) {
						logger.info("deleting successfully");
						iAccountRepository.deleteByAccountNumber(accountNumber);
						logger.info("deleted  successfully");
						return Boolean.TRUE;
					} else {
						throw new ServiceException(ErrorCodes.UN_AUTHORIZED);
					}
				} else {
					throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
				}

			} else {
				throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
			}
		} catch (Exception e) {
			e.getStackTrace();
			return Boolean.FALSE;

		}
	}

	/*private boolean backUporderHistoryBeforeDeleting(Long customerId, Long accountId) {
		Long  result =ordersRepo.updateOrderStatus(customerId,accountId,accountId);
		if (result>0){
			result true
		}
		return  false;
	}*/

	/*
	 * below method gives list of all accounts available in database. Need to check
	 * whether its required or not.
	 */
	@Override
	public List<AccountResponseDto> fetchAllAccounts() throws ServiceException {
		List<AccountDetails> allAccounts = iAccountRepository.findAll();
		if (allAccounts.size() > 0) {
			List<AccountResponseDto> accountDtoList = allAccounts.stream()
					.map(account -> modelMapper.map(account, AccountResponseDto.class)).collect(Collectors.toList());

			return accountDtoList;
		} else {
			throw new ServiceException(ErrorCodes.ACCOUNT_NOT_FOUND);
		}
	}

}
