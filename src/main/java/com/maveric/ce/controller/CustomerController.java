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
@RequestMapping("/admin/customer")
public class CustomerController
{

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);
    @Autowired
    private CustomerService customerService;
    @PostMapping("/create")
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CustomerDto customerDto)
    {
        return new ResponseEntity<>(customerService.createCustomer(customerDto), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable(name = "id") Long customerId)
    {
        return new ResponseEntity<>(customerService.deleteCustomer(customerId),HttpStatus.OK);
    }
    @PutMapping("/update")
    public ResponseEntity<CustomerResponseDto> updateCustomer(@Valid @RequestBody CustomerUpdateDto customerUpdateDto)
    {
        logger.info("updateCustomer called:",customerUpdateDto);
        return new ResponseEntity<>(customerService.updateCustomer(customerUpdateDto),HttpStatus.OK);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerFetchResponseDto> findCustomerById(@PathVariable(name="customerId") Long customerId){

        return new ResponseEntity<>(customerService.fetchCustomerById(customerId),HttpStatus.OK);

    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<CustomerResponseDto>> fetchAllCustomers(){

        return new ResponseEntity<>(customerService.fetchAllCustomers(),HttpStatus.OK);
    }

}
