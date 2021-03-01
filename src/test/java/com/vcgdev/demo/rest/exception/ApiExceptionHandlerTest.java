package com.vcgdev.demo.rest.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.vcgdev.common.exception.ErrorCode;
import com.vcgdev.common.exception.ServiceException;
import com.vcgdev.common.exception.web.ApiError;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

@DisplayName("ExceptionHandling Testing")
@ExtendWith(MockitoExtension.class)
public class ApiExceptionHandlerTest {
    
    private ApiExceptionHandler exceptionHandler = new ApiExceptionHandler();

    @Mock
    private ConstraintViolationException constraintViolationException;

    @Mock
    private MethodParameter parameter;

    @Mock
    private ConstraintViolation<?> cve1;

    @Mock
    private ConstraintViolation<?> cve2;

    private MethodArgumentNotValidException exception;

    @Mock
    private HttpHeaders headers;

    @Mock
    private WebRequest request;

    @Mock
    private BindingResult result;

    @Mock
    private FieldError error1;

    @Mock
    private FieldError error2;

    @Test
    public void handleServiceException_ResponseContainsValidData() throws Exception {
        ResponseEntity<ApiError> responseError = exceptionHandler.handleServiceException(new ServiceException(ErrorCode.BAD_REQUEST));
        
        assertNotNull(responseError.getBody());

        ApiError error = responseError.getBody();
        assertEquals("400", error.getCode());
        assertEquals("Invalid parameters", error.getMessage());
        assertTrue(error.getErrors().isEmpty());
    }

    @Test
    public void handleConstraintViolation_ResponseContainsValidData() throws Exception {

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(cve1);
        violations.add(cve2);

        doReturn(violations).when(constraintViolationException).getConstraintViolations();
        doReturn("invalid-code").when(cve1).getMessage();
        doReturn("0000").when(cve2).getMessage();

        ResponseEntity<ApiError> responseError = exceptionHandler.handleConstraintViolation(constraintViolationException);
        
        assertNotNull(responseError.getBody());

        ApiError error = responseError.getBody();
        assertEquals("400", error.getCode());
        assertEquals("Invalid parameters", error.getMessage());
        assertFalse(error.getErrors().isEmpty());
    }

    @Test
    public void handleMethodArgumentNotValid_ResponseContainsValidData() throws Exception {

        exception = spy(new MethodArgumentNotValidException(parameter, result));
        
        when(result.getFieldErrors()).thenReturn(Arrays.asList(error1, error2));
        doReturn("unknow-code").when(error1).getDefaultMessage();
        doReturn("0000").when(error2).getDefaultMessage();

        ResponseEntity<Object> responseError = exceptionHandler
            .handleMethodArgumentNotValid(exception, headers, HttpStatus.BAD_REQUEST, request);

        assertNotNull(responseError.getBody());

        ApiError error = (ApiError)responseError.getBody();
        assertEquals("400", error.getCode());
        assertEquals("Invalid parameters", error.getMessage());
        assertFalse(error.getErrors().isEmpty());
    }
}
