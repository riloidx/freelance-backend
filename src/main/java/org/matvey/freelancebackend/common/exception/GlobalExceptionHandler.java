package org.matvey.freelancebackend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.matvey.freelancebackend.common.exception.dto.ErrorResponse;
import org.matvey.freelancebackend.common.exception.dto.ValidationErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.matvey.freelancebackend.common.exception.helper.ErrorResponseHelper.buildErrorResponse;
import static org.matvey.freelancebackend.common.exception.helper.ErrorResponseHelper.buildValidationErrorResponse;

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
}
