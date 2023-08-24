package com.maveric.ce.utils;

import com.maveric.ce.dto.*;
import com.maveric.ce.entity.AccountDetails;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class CommonUtils {

	@Autowired
	ModelMapper modelMapper;

	public CustomerResponseDto customerToCustomerResponseDto(CustomerDetails customerDetails) {

		CustomerResponseDto customerResponseDto = new CustomerResponseDto();

		BeanUtils.copyProperties(customerDetails, customerResponseDto);

		/*
		 * if(customerDetails.getAccountDetails()!=null){ List<AccountResponseDto>
		 * accountResponseDtoList = customerDetails.getAccountDetails().stream().map(
		 * customer -> modelMapper.map(customer,AccountResponseDto.class)
		 * ).collect(Collectors.toList());
		 * customerResponseDto.setAccountResponseDtoList(accountResponseDtoList); }
		 */

		return customerResponseDto;

	}

	public CustomerFetchResponseDto customerToCustomerFetchResponseDto(CustomerDetails customerDetails) {

		CustomerFetchResponseDto customerFetchResponseDto = new CustomerFetchResponseDto();

		BeanUtils.copyProperties(customerDetails, customerFetchResponseDto);

		if (customerDetails.getAccountDetails() != null) {
			List<AccountResponseDto> accountResponseDtoList = customerDetails.getAccountDetails().stream()
					.map(customer -> modelMapper.map(customer, AccountResponseDto.class)).collect(Collectors.toList());
			customerFetchResponseDto.setAccountResponseDtoList(accountResponseDtoList);
		}

		return customerFetchResponseDto;

	}

	public CustomerDetails customerDtoToCustomerDetails(CustomerDto customerDto) {

		CustomerDetails customerDetails = new CustomerDetails();

		BeanUtils.copyProperties(customerDto, customerDetails);
		return customerDetails;

	}

	public AccountDetails accountDtoToAccountDetails(AccountDto accountDto) {

		AccountDetails accountDetails = new AccountDetails();
		BeanUtils.copyProperties(accountDto, accountDetails);
		return accountDetails;
	}

	public AccountResponseDto accountDetailsToAccountResponseDto(AccountDetails accountDetails) {

		AccountResponseDto accountResponseDto = new AccountResponseDto();
		BeanUtils.copyProperties(accountDetails, accountResponseDto);
		return accountResponseDto;
	}

	public static boolean checkNullable(OrderDto orderMap) {

		return Optional.ofNullable(orderMap).isEmpty();
	}

	public static boolean checkGreaterThan(int i) {

		return i > 0;

	}

	public static Optional<Boolean> checkNullableAndEmpty(String str) {
		return Optional.of(Optional.ofNullable(str).isEmpty() ? true : str.isEmpty());
	}

	public static <T, R> R getMapper(T mapFrom, Class<R> mapTo) {
		ModelMapper modelMapper = new ModelMapper();
		try {
			R ordermap = modelMapper.map(mapFrom, mapTo);
			return ordermap;
		} catch (Exception e) {
			return null;
		}

	}

}
