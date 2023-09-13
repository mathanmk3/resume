package com.maveric.ce.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.maveric.ce.dto.*;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.response.LoginResponse;
import com.maveric.ce.service.CustomerService;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.service.spi.InjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;



@ContextConfiguration(classes = {CustomerController.class})
@ExtendWith(SpringExtension.class)
class CustomerControllerTest {



    @MockBean
    CustomerService service;

    @Autowired
    CustomerController customerController;



    @BeforeEach
    void setUp() {

    }


    @Test
    void testCreateCustomer() throws Exception {
        CustomerDto requestData=new CustomerDto(1L,"veerakalyan","nanubala","veerakalyan07","veera","veera@gmail.com","9652783005", RolesEnum.CUSTOMER, Gender.Male);
        when(service.createCustomer(Mockito.<CustomerDto>any()))
                .thenReturn(mock(CustomerResponseDto.class));


        String request = (new ObjectMapper()).writeValueAsString(requestData);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request);

        MockMvcBuilders.standaloneSetup(customerController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk());

    }


    @Test
    public void testDeleteCustomerSuccess() {
        // Arrange
        Long customerId = 1L;
        when(service.deleteCustomer(customerId)).thenReturn(true);

        // Act
        ResponseEntity<Boolean> responseEntity = customerController.deleteCustomer(customerId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody());
    }


    @Test
    public void testUpdateCustomerSuccess() {
        // Arrange
        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("veerakalyan","veera","veera@gmail.com","9652783005"); // Provide valid customer data here
        Long customerId = 1L;
        when(service.updateCustomer(customerUpdateDto, customerId)).thenReturn(new CustomerResponseDto()); // Mock service response

        // Act
        ResponseEntity<CustomerResponseDto> responseEntity = customerController.updateCustomer(customerUpdateDto, customerId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testFetchAllCustomersSuccess() {
        // Arrange
        List<CustomerResponseDto> customerList = Arrays.asList(new CustomerResponseDto(), new CustomerResponseDto()); // Provide a list of customer data
        when(service.fetchAllCustomers()).thenReturn(customerList); // Mock service response

        // Act
        ResponseEntity<List<CustomerResponseDto>> responseEntity = customerController.fetchAllCustomers();

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(customerList, responseEntity.getBody());
    }


}