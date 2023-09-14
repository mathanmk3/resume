package com.maveric.ce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.ce.dto.ErrorDto;
import com.maveric.ce.exceptions.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.OutputStream;


@Component
public class NoResourceHandler extends Http403ForbiddenEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException arg)
            throws IOException {
        try {
        response.setStatus(404);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorDto errors = new ErrorDto();
        errors.setErrorMessgae(ErrorCodes.PAGE_NOT_FOUND);
        errors.setErrorCode("404");
        errors.setHttpStatus(404);
        OutputStream os =response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(os,errors);
        os.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
