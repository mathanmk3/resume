package com.maveric.ce.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerResponseDto {



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
    //private List<AccountResponseDto> accountResponseDtoList;



}
