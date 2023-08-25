package com.maveric.ce.dto;

import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CustomerDto {

    private Long customerId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
    @NotBlank
    private String username;

    @NotBlank
    private String password;


    @NotBlank
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotBlank
    @Size(min=10,message = "Phone number should be 10 digits")
    @Size(max = 10, message = "Phone number should be 10 digits")
    private String phoneNumber;

    // NotBlank wont work for enum
    @NotNull
    @Enumerated(EnumType.STRING)
    private RolesEnum rolesENum;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
