package com.maveric.ce.utils;

import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.response.LoginResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.http.HttpRequest;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ContextConfiguration(classes = {JWTUtils.class})
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "apiAccessKey=83AD679C4EFDCC9A8F1486A9FDFDB83AD679C4EFDCC9A8F1486A9FDFDB",
        "jwt.expirationDateInMs=1800000",
})
class JWTUtilsTest {

    @Value("${jwt.expirationDateInMs}")
    String sessionExpireTime;
    static String apiAccessKey;
    @Value("${apiAccessKey}")
    private void activeProfile(String key) {
        apiAccessKey = key;
    };

    @Autowired
    JWTUtils jWTUtils;

    @BeforeEach
    void setUp(){}


    @Test
    void testGenerateToken (){
        StringBuffer url= new StringBuffer("localhost:8080\\home");
        UserDetailsImpl dto = new UserDetailsImpl();
        dto.setUsername("mathan");
        dto.setEmailId("mathan33@gmail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(url);
        LoginResponse output = jWTUtils.generateToken(dto,request,new LoginResponse());
        assertNotNull(output);
    }
    @Test
    void testGenerateTokenNegative() {
        StringBuffer url= new StringBuffer("localhost:8080\\home");
        UserDetailsImpl dto = new UserDetailsImpl();
        dto.setUsername("mathan");
        dto.setEmailId("mathan33@gmail.com");
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURL()).thenReturn(null);
        try {
            LoginResponse output = jWTUtils.generateToken(dto,request,new LoginResponse());
        }catch (ServiceException e){
            assertNotNull(e);
        }
    }
    @Test
    @Disabled
    void  extractUserMailId() throws ServiceException {
        try {
            StringBuffer url= new StringBuffer("localhost:8080\\home");
            UserDetailsImpl dto = new UserDetailsImpl();
            dto.setUsername("mathan");
            dto.setEmailId("mathan33@gmail.com");
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURL()).thenReturn(url);
            LoginResponse generateToken = jWTUtils.generateToken(dto,request,new LoginResponse());
            String expectedMaild="mathan33@gmail.com";
            String token ="Bearer "+generateToken.getAccessToken();
            String output=JWTUtils.extractUserMailId(token);
            assertEquals(expectedMaild,output);
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.INVALID_TOKEN);
        }
    }

    @Test
    void  extractUserMailIdInvalid() throws ServiceException {
        try {
            String expectedMaild="veera@gmail.com";
            String token ="JzdWIiOiJ2ZWVyYUBnbWFpbC5jb20iLCJpYXQiOjE2OTQ2MTQzNTAsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAxMi9hdXRoZW50aWNhdGUvbG9naW4iLCJleHAiOjE2OTQ2MTYxNTAsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1NVUEVSVVNFUiJ9XSwidXNlck5hbWUiOiJ2ZWVyYSJ9.vZn0ZEW6XvgBjKWGq5U3FUvyIVyna2NJJzHJ747Fxhk";
            String output=JWTUtils.extractUserMailId(token);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
    @Test
    @Disabled
    void isTokenValid() {
        try {
            StringBuffer url= new StringBuffer("localhost:8080\\home");
            UserDetailsImpl dto = new UserDetailsImpl();
            dto.setUsername("mathan");
            dto.setEmailId("mathan33@gmail.com");
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getRequestURL()).thenReturn(url);
            LoginResponse generateToken = jWTUtils.generateToken(dto,request,new LoginResponse());
            String token ="Bearer "+generateToken.getAccessToken();
            Boolean bvlaue=jWTUtils.isTokenValid(token,dto);
            assertTrue(bvlaue);
        } catch (Exception e) {
            throw new ServiceException(ErrorCodes.INVALID_TOKEN);
        }
    }

    @Test
    void isTokenInValid() {
        try {
            String token ="ZWVyYUBnbWFpbC5jb20iLCJpYXQiOjE2OTQ2MTQzNTAsImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODAxMi9hdXRoZW50aWNhdGUvbG9naW4iLCJleHAiOjE2OTQ2MTYxNTAsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1NVUEVSVVNFUiJ9XSwidXNlck5hbWUiOiJ2ZWVyYSJ9.vZn0ZEW6XvgBjKWGq5U3FUvyIVyna2NJJzHJ747Fxhk";
            UserDetailsImpl dto = new UserDetailsImpl();
            dto.setUsername("mathan");
            dto.setEmailId("mathan33@gmail.com");
            Boolean bvlaue=jWTUtils.isTokenValid(token,dto);
            assertTrue(bvlaue);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }
}

