package com.maveric.ce.dto;

import com.maveric.ce.userenum.Gender;
import com.maveric.ce.userenum.RolesEnum;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long customerId;

    @NotBlank(message = "firstName is blank")
    private String firstName;

    @NotBlank(message = "lastName is blank")
    private String lastName;
    @NotBlank (message = "userName is blank")
    private String username;

    @NotBlank(message = "password is blank")
    private String password;


    @NotBlank(message = "email is blank")
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @NotBlank(message = "phoneNumber  is blank")
    @Size(min=10,message = "Phone number should be 10 digits")
    @Size(max = 10, message = "Phone number should be 10 digits")
    private String phoneNumber;

    // NotBlank wont work for enum
    @NotNull(message = "role is blank")
    @Enumerated(EnumType.STRING)
    private RolesEnum rolesENum;

    @NotNull(message = "gender is blank")
    @Enumerated(EnumType.STRING)
    private Gender gender;

}
