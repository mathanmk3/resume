package com.maveric.ce.controller;

import com.maveric.ce.dto.OrderDto;
import com.maveric.ce.dto.WatchListDto;
import com.maveric.ce.response.OrderResponse;
import com.maveric.ce.service.CurrencyExchangeOrderService;
import com.maveric.ce.serviceImpl.AccountServiceImpl;
import com.maveric.ce.serviceImpl.CurrencyExchangeOrderServiceImpl;
import com.maveric.ce.utils.CommonUtils;
import com.maveric.ce.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {OrderController.class})
@ExtendWith(SpringExtension.class)
class OrderControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(OrderControllerTest.class);

    @MockBean
    private CurrencyExchangeOrderServiceImpl currencyExchangeOrderService;

    @Autowired
    private OrderController orderController;
    @MockBean
    private HttpServletRequest request;



    @MockBean
    OrderResponse response;


    static MockedStatic<JWTUtils> jwtUtils;

    @BeforeAll
    static void setup() {
        jwtUtils = Mockito.mockStatic(JWTUtils.class);
    }



    @BeforeEach
    void setUp() {

    }


    @Test
    public void testPlaceOrderValidInput() throws Exception {
        // Prepare test data
        logger.info("Called testPlaceOrderValid()");
        OrderDto inputOrderDto = new OrderDto(); // You should create a valid input here
        OrderDto expectedOrderDetails = new OrderDto(); // You should create expected output data here

        // Mock the behavior of the orderService.newOrder method
        when(currencyExchangeOrderService.newOrder(inputOrderDto)).thenReturn(expectedOrderDetails);

        // Execute the method under test
        ResponseEntity<OrderResponse> responseEntity = orderController.placeOrder(inputOrderDto, request);
        logger.info("ResponseEntity body:"+responseEntity.getBody().getMessage());
        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        OrderResponse responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        logger.info("responseBody:"+responseBody);
        logger.info("response body message:"+responseBody.getMessage());
        assertEquals("Currency Exchanges", responseBody.getMessage());
        assertEquals(expectedOrderDetails, responseBody.getOrderData());


    }


    @Test
    public void testGetWatchList() throws Exception {
        // Prepare test data
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ2ZWVyYUBnbWFpbC5jb20iLCJpYXQiOjE2OTQ0MzEzODcsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6OTcwMS9hdXRoZW50aWNhdGUvbG9naW4iLCJleHAiOjE2OTQ0MzMxODcsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1NVUEVSVVNFUiJ9XSwidXNlck5hbWUiOiJ2ZWVyYSJ9.iMyW6Z8No0yXi-L-XolA5wsu97Ge1tpH5J-yzhRcz0A"; // Provide a valid token for testing
        String userMailId = "veera@gmail.com"; // Expected user email extracted from the token
        List<WatchListDto> expectedWatchList = new ArrayList<>(); // Prepare expected watch list data


        // Mock the behavior of the JWTUtils.extractUserMailId method
        jwtUtils.when(() -> JWTUtils.extractUserMailId(token)).thenReturn(userMailId);


        // Mock the behavior of the currencyExchangeOrderService.getOrderWatchList method
        when(currencyExchangeOrderService.getOrderWatchList(userMailId)).thenReturn(expectedWatchList);

        // Execute the method under test
        ResponseEntity<List<WatchListDto>> responseEntity = orderController.getWatchList(request);

        // Verify that the response is as expected
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<WatchListDto> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertEquals(expectedWatchList, responseBody);


    }
}