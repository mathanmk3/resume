package com.maveric.ce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
public class CustomerFetchResponseDto {



    private Long customerId;

    private String firstName;

    private String lastName;
    private String username;
    private String password;


    private String email;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUpdatedAt;

    private String phoneNumber;

    private RolesEnum rolesENum;

    private Gender gender;

    // Test for fetching account for customer.
    private List<AccountResponseDto> accountResponseDtoList;



}
