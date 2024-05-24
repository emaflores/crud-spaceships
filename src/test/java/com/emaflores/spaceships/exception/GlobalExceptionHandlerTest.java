package com.emaflores.spaceships.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;


import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleDuplicateSpaceshipException() {
        DuplicateSpaceshipException ex = new DuplicateSpaceshipException("Duplicate spaceship");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDuplicateSpaceshipException(ex);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate spaceship", response.getBody().getMessage());
    }

    @Test
    public void testHandleInvalidIdException() {
        InvalidIdException ex = new InvalidIdException("Invalid ID");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleInvalidIdException(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid ID", response.getBody().getMessage());
    }

    @Test
    public void testHandleNoResourceFoundException() {
        NoResourceFoundException ex = mock(NoResourceFoundException.class);
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleNoResourceFoundException(ex);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("The requested URL was not found on this server.", response.getBody().getMessage());
    }

    @Test
    public void testHandleValidationExceptions() {
        BindingResult bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        FieldError fieldError = new FieldError("spaceship", "name", "must not be blank");
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("must not be blank", response.getBody().get("name"));
    }

    @Test
    public void testHandleDataIntegrityViolationExceptionNameField() {
        Throwable rootCause = new Throwable("La columna \"NAME\" no permite valores nulos");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation", rootCause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The name field cannot be null. Please provide a valid name.", response.getBody().getMessage());
    }

    @Test
    public void testHandleDataIntegrityViolationExceptionTypeField() {
        Throwable rootCause = new Throwable("La columna \"TYPE\" no permite valores nulos");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation", rootCause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The type field cannot be null. Please provide a valid type.", response.getBody().getMessage());
    }

    @Test
    public void testHandleDataIntegrityViolationExceptionSourceField() {
        Throwable rootCause = new Throwable("La columna \"SOURCE\" no permite valores nulos");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation", rootCause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The source field cannot be null. Please provide a valid source.", response.getBody().getMessage());
    }

    @Test
    public void testHandleDataIntegrityViolationExceptionGeneric() {
        Throwable rootCause = new Throwable("Some generic data integrity violation");
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation", rootCause);

        ResponseEntity<ErrorResponse> response = exceptionHandler.handleDataIntegrityViolationException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data integrity violation: Some generic data integrity violation", response.getBody().getMessage());
    }

    @Test
    public void testHandleGenericException() {
        Exception ex = new Exception("Generic error");
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleGenericException(ex);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred.", response.getBody().getMessage());
    }
}
