package com.maveric.ce.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.catalina.connector.CoyoteOutputStream;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.DelegatingServletInputStream;
import org.springframework.mock.web.MockHttpServletMapping;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {NoResourceHandler.class})
@ExtendWith(SpringExtension.class)
class NoResourceHandlerTest {
    @Autowired
    private NoResourceHandler noResourceHandler;

    @Test
    void testCommence2() throws IOException {
        HttpServletRequestWrapper request = mock(HttpServletRequestWrapper.class);
        Response response = new Response();
        noResourceHandler.commence(request, response, new AccountExpiredException("Msg"));
        HttpServletResponse response2 = response.getResponse();
        assertTrue(response2 instanceof ResponseFacade);
        assertSame(response.getOutputStream(), response2.getOutputStream());
    }

    @Test
    void testCommence() throws IOException {
        NoResourceHandler noResourceHandler = new NoResourceHandler();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletOutputStream output = mock(ServletOutputStream.class);
        AuthenticationException authenticationException = mock(AuthenticationException.class);
        when(response.getOutputStream()).thenReturn(output);
        noResourceHandler.commence(request, response, authenticationException);
        verify(response).setStatus(404);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

    }
}

