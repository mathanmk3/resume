package com.maveric.ce.serviceImpl;

import com.maveric.ce.dto.CustomerDto;
import com.maveric.ce.dto.CustomerFetchResponseDto;
import com.maveric.ce.dto.CustomerResponseDto;
import com.maveric.ce.dto.CustomerUpdateDto;
import com.maveric.ce.entity.AccountDetails;
import com.maveric.ce.entity.CustomerDetails;
import com.maveric.ce.exceptions.ErrorCodes;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.repository.IAccountRepository;
import com.maveric.ce.repository.ICustomerRepository;
import com.maveric.ce.service.CustomerService;
import com.maveric.ce.utils.CommonUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerServiceImpl.class);
    @Autowired
    private ICustomerRepository iCustomerRepository;

    @Autowired
    private IAccountRepository iAccountRepository ;

    @Autowired
    CommonUtils commonUtils;

    @Autowired
    ModelMapper modelMapper;


    @Override
    public CustomerResponseDto createCustomer(CustomerDto customerDto) {

        CustomerDetails customerDetails = commonUtils.customerDtoToCustomerDetails(customerDto);
        logger.info("CustomerDetails: ",customerDetails);

        customerDetails.setCreatedAt( LocalDateTime.now());

        CustomerDetails savedCustomerDetails= iCustomerRepository.save(customerDetails);
        CustomerResponseDto savedCustomerDto = commonUtils.customerToCustomerResponseDto(savedCustomerDetails);;

        return savedCustomerDto;
    }

    public String deleteCustomer(Long customerId) {
        Optional<CustomerDetails> customerDetails = iCustomerRepository.findBycustomerId(customerId);
        logger.info("after find customer by id in delete:",customerDetails);
        logger.info("delete customer object:"+customerDetails);
        if (!customerDetails.isEmpty()) {
            logger.info("customer found to delete");
            iCustomerRepository.deleteById(customerDetails.get().getCustomerId());
            return "Customer with Id: " + customerId + " deleted successfully";
        } else {
            logger.info("Customer not found to delete");
            throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
        }

    }

    @Override
    public CustomerResponseDto updateCustomer(CustomerUpdateDto customerDto)  {

        Optional<CustomerDetails> customerDetails1 = iCustomerRepository.findBycustomerId(customerDto.getCustomerId());

        logger.info("update customer customer found: ",customerDetails1);
        System.out.println("update customer object:"+customerDetails1);
        if (!customerDetails1.isEmpty()) {
           logger.info("customer found with id");
            CustomerDetails customerDetails = customerDetails1.get();
            if (customerDto.getUsername() != null && !customerDto.getUsername().equals(""))
                customerDetails.setUsername(customerDto.getUsername());
            if (customerDto.getEmail() != null && !customerDto.getEmail().equals(""))
                customerDetails.setEmail(customerDto.getEmail());

            if (customerDto.getPassword() != null && !customerDto.getPassword().equals(""))
                customerDetails.setPassword(customerDto.getPassword());

            if (customerDto.getPhoneNumber() != null && !customerDto.getPhoneNumber().equals(""))
                customerDetails.setPhoneNumber(customerDto.getPhoneNumber());

            customerDetails.setLastUpdatedAt(LocalDateTime.now());
            CustomerDetails updatedCustomer = iCustomerRepository.save(customerDetails);
            logger.info("updated customer:" + updatedCustomer);

            CustomerResponseDto updatedCustomerDto = commonUtils.customerToCustomerResponseDto(updatedCustomer);
            return updatedCustomerDto;
        } else {
            logger.info("user not found with id to update");
            throw new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND);
        }
    }

    @Override
    public CustomerFetchResponseDto fetchCustomerById(Long customerId) {
        logger.info("fetchedCustomerid:"+customerId);

        CustomerDetails customerDetails = iCustomerRepository.findBycustomerId(customerId).orElseThrow(

                () -> new ServiceException(ErrorCodes.CUSTOMER_NOT_FOUND)
        );

        logger.info("fetchedCustomer:",customerDetails);


        List<AccountDetails> accountDetailsList = iAccountRepository.findByCustomer_CustomerId(customerId);

        customerDetails.setAccountDetails(accountDetailsList);
        logger.info("account list size in customer id fetch:"+accountDetailsList.size());


        CustomerFetchResponseDto customerResponseDto =  commonUtils.customerToCustomerFetchResponseDto(customerDetails);


        return customerResponseDto;
    }

    @Override
    public List<CustomerResponseDto> fetchAllCustomers() {

        List<CustomerDetails> customerList = iCustomerRepository.findAll();

        List<CustomerResponseDto> customerDtoList = customerList.stream().map(

                customer -> modelMapper.map(customer,CustomerResponseDto.class)
        ).collect(Collectors.toList());



        return customerDtoList;

    }


}

