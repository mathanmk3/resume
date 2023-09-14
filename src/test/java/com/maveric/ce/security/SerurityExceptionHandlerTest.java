package com.maveric.ce.security;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PipedOutputStream;

import org.apache.catalina.connector.Response;
import org.apache.catalina.connector.ResponseFacade;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.mock.web.DelegatingServletOutputStream;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.core.AuthenticationException;

class SerurityExceptionHandlerTest {
    SerurityExceptionHandler handle = new SerurityExceptionHandler();
    @Test
    void testHandle() throws IOException {
        ServletOutputStream output = mock(ServletOutputStream.class);
        HttpServletRequest  request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        AccessDeniedException accessDeniedException = mock(AccessDeniedException.class);
        when(response.getOutputStream()).thenReturn(output);
        handle.handle(request, response, accessDeniedException);
        verify(response).setStatus(403);
        verify(response).setContentType(MediaType.APPLICATION_JSON_VALUE);

    }

}

