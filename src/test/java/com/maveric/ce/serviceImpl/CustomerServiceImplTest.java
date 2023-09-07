package com.maveric.ce.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.CustomerDto;
import com.maveric.ce.dto.CustomerFetchResponseDto;
import com.maveric.ce.dto.CustomerResponseDto;
import com.maveric.ce.dto.CustomerUpdateDto;
import com.maveric.ce.entity.CustomerDetails;
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
    void testDeleteCustomer() {
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        doNothing().when(iCustomerRepository).deleteById(Mockito.<Long>any());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        assertTrue(customerServiceImpl.deleteCustomer(1L));
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        verify(iCustomerRepository).deleteById(Mockito.<Long>any());
    }
    @Test
    void testDeleteCustomerNegative() {
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        doThrow(new ServiceException("An error occurred")).when(iCustomerRepository).deleteById(Mockito.<Long>any());
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        customerServiceImpl.deleteCustomer(1L);
    }
    @Test
    void testUpdateCustomer() {

        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        Optional<CustomerDetails> ofResult2 = Optional.of(customerDetails);
        when(iCustomerRepository.save(Mockito.<CustomerDetails>any())).thenReturn(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult2);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenReturn("secret");

        CustomerUpdateDto customerDto = new CustomerUpdateDto();
        customerDto.setEmail("mathan@gmail.com");
        customerDto.setPassword("password");
        customerDto.setPhoneNumber("9488101204");
        customerDto.setUsername("mathanmk");
        assertSame(customerResponseDto, customerServiceImpl.updateCustomer(customerDto, 1L));
        verify(commonUtils).customerToCustomerResponseDto(Mockito.<CustomerDetails>any());
        verify(iCustomerRepository).save(Mockito.<CustomerDetails>any());
        verify(iCustomerRepository).findByEmail(Mockito.<String>any());
        verify(iCustomerRepository).findBycustomerId(Mockito.<Long>any());
        verify(passwordEncoder).encode(Mockito.<CharSequence>any());
    }
    @Test
    void testUpdateCustomerNegative() {
        when(commonUtils.customerToCustomerResponseDto(Mockito.<CustomerDetails>any())).thenReturn(customerResponseDto);
        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        Optional<CustomerDetails> ofResult2 = Optional.of(customerDetails);
        when(iCustomerRepository.save(Mockito.<CustomerDetails>any())).thenReturn(customerDetails);
        when(iCustomerRepository.findByEmail(Mockito.<String>any())).thenReturn(ofResult2);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        when(passwordEncoder.encode(Mockito.<CharSequence>any())).thenThrow(new ServiceException("An error occurred"));
        CustomerUpdateDto customerDto = new CustomerUpdateDto();
        customerDto.setEmail("mathan@gmail.com");
        customerDto.setPassword("password");
        customerDto.setPhoneNumber("9488101204");
        customerDto.setUsername("mathanmk");
        customerServiceImpl.updateCustomer(customerDto, 1L);
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
    void testFetchCustomerByIdNegative() {
        when(commonUtils.customerToCustomerFetchResponseDto(Mockito.<CustomerDetails>any()))
                .thenReturn(customerFetchResponseDto);
        when(iAccountRepository.findByCustomer_CustomerId(Mockito.<Long>any()))
                .thenThrow(new ServiceException("An error occurred"));

        Optional<CustomerDetails> ofResult = Optional.of(customerDetails);
        when(iCustomerRepository.findBycustomerId(Mockito.<Long>any())).thenReturn(ofResult);
        customerServiceImpl.fetchCustomerById(1L);

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
    @Test
    void testFetchAllCustomersDeatils() {
        ArrayList<CustomerDetails> customerDetailsList = new ArrayList<>();
        customerDetailsList.add(customerDetails);
        customerDetailsList.add(customerDetails);
        when(iCustomerRepository.findAll()).thenReturn(customerDetailsList);
        when(modelMapper.map(Mockito.<Object>any(), Mockito.<Class<CustomerResponseDto>>any()))
                .thenReturn(customerResponseDto);
        assertEquals(true, !customerServiceImpl.fetchAllCustomers().isEmpty());
        verify(iCustomerRepository).findAll();
    }

}

