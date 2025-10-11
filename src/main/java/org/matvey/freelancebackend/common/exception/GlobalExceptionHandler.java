package org.matvey.freelancebackend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.matvey.freelancebackend.common.exception.dto.ErrorResponse;
import org.matvey.freelancebackend.common.exception.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        var body = buildValidationErrorResponse(e, request);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e,
                                                  HttpServletRequest request) {
        var body = buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);

        return ResponseEntity.badRequest().body(body);
    }

    private ErrorResponse buildErrorResponse(Exception e,
                                             HttpStatus httpStatus,
                                             HttpServletRequest request) {
        return ErrorResponse.builder()
                .timestamp(Instant.now())
                .status(httpStatus.value())
                .error(httpStatus.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .build();
    }

    private ValidationErrorResponse buildValidationErrorResponse(MethodArgumentNotValidException e,
                                                                 HttpServletRequest request) {
        return ValidationErrorResponse.validationBuilder()
                .timestamp(Instant.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message(e.getMessage())
                .path(request.getRequestURI())
                .validationErrors(getValidationErrors(e))
                .build();
    }

    private Map<String, String> getValidationErrors(MethodArgumentNotValidException e) {

        return e.getBindingResult().getFieldErrors().stream()
                .filter(fe -> fe.getDefaultMessage() != null)
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (m1, m2) -> m1));
    }
}
