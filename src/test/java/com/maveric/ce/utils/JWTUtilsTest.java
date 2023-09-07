package com.maveric.ce.utils;

import com.maveric.ce.dto.UserDetailsImpl;
import com.maveric.ce.exceptions.ServiceException;
import com.maveric.ce.response.LoginResponse;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
class JWTUtilsTest {
    @Autowired
    private LoginResponse loginResponse;

    @Autowired
    private JWTUtils jWTUtils;





}

