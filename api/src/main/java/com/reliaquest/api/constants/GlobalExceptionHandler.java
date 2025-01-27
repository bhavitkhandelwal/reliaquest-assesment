package com.reliaquest.api.constants;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @Autowired
    private ObjectMapper mapper;

    @ExceptionHandler(HttpClientErrorException.BadRequest.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(HttpClientErrorException.BadRequest exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                "Bad Request");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(HttpClientErrorException.Forbidden exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                "Forbidden");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(HttpClientErrorException.NotFound exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                "Not Found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                exceptionx.getStatusText());
        return ResponseEntity.status(exceptionx.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(HttpServerErrorException.InternalServerError exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                "Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.ServiceUnavailable.class)
    public ResponseEntity<ErrorResponse> handleServiceUnavailableException(HttpServerErrorException.ServiceUnavailable exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                "Service Unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerErrorException(HttpServerErrorException exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getResponseBodyAsString(),
                exceptionx.getStatusText());
        return ResponseEntity.status(exceptionx.getStatusCode()).body(errorResponse);
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleResourceAccessException(ResourceAccessException exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getMessage(),
                "Service is unavailable");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<ErrorResponse> handleRestClientException(RestClientException exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getMessage(),
                "Some Client Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception exceptionx) {
        ErrorResponse errorResponse = parseErrorResponse(exceptionx.getMessage(),
                "Some Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private ErrorResponse parseErrorResponse(String responseBodyAsString, String status) {
        ErrorResponse errorResponse;
        try {
            errorResponse = mapper.readValue(responseBodyAsString, ErrorResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return errorResponse;
    }
}
