package com.maveric.ce.controller;

import com.maveric.ce.dto.AccountDto;
import com.maveric.ce.dto.AccountResponseDto;
import com.maveric.ce.dto.AccountUpdateDto;
import com.maveric.ce.service.AccountService;
import com.maveric.ce.userenum.CurrencyType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {AccountController.class})
@ExtendWith(SpringExtension.class)
class AccountControllerTest {


    @MockBean
    AccountService accountService;

    @Autowired
    AccountController accountController;

    @BeforeEach
    void setUp() {
    }

    @Test
    public void testCreateAccountSuccess() {
        // Arrange
        Long customerId = 1L;
        AccountDto accountDto = new AccountDto(CurrencyType.INR,new BigDecimal(1000)); // Provide valid account data here
        //when(validator.validate(accountDto)).thenReturn(Collections.emptySet()); // Mock validation to return no errors
        when(accountService.createAccount(customerId, accountDto)).thenReturn(new AccountResponseDto()); // Mock service response

        // Act
        ResponseEntity<AccountResponseDto> responseEntity = accountController.createAccount(customerId, accountDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testFetchAccountByNumberSuccess() {
        // Arrange
        Long customerId = 1L;
        Long accountNumber = 1011200L;
        AccountResponseDto expectedResponse = new AccountResponseDto(1L,1011200L,"veerakalyan",CurrencyType.INR,new BigDecimal(1000), LocalDateTime.now(), LocalDateTime.now()); // Create an expected response object
        when(accountService.fetchByAccountNumber(customerId, accountNumber)).thenReturn(expectedResponse); // Mock service response

        // Act
        ResponseEntity<AccountResponseDto> responseEntity = accountController.fetchAccountByNumber(customerId, accountNumber);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }



    @Test
    public void testFetchAccountByCustomerSuccess() {
        // Arrange
        Long customerId = 1L;
        List<AccountResponseDto> expectedResponse = Arrays.asList(new AccountResponseDto(1L,1011200L,"veerakalyan",CurrencyType.INR,new BigDecimal(1000), LocalDateTime.now(), LocalDateTime.now()),new AccountResponseDto(1L,200000L,"Akhil Birru",CurrencyType.USD,new BigDecimal(1000), LocalDateTime.now(), LocalDateTime.now())); // Create an expected response list
        when(accountService.fetchAccountByCustomer(customerId)).thenReturn(expectedResponse); // Mock service response

        // Act
        ResponseEntity<List<AccountResponseDto>> responseEntity = accountController.fetchAccountByCustomer(customerId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }


    @Test
    public void testGetAccountByCustomerSuccess() {
        // Arrange
        Long customerId = 1L;
        List<AccountResponseDto> expectedResponse = Arrays.asList(new AccountResponseDto(1L,1011200L,"veerakalyan",CurrencyType.INR,new BigDecimal(1000), LocalDateTime.now(), LocalDateTime.now()),new AccountResponseDto(1L,200000L,"Akhil Birru",CurrencyType.USD,new BigDecimal(1000), LocalDateTime.now(), LocalDateTime.now())); // Create an expected response list
        when(accountService.fetchAccountByCustomer(customerId)).thenReturn(expectedResponse); // Mock service response

        // Act
        ResponseEntity<List<AccountResponseDto>> responseEntity = accountController.getAccountByCustomer(customerId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testUpdateAccountSuccess() {
        // Arrange
        Long customerId = 1L;
        Long accountNumber = 1011200L;
        AccountUpdateDto accountUpdateDto = new AccountUpdateDto(new BigDecimal(1600),accountNumber); // Provide valid account update data here
        AccountResponseDto expectedResponse = new AccountResponseDto(); // Create an expected response object
        when(accountService.updateAccount(accountUpdateDto, customerId, accountNumber)).thenReturn(expectedResponse); // Mock service response

        // Act
        ResponseEntity<AccountResponseDto> responseEntity = accountController.updateAccount(accountUpdateDto, customerId, accountNumber);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    public void testDeleteAccount_Success() {
        // Arrange
        Long accountNumber = 1011200L;
        Long customerId = 1L;

        when(accountService.deleteAccount(accountNumber, customerId)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> response = accountController.deleteAccount(accountNumber, customerId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody());
    }





}