package com.maveric.ce.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maveric.ce.dto.ErrorDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
class SerurityExceptionHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException exc) throws IOException {
        System.out.println("================================");
        System.out.println(request.getRequestURL());
        System.out.println(request.getRequestURI());

        System.out.println("================================");

        response.setStatus(403);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorDto errors = new ErrorDto();
        errors.setErrorMessgae("access_denied_reason");
        errors.setErrorCode("403");
        errors.setHttpStatus(403);
        OutputStream os = response.getOutputStream();
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(os,errors);
        os.flush();
    }
}