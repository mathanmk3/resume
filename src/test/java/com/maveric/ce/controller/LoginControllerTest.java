package com.maveric.ce.controller;

import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.ce.dto.LoginDto;
import com.maveric.ce.response.LoginResponse;
import com.maveric.ce.serviceImpl.LoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {LoginController.class})
@ExtendWith(SpringExtension.class)
class LoginControllerTest {
    @Autowired
    private LoginController loginController;
    @MockBean
    private LoginServiceImpl loginServiceImpl;
    @Test
    void testCreateCustomer() throws Exception {
        when(loginServiceImpl.findUserByMailId(Mockito.<LoginDto>any(), Mockito.<HttpServletRequest>any()))
                .thenReturn(new LoginResponse());

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mathanmk@example.com");
        loginDto.setPassword("mathan33");
        String request = (new ObjectMapper()).writeValueAsString(loginDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/authenticate/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request);

        MockMvcBuilders.standaloneSetup(loginController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"accessToken\":null,\"refreshToken\":null,\"role\":null,\"customerId\":null}"));
    }
}

