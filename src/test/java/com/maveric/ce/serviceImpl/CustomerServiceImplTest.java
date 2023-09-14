package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.*;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import com.maveric.ce.utils.CommonUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {CustomerServiceImpl.class})
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {
    @MockBean
    private CommonUtils commonUtils;
    @Autowired
    private CustomerServiceImpl customerServiceImpl;
    @MockBean
    private IAccountRepository iAccountRepository;
    @MockBean
    private ICustomerRepository iCustomerRepository;
    @MockBean
    private ModelMapper modelMapper;
    @MockBean
    private PasswordEncoder passwordEncoder;

    CustomerDetails customerDetails = new CustomerDetails();
    CustomerResponseDto customerResponseDto = new CustomerResponseDto();
    CustomerDto customerDto = new CustomerDto();
    CustomerFetchResponseDto customerFetchResponseDto = new CustomerFetchResponseDto();

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
        customerDetails.setPassword("password");
        customerDetails.setPhoneNumber("9488101204");
        customerDetails.setRolesENum(RolesEnum.CUSTOMER);
        customerDetails.setUsername("mathanmk");

        customerResponseDto.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerResponseDto.setCustomerId(1L);
        customerResponseDto.setEmail("mathan@gmail.com");
        customerResponseDto.setFirstName("mathan");
        customerResponseDto.setGender(Gender.Male);
        customerResponseDto.setLastName("mk");
        customerResponseDto.setLastUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerResponseDto.setPassword("password");
        customerResponseDto.setPhoneNumber("9488101204");
        customerResponseDto.setRolesENum(RolesEnum.CUSTOMER);
        customerResponseDto.setUsername("mathanmk");

        customerDto.setCustomerId(1L);
        customerDto.setEmail("mathan@gmail.com");
        customerDto.setFirstName("mathan");
        customerDto.setGender(Gender.Male);
        customerDto.setLastName("mk");
        customerDto.setPassword("password");
        customerDto.setPhoneNumber("9488101204");
        customerDto.setRolesENum(RolesEnum.CUSTOMER);
        customerDto.setUsername("mathanmk");

        customerFetchResponseDto.setCreatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerFetchResponseDto.setCustomerId(1L);
        customerFetchResponseDto.setEmail("mathan@gmail.com");
        customerFetchResponseDto.setFirstName("mathan");
        customerFetchResponseDto.setGender(Gender.Male);
        customerFetchResponseDto.setLastName("mk");
        customerFetchResponseDto.setLastUpdatedAt(LocalDate.of(1970, 1, 1).atStartOfDay());
        customerFetchResponseDto.setPassword("password");
        customerFetchResponseDto.setPhoneNumber("9488101204");
        customerFetchResponseDto.setRolesENum(RolesEnum.CUSTOMER);
        customerFetchResponseDto.setUsername("mathanmk");
    }

    @Test
    public void testCreateCustomer() {
        //
        when(iCustomerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(customerDto.getPassword())).thenReturn("encodedPassword");
        when(commonUtils.customerDtoToCustomerDetails(Mockito.<CustomerDto>any())).thenReturn(customerDetails);
        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        CustomerResponseDto response = customerServiceImpl.createCustomer(customerDto);
        assertSame(response,customerResponseDto);
        assertEquals("mathan@gmail.com", response.getEmail());
    }

    @Test
    public void testCreateCustomerMailIdExist() {
        when(iCustomerRepository.findByEmail(customerDto.getEmail())).thenReturn(Optional.of(customerDetails));
        when(passwordEncoder.encode(customerDto.getPassword())).thenReturn("encodedPassword");
        when(commonUtils.customerDtoToCustomerDetails(Mockito.<CustomerDto>any())).thenReturn(customerDetails);
        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        try {
            CustomerResponseDto response = customerServiceImpl.createCustomer(customerDto);
        }catch (ServiceException e){
            assertSame(ErrorCodes.EMAIL_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomer(){
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        doNothing().when(iCustomerRepository).deleteById(Mockito.<Long>any());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setEmail("kumar@gmai.com");
        updateDto.setUsername("mathanmk");
        updateDto.setPassword("mathan33");
        updateDto.setPhoneNumber("94888101204");

        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(Optional.empty());
        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        CustomerResponseDto response = customerServiceImpl.updateCustomer(updateDto,1L);
        assertSame(customerResponseDto,response);
    }

    @Test
    public void testUpdateCustomerSameMailExit(){
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        doNothing().when(iCustomerRepository).deleteById(Mockito.<Long>any());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setEmail("mathan@gmail.com");
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult);
        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        try {
            CustomerResponseDto response = customerServiceImpl.updateCustomer(updateDto, 11L);
        }catch (ServiceException e){
            assertSame(ErrorCodes.EMAIL_ALREADY_EXISTS, e.getMessage());
        }
    }

    @Test
    public void testUpdateCustomerNoCustomerFound(){

        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(Optional.empty());
        CustomerUpdateDto updateDto = new CustomerUpdateDto();
        updateDto.setEmail("mathan@gmail.com");
        try {
            CustomerResponseDto response = customerServiceImpl.updateCustomer(updateDto, 1L);
        }catch (ServiceException e){
            assertSame(ErrorCodes.CUSTOMER_NOT_FOUND, e.getMessage());
        }
    }




    @Test
    void testDeleteCustomer() {
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        doNothing().when(iCustomerRepository).deleteById(Mockito.<Long>any());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(customerServiceImpl.deleteCustomer(1L));
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        verify(iCustomerRepository).deleteById(Mockito.<Long>any());
    }

    @Test
    void testDeleteCustomerWhenNoCustomerFound() {
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(Optional.empty());
        try {
            customerServiceImpl.deleteCustomer(1L);
        }catch (ServiceException e) {
            assertSame(ErrorCodes.CUSTOMER_NOT_FOUND, e.getMessage());
            verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
       }
    }
    @Test
    void testFetchCustomerById() {
        when(commonUtils.customerToCustomerFetchResponseDto(Mockito.<CustomerDetails>any()))
                .thenReturn(customerFetchResponseDto);
        when(iAccountRepository.findByCustomer_CustomerId(Mockito.<Long>any())).thenReturn(new ArrayList<>());
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        assertSame(customerFetchResponseDto, customerServiceImpl.fetchCustomerById(1L));
        verify(commonUtils).customerToCustomerFetchResponseDto(Mockito.<CustomerDetails>any());
        verify(iAccountRepository).findByCustomer_CustomerId(Mockito.<Long>any());
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
    }

    @Test
    void testFetchCustomerByIdWhenNoCustomerFound() {
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(Optional.empty());
        try {
            assertSame(customerFetchResponseDto, customerServiceImpl.fetchCustomerById(1L));
        }catch (ServiceException e){
            assertSame(ErrorCodes.CUSTOMER_NOT_FOUND, e.getMessage());
            verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        }
    }
    @Test
    void testFetchAllCustomers() {
        ArrayList<CustomerDetails> customerDetailsList = new ArrayList<>();
        customerDetailsList.add(customerDetails);
        when(iCustomerRepository.findAll()).thenReturn(customerDetailsList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<CustomerResponseDto>>any()))
                .thenReturn(customerResponseDto);
        assertEquals(true, !customerServiceImpl.fetchAllCustomers().isEmpty());
        verify(iCustomerRepository).findAll();
        verify(modelMapper).map(Mockito.<Object>any(), Mockito.<Class<CustomerResponseDto>>any());
    }

}

