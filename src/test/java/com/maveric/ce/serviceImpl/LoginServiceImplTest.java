package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.LoginDto;
import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.response.LoginResponse;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import com.maveric.ce.utils.JWTUtils;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {LoginServiceImpl.class})
@ExtendWith(SpringExtension.class)
class LoginServiceImplTest {
    @MockBean
    private ICustomerRepository iCustomerRepository;

    @MockBean
    private JWTUtils jWTUtils;

    @MockBean
    private LoginResponse loginResponse;

    @Autowired
    private LoginServiceImpl loginServiceImpl;

    @MockBean
    private PasswordEncoder passwordEncoder;

    CustomerDetails customerDetails = new CustomerDetails();

    @BeforeEach
    public void setUp(){
        customerDetails.setAccountDetails(new ArrayList<>());
        customerDetails.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerDetails.setCustomerId(1L);
        customerDetails.setEmail("mathan@gmail.com");
        customerDetails.setFirstName("mathan");
        customerDetails.setGender(Gender.Male);
        customerDetails.setLastName("mk");
        customerDetails.setLastUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerDetails.setPassword("mathanaa");
        customerDetails.setPhoneNumber("6625550144");
        customerDetails.setRolesENum(RolesEnum.CUSTOMER);
        customerDetails.setUsername("mathanmk");
    }

    @Test
    void testFindUserByMailId() throws ServiceException {
        Optional<CustomerDetails> mockResult = Optional.of(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(mockResult);
        doNothing().when(loginResponse).setCustomerId(Mockito.<Long>any());
        doNothing().when(loginResponse).setRole(Mockito.<String>any());
        when(jWTUtils.generateToken(Mockito.<UserDetailsImpl>any(), Mockito.<HttpServletRequest>any(),
                Mockito.<LoginResponse>any())).thenReturn(loginResponse);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(true);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mathan@gmail.com");
        loginDto.setPassword("mathan");
        assertSame(loginServiceImpl.loginResponse,
                loginServiceImpl.findUserByMailId(loginDto, new MockHttpServletRequest()));
        verify(iCustomerRepository).findByEmail(Mockito.<String>any());
        verify(jWTUtils).generateToken(Mockito.<UserDetailsImpl>any(), Mockito.<HttpServletRequest>any(),
                Mockito.<LoginResponse>any());
        verify(loginResponse).setCustomerId(Mockito.<Long>any());
        verify(loginResponse).setRole(Mockito.<String>any());
        verify(passwordEncoder).matches(Mockito.<CharSequence>any(), Mockito.<String>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }

    @Test
    void testFindUserByMailIdInvalidUserName() throws ServiceException {
        Optional<CustomerDetails> mockResult = Optional.of(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mathan@gmail.com");
        loginDto.setPassword("mathan");
        try {
            loginServiceImpl.findUserByMailId(loginDto, new MockHttpServletRequest());
        }catch (ServiceException e ){
            assertSame(ErrorCodes.INVALID_AUTHENTICATION, e.getMessage());
            verify(iCustomerRepository).findByEmail(Mockito.<String>any());
        }
    }

    @Test
    void testFindUserByMailIdInvalidPassword() throws ServiceException {
        Optional<CustomerDetails> mockResult = Optional.of(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(mockResult);
        doNothing().when(loginResponse).setCustomerId(Mockito.<Long>any());
        doNothing().when(loginResponse).setRole(Mockito.<String>any());
        when(jWTUtils.generateToken(Mockito.<UserDetailsImpl>any(), Mockito.<HttpServletRequest>any(),
                Mockito.<LoginResponse>any())).thenReturn(loginResponse);
        when(passwordEncoder.matches(Mockito.<CharSequence>any(), Mockito.<String>any())).thenReturn(false);
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("mathan@gmail.com");
        loginDto.setPassword("mathan");
        try {
            loginServiceImpl.findUserByMailId(loginDto, new MockHttpServletRequest());
        }catch (ServiceException e ){
            assertSame(ErrorCodes.INVALID_AUTHENTICATION, e.getMessage());
            verify(iCustomerRepository).findByEmail(Mockito.<String>any());
        }
    }

    @Test
    void testLoadUserByUsername() throws UsernameNotFoundException {
        Optional<CustomerDetails> customerDetailsMock = Optional.of(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(customerDetailsMock);
        UserDetailsImpl uernameResult = loginServiceImpl.loadUserByUsername("42");
        assertEquals("mathanmk", uernameResult.getUsername());
        assertEquals("mathanaa", uernameResult.getPassword());
        assertEquals("mathan@gmail.com", uernameResult.getEmailId());
        Collection<? extends GrantedAuthority> authorities = uernameResult.getAuthorities();
        verify(iCustomerRepository).findByEmail(Mockito.<String>any());
    }
    @Test
    void testLoadUserByUsernameInvalidUser() throws UsernameNotFoundException {
        Optional<CustomerDetails> customerDetailsMock = Optional.of(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        try {
            UserDetailsImpl uernameResult = loginServiceImpl.loadUserByUsername("42");
        }catch (ServiceException e){
            assertSame(ErrorCodes.CUSTOMER_NOT_FOUND, e.getMessage());
            verify(iCustomerRepository).findByEmail(Mockito.<String>any());

        }

    }

}

