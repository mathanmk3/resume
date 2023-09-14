package com.maveric.ce.utils;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.*;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.userenum.CurrencyType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ContextConfiguration(classes = {CommonUtils.class})
@ExtendWith(SpringExtension.class)
class CommonUtilsTest {
    @Autowired
    private CommonUtils commonUtils;

    @MockBean
    private ModelMapper modelMapper;



    @Test
    void testCustomerToCustomerResponseDto() {
        CommonUtils utils = new CommonUtils();
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("mathan");
        customerDetails.setLastName("kumar");
        customerDetails.setEmail("mathan33@example.com");
        customerDetails.setUsername("mathan33");
        customerDetails.setPassword("mathan33#");
        CustomerResponseDto customerDto = utils.customerToCustomerResponseDto(customerDetails);
        assertEquals(customerDetails.getFirstName(), customerDto.getFirstName());
        assertEquals(customerDetails.getLastName(), customerDto.getLastName());
        assertEquals(customerDetails.getEmail(), customerDto.getEmail());
    }

    @Test
    void testaccountDetailsToAccountResponseDto() {
        CommonUtils utils = new CommonUtils();
        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccHolderName("mathan kumar");
        accountDetails.setBalance(BigDecimal.valueOf(500L));
        accountDetails.setAccountNumber(1234L);
        AccountResponseDto responseDto = utils.accountDetailsToAccountResponseDto(accountDetails);
        assertEquals(accountDetails.getAccHolderName(), responseDto.getAccHolderName());
        assertEquals(accountDetails.getBalance(), responseDto.getBalance());
    }

    @Test
     void testaccountDtoToAccountDetails() {
         CommonUtils utils = new CommonUtils();
         AccountDto accountDto = new AccountDto();
         accountDto.setBalance(BigDecimal.valueOf(500L));
         accountDto.setCurrencyType(CurrencyType.AED);
         AccountDetails accountDetailsResp = utils.accountDtoToAccountDetails(accountDto);
         assertEquals(accountDto.getCurrencyType(), accountDetailsResp.getCurrencyType());
         assertEquals(accountDto.getBalance(), accountDetailsResp.getBalance());
    }

    @Test
    void testCustomerDtoToCustomerDetails() {
        CommonUtils utils = new CommonUtils();
        CustomerDto inputDto = new CustomerDto();
        inputDto.setFirstName("mathan");
        inputDto.setLastName("kumar");
        inputDto.setEmail("mathan33@example.com");
        inputDto.setUsername("mathan33");
        inputDto.setPassword("mathan33#");
        CustomerDetails response = utils.customerDtoToCustomerDetails(inputDto);
        assertEquals(response.getFirstName(), inputDto.getFirstName());
        assertEquals(response.getLastName(), inputDto.getLastName());
        assertEquals(response.getEmail(), inputDto.getEmail());
    }


    @Test
    void testCustomerToCustomerFetchResponseDto() {
        // Arrange
        CustomerDetails customerDetails = new CustomerDetails();
        customerDetails.setFirstName("John");
        customerDetails.setLastName("Doe");
        customerDetails.setEmail("john@example.com");

        AccountDetails accountDetails = new AccountDetails();
        accountDetails.setAccHolderName("mathan kumar");
        accountDetails.setBalance(BigDecimal.valueOf(500L));
        accountDetails.setAccountNumber(1234L);

        AccountDetails accountDetails1 = new AccountDetails();
        accountDetails.setAccHolderName("mathan kumar");
        accountDetails.setBalance(BigDecimal.valueOf(500L));
        accountDetails.setAccountNumber(1234L);

        List<AccountDetails> accountDetailsList = new ArrayList<>();
        accountDetailsList.add(accountDetails);
        accountDetailsList.add(accountDetails1);

        AccountResponseDto responseDto = new AccountResponseDto();
        responseDto.setAccountNumber(1234L);

        customerDetails.setAccountDetails(accountDetailsList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<AccountResponseDto>>any()))
                .thenReturn(responseDto);
        CustomerFetchResponseDto result = commonUtils.customerToCustomerFetchResponseDto(customerDetails);
        assertEquals(customerDetails.getFirstName(), result.getFirstName());
        assertEquals(customerDetails.getLastName(), result.getLastName());
        assertEquals(customerDetails.getEmail(), result.getEmail());
        assertEquals(accountDetailsList.size(), result.getAccountResponseDtoList().size());
        assertEquals(accountDetails.getAccountNumber(), result.getAccountResponseDtoList().get(0).getAccountNumber());
    }

    @Test
    @Disabled
    void testGetMapper() throws InterruptedException {
        String mapFrom = "MapValue";
        Class<String> mapTo = String.class;
        String result = commonUtils.getMapper(mapFrom, mapTo);
        assertEquals(mapFrom, result);
    }
    @Test
    void testCheckNullable() {
        OrderDto dto = new OrderDto();
        dto.setCurrencyRate(BigDecimal.valueOf(1.7));
        assertFalse(CommonUtils.checkNullable(dto));
    }
    @Test
    @Disabled
    void testCheckNullableAndEmptyWithNonEmptyString() {
        String str = "mathan kumar";
        assertNotNull(commonUtils.checkNullableAndEmpty(str));
        assertFalse(commonUtils.checkNullableAndEmpty(str).get());
    }

    @Test
    @Disabled
    void checkGreaterThan(){
       assertTrue(commonUtils.checkGreaterThan(10));
    }

}

