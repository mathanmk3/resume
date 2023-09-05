package com.maveric.ce.controller;

import com.maveric.ce.dto.CustomerDto;
import com.maveric.ce.dto.CustomerFetchResponseDto;
import com.maveric.ce.dto.CustomerResponseDto;
import com.maveric.ce.dto.CustomerUpdateDto;
import com.maveric.ce.service.CustomerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController
{

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;
    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto)
    {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.OK);
    }
    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<Boolean> deleteCustomer(@PathVariable(name = "customerId") Long customerId)
    {
        return new ResponseEntity<>(customerService.deleteCustomer(customerId),HttpStatus.OK);
    }
    @PutMapping("/update/{customerId}")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@Valid @RequestBody CustomerUpdateDto customerUpdateDto,@PathVariable Long customerId)
    {
        logger.info("updateCustomer called:",customerUpdateDto);
        return new ResponseEntity<>(customerService.updateCustomer(customerUpdateDto,customerId),HttpStatus.OK);
    }

    @GetMapping("/fetch/{customerId}")
    public ResponseEntity<CustomerFetchResponseDto> findCustomerById(@PathVariable(name="customerId") Long customerId){

        return new ResponseEntity<>(customerService.fetchCustomerById(customerId),HttpStatus.OK);

    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<CustomerResponseDto>> fetchAllCustomers(){

        return new ResponseEntity<>(customerService.fetchAllCustomers(),HttpStatus.OK);
    }

}
