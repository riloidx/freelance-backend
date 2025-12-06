package org.matvey.freelancebackend.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.matvey.freelancebackend.ads.exception.AdNotFoundException;
import org.matvey.freelancebackend.common.exception.dto.ErrorResponse;
import org.matvey.freelancebackend.common.exception.dto.ValidationErrorResponse;
import org.matvey.freelancebackend.proposal.exception.ProposalNotFoundException;
import org.matvey.freelancebackend.roles.exception.RoleAlreadyExistsException;
import org.matvey.freelancebackend.roles.exception.RoleNotFoundException;
import org.matvey.freelancebackend.security.exception.InvalidCredentialsException;
import org.matvey.freelancebackend.users.exception.InsufficientBalanceException;
import org.matvey.freelancebackend.users.exception.UserAlreadyExistsException;
import org.matvey.freelancebackend.users.exception.UserAlreadyHasRoleException;
import org.matvey.freelancebackend.users.exception.UserDoesntHasRoleException;
import org.matvey.freelancebackend.users.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.matvey.freelancebackend.common.exception.helper.ErrorResponseHelper.buildErrorResponse;
import static org.matvey.freelancebackend.common.exception.helper.ErrorResponseHelper.buildValidationErrorResponse;

/**
 * Global exception handler for the freelance platform REST API.
 * 
 * Centralizes exception handling across all controllers, providing consistent
 * error responses and appropriate HTTP status codes. Handles both business
 * logic exceptions and validation errors with proper logging.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors from request body validation.
     * 
     * @param e the validation exception
     * @param request the HTTP request that caused the error
     * @return ResponseEntity with validation error details and 400 status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
                                                                                         HttpServletRequest request) {
        log.warn("Validation error on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildValidationErrorResponse(e, request);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ErrorResponse> handleInsufficientBalanceException(InsufficientBalanceException e,
                                                                            HttpServletRequest request) {
        log.warn("Insufficient balance error on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(AdNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAdNotFoundException(AdNotFoundException e,
                                                                   HttpServletRequest request) {
        log.warn("Ad not found on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(InvalidCredentialsException e,
                                                                           HttpServletRequest request) {
        log.warn("Invalid credentials on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.UNAUTHORIZED, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e,
                                                                     HttpServletRequest request) {
        log.warn("User not found on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e,
                                                                          HttpServletRequest request) {
        log.warn("User already exists on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.CONFLICT, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(ProposalNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProposalNotFoundException(ProposalNotFoundException e,
                                                                         HttpServletRequest request) {
        log.warn("Proposal not found on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException e,
                                                                     HttpServletRequest request) {
        log.warn("Role not found on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.NOT_FOUND, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRoleAlreadyExistsException(RoleAlreadyExistsException e,
                                                                          HttpServletRequest request) {
        log.warn("Role already exists on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.CONFLICT, request);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(UserAlreadyHasRoleException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyHasRoleException(UserAlreadyHasRoleException e,
                                                                           HttpServletRequest request) {
        log.warn("User already has role on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(UserDoesntHasRoleException.class)
    public ResponseEntity<ErrorResponse> handleUserDoesntHasRoleException(UserDoesntHasRoleException e,
                                                                          HttpServletRequest request) {
        log.warn("User doesn't have role on {}: {}", request.getRequestURI(), e.getMessage());
        var body = buildErrorResponse(e, HttpStatus.BAD_REQUEST, request);
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Handles all unhandled exceptions as a fallback.
     * 
     * @param e the unhandled exception
     * @param request the HTTP request that caused the error
     * @return ResponseEntity with generic error message and 500 status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e,
                                                         HttpServletRequest request) {
        log.error("Unhandled exception on {}: {}", request.getRequestURI(), e.getMessage(), e);
        var body = buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
