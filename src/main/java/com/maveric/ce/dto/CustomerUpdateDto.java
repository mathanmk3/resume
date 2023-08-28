package com.maveric.ce.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerUpdateDto {

    /*@NotNull
    private Long customerId;*/

    private String username;

    private String password;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    @Size(min=10,message = "Phone number should be 10 digits")
    @Size(max = 10, message = "Phone number should be 10 digits")
    private String phoneNumber;


}
