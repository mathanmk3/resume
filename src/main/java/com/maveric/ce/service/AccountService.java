package com.maveric.ce.service;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.exceptions.ServiceException;

import java.util.List;

public interface AccountService
{
    AccountResponseDto createAccount(Long customerId, AccountDto accountDto) throws ServiceException;


    AccountResponseDto fetchByAccountNumber(Long accountNumber) throws ServiceException;

    List<AccountResponseDto> fetchAccountByCustomer(Long customerId)throws ServiceException;
    List<AccountResponseDto> fetchAllAccounts() throws ServiceException;
    AccountResponseDto updateAccount(AccountUpdateDto accountDto, Long customerId) throws ServiceException;

    String deleteAccount(Long accountNumber, Long customerId) throws ServiceException;


}
