package com.maveric.ce.repository;

import com.maveric.ce.entity.CustomerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ICustomerRepository extends JpaRepository<CustomerDetails,Long> {

    Optional<CustomerDetails> findByEmail(String email);

    Optional<CustomerDetails> findBycustomerId(Long customerId);

}
