package com.maveric.ce.service;

import com.maveric.ce.dto.CustomerDto;
import com.maveric.ce.dto.CustomerFetchResponseDto;
import com.maveric.ce.dto.CustomerResponseDto;
import com.maveric.ce.dto.CustomerUpdateDto;

import java.util.List;

public interface CustomerService
{
    CustomerResponseDto createCustomer(CustomerDto customerDto);
    String deleteCustomer(Long customerId);

    CustomerResponseDto updateCustomer(CustomerUpdateDto customerDto) ;

    CustomerFetchResponseDto fetchCustomerById(Long customerId);

    List<CustomerResponseDto> fetchAllCustomers();
}
