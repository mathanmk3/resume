
package com.maveric.ce.controller;


import com.maveric.ce.dto.LoginDto;
import com.maveric.ce.response.LoginResponse;
import com.maveric.ce.serviceImpl.LoginServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    LoginServiceImpl loginImpl;


    @PostMapping("/authenticate/login")
    public ResponseEntity<LoginResponse> createCustomer(@Valid @RequestBody LoginDto dto, HttpServletRequest request,
                                                        HttpServletResponse response) {
        LoginResponse userToken = loginImpl.findUserByMailId(dto, request);
        return new ResponseEntity<>(userToken, HttpStatus.OK);
    }

}
 