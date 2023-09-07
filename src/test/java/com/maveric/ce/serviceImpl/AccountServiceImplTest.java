package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.*;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.AccountNumGenerator;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.IAccNumGenRepository;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.userenum.CurrencyType;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import com.maveric.ce.utils.CommonUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = {AccountServiceImpl.class})
@ExtendWith(SpringExtension.class)
public class AccountServiceImplTest {
    @Autowired
    private AccountServiceImpl accountServiceImpl;
    @MockBean
    private CommonUtils commonUtils;
    @MockBean
    private IAccNumGenRepository iAccNumGenRepository;
    @MockBean
    private IAccountRepository iAccountRepository;
    @MockBean
    private ICustomerRepository iCustomerRepository;
    @MockBean
    private ModelMapper modelMapper;
    CustomerDetails customerDetails = new CustomerDetails();
    AccountDetails accountDetails = new AccountDetails();
    AccountResponseDto accountResponseDto = new AccountResponseDto();
    @BeforeEach
    public void customerObjectInit(){
        customerDetails.setAccountDetails(new ArrayList<>());
        customerDetails.setCreatedAt(LocalDate.of(2023, 5, 9).atStartOfDay());
        customerDetails.setCustomerId(1L);
        customerDetails.setEmail("veera@maveric-systems.com");
        customerDetails.setFirstName("veera");
        customerDetails.setGender(Gender.Male);
        customerDetails.setLastName("kalyan");
        customerDetails.setLastUpdatedAt(LocalDate.of(2023, 6, 1).atStartOfDay());
        customerDetails.setPassword("veera");
        customerDetails.setPhoneNumber("9652783005");
        customerDetails.setRolesENum(RolesEnum.CUSTOMER);
        customerDetails.setUsername("veerakalyan");

        accountDetails.setCustomer(customerDetails);
        accountDetails.setAccountNumber(201011L);

        accountResponseDto.setAccCreatedAt(LocalDate.of(2023, 7, 1).atStartOfDay());
        accountResponseDto.setAccHolderName("veera kalyan");
        accountResponseDto.setAccLastUpdatedAt(LocalDate.of(1970, 7, 9).atStartOfDay());
        accountResponseDto.setAccountNumber(201011L);
        accountResponseDto.setBalance(BigDecimal.valueOf(1000L));
        accountResponseDto.setCurrencyType(CurrencyType.INR);
        accountResponseDto.setId(1L);
    }
    @Test
    public void testCreateAccount() throws ServiceException {


        when(commonUtils.accountDetailsToAccountResponseDto(Mockito.<AccountDetails>any()))
                .thenReturn(accountResponseDto);
        when(commonUtils.accountDtoToAccountDetails(Mockito.<AccountDto>any())).thenReturn(new AccountDetails());

        AccountNumGenerator accountNumGenerator = new AccountNumGenerator();
        accountNumGenerator.setAccSequence(1L);
        when(iAccNumGenRepository.save(Mockito.<AccountNumGenerator>any())).thenReturn(accountNumGenerator);
        when(
                iAccountRepository.findByCustomer_CustomerIdAndCurrencyType(Mockito.<Long>any(), Mockito.<CurrencyType>any()))
                .thenReturn(new AccountDetails());
        when(iAccountRepository.save(Mockito.<AccountDetails>any())).thenReturn(new AccountDetails());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(Optional.empty());

        AccountDto accountDto = new AccountDto();

        accountDto.setBalance(BigDecimal.valueOf(1L));
        accountDto.setCurrencyType(CurrencyType.INR);
        AccountDto AccountDtoMock = mock(AccountDto.class);

        AccountResponseDto accountDetails = accountServiceImpl.createAccount(Mockito.<Long>any(), AccountDtoMock);
       // List<OrderPageDto> expected = service.getOrderPageDetails(Mockito.anyString());

        assertSame(accountResponseDto, accountDetails);
        assertEquals(accountResponseDto, accountDetails);
        verify(commonUtils).accountDetailsToAccountResponseDto(Mockito.<AccountDetails>any());
        verify(iAccountRepository).findByAccountNumber(Mockito.<Long>any());
        System.out.println("-------------------------------1");
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        System.out.println("-------------------------------2");

    }
    @Test
    public void testFetchByAccountNumber() throws ServiceException {

        when(commonUtils.accountDetailsToAccountResponseDto(Mockito.<AccountDetails>any()))
                .thenReturn(accountResponseDto);
        when(iAccountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(new AccountDetails());
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        AccountResponseDto actualFetchByAccountNumberResult = accountServiceImpl.fetchByAccountNumber(1L, 1234567890L);
        assertSame(accountResponseDto, actualFetchByAccountNumberResult);
        assertEquals("1000", actualFetchByAccountNumberResult.getBalance().toString());
        verify(commonUtils).accountDetailsToAccountResponseDto(Mockito.<AccountDetails>any());
        verify(iAccountRepository).findByAccountNumber(Mockito.<Long>any());
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
    }
    @Test
    public void testFetchAccountByCustomer() throws ServiceException {
        when(iAccountRepository.findByCustomer_CustomerId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        assertNull(accountServiceImpl.fetchAccountByCustomer(1L));
        verify(iAccountRepository).findByCustomer_CustomerId(Mockito.<Long>any());
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
    }
    @Test
    public void testUpdateAccount() throws ServiceException {
        when(iAccountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(new AccountDetails());
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);

        AccountUpdateDto accountDto = new AccountUpdateDto();
        accountDto.setAccountNumber(1234567890L);
        accountDto.setBalance(BigDecimal.valueOf(1L));

        accountServiceImpl.updateAccount(accountDto, 1L, 1234567890L);
    }

    @Test
    public void testDeleteAccount() throws ServiceException {
        when(iAccountRepository.findByAccountNumber(Mockito.<Long>any())).thenReturn(new AccountDetails());
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        accountServiceImpl.deleteAccount(201011L, 1L);
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        verify(accountServiceImpl).deleteAccount(Mockito.<Long>any(),Mockito.<Long>any());
    }
    @Test
    public void testFetchAllAccounts() throws ServiceException {
        ArrayList<AccountDetails> accountDetailsList = new ArrayList<>();
        accountDetailsList.add(new AccountDetails());
        when(iAccountRepository.findAll()).thenReturn(accountDetailsList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AccountResponseDto>>any()))
                .thenReturn(accountResponseDto);
        assertEquals(1, accountServiceImpl.fetchAllAccounts().size());
        verify(iAccountRepository).findAll();
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<AccountResponseDto>>any());
    }


}

