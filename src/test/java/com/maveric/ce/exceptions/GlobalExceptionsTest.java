package com.maveric.ce.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.maveric.ce.dto.ErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import java.lang.reflect.Executable;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.validator.internal.engine.path.PathImpl;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;

@ContextConfiguration(classes = {GlobalExceptions.class, ErrorCodes.class})
@ExtendWith(SpringExtension.class)
class GlobalExceptionsTest {
    @Autowired
    private GlobalExceptions globalExceptions;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Test
     void testHandleServiceExceptionWithErrorCode() {
        ServiceException ex = new ServiceException("Error Message##400");
        ResponseEntity<ErrorDto> response = globalExceptions.handleServiceException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("Error Message", errorDto.getErrorMessgae());
        assertEquals("400", errorDto.getErrorCode());
    }
    @Test
     void testHandleServiceExceptionWithoutErrorCode() {
        ServiceException ex = new ServiceException("Error Message");
        ResponseEntity<ErrorDto> response = globalExceptions.handleServiceException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("Error Message", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }

    @Test
     void testHandleSqlExceptionWithErrorCode() {
        SQLExceptions ex = new SQLExceptions("SQL Error##400");

        ResponseEntity<ErrorDto> response = globalExceptions.handleSqlException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("SQL Error", errorDto.getErrorMessgae());
        assertEquals("400", errorDto.getErrorCode());
    }

    @Test
     void testHandleSqlExceptionWithoutErrorCode() {
        SQLExceptions ex = new SQLExceptions("SQL Error");
        ResponseEntity<ErrorDto> response = globalExceptions.handleSqlException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("INTERNAL_SERVER_ERROR", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }

    @Test
     void testHandleConstraintViolationException() {
        ConstraintViolationException exception = mock(ConstraintViolationException.class);
        ConstraintViolation<?> constraintViolation = mock(ConstraintViolation.class);
        when(constraintViolation.getMessage()).thenReturn("Constraint violation error");
        when(exception.getConstraintViolations()).thenReturn(Set.of(constraintViolation));
        ResponseEntity<ErrorDto> response = globalExceptions.handle(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("Constraint violation error", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }

        @Test
         void testHandleBadRequestExceptionstest() {
            MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
            FieldError fieldError = mock(FieldError.class);
            when(fieldError.getField()).thenReturn("fieldName");
            when(fieldError.getDefaultMessage()).thenReturn("Error Message");
            when(ex.getFieldError()).thenReturn(fieldError);
            BindingResult bindingResult = mock(BindingResult.class);
            ObjectError error1 = new ObjectError("objectName", "PNR Should not be Null");
            ObjectError error2 = new ObjectError("objectName", "PNR Should not be Blank");
            when(bindingResult.getAllErrors()).thenReturn(List.of(error1, error2));
            when(ex.getBindingResult()).thenReturn(bindingResult);
            ResponseEntity<ErrorDto> response = globalExceptions.handleBadRequestExceptions(ex);
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            ErrorDto errorDto = response.getBody();
            assertNotNull(errorDto);
            assertEquals("PNR Should not be Null,PNR Should not be Blank", errorDto.getErrorMessgae().substring(1));
        }


    @Test
    public void testHandleDefaultExceptionInvalidCurrencyType() {
        // Arrange
        String invalidCurrencyTypeMessage = "Invalid message containing CurrencyType";
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(invalidCurrencyTypeMessage);

        // Act
        ResponseEntity<ErrorDto> response = globalExceptions.handleDefaultException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("INVALID_CurrencyType", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }


    @Test
    public void testHandleDefaultExceptionInvalidRole() {
        // Arrange
        String invalidRoleMessage = "Invalid message containing Role";
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(invalidRoleMessage);

        // Act
        ResponseEntity<ErrorDto> response = globalExceptions.handleDefaultException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("INVALID_Role", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }

    @Test
    public void testHandleDefaultExceptionInvalidGender() {
        // Arrange
        String invalidGenderMessage = "Invalid message containing Gender";
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(invalidGenderMessage);

        // Act
        ResponseEntity<ErrorDto> response = globalExceptions.handleDefaultException(ex);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ErrorDto errorDto = response.getBody();
        assertNotNull(errorDto);
        assertEquals("INVALID_GENDER", errorDto.getErrorMessgae());
        assertEquals("500", errorDto.getErrorCode());
    }



}

