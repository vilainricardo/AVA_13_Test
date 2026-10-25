package org.BaseAPI.erros;

import lombok.extern.slf4j.Slf4j;
import org.BaseAPI.erros.exceptions.ExpiredSessionException;
import org.BaseAPI.erros.exceptions.ResourceNotFoundException;
import org.BaseAPI.erros.exceptions.WriteFileException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity<ApiError> handleExpiredSession(ExpiredSessionException ex) {
        ApiError err = base(HttpStatus.UNAUTHORIZED, "Authorization Expired.", ex);
        err.setDetail(safeMessage(ex));
        log.warn("401 Unauthorized - {} - detail='{}'", ex.getClass().getSimpleName(), err.getDetail());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError err = base(HttpStatus.NOT_FOUND, "Resource not found", ex);
        err.setDetail(safeMessage(ex));
        log.info("404 Not Found - {} - detail='{}'", ex.getClass().getSimpleName(), err.getDetail());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }


    // Validation (DTO @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        ApiError err = base(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", ex);

        log.warn("422 Unprocessable Entity - {} - violations: {}", ex.getClass().getSimpleName(),
                ex.getBindingResult().getErrorCount());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // Validation (DTO @Valid)
    @ExceptionHandler(WriteFileException.class)
    public ResponseEntity<ApiError> handleValidation(WriteFileException ex) {
        ApiError err = base(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        log.warn("422 WriteFileException - detail='{}'", err.getDetail(), ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // Validation (query/path params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError err = base(HttpStatus.UNPROCESSABLE_ENTITY, "Constraint violation", ex);
        log.warn("422 ConstraintViolation - violations: {}", ex.getConstraintViolations().size());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // Missing parameters, bad types, 404 routes
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
        ApiError err = base(HttpStatus.BAD_REQUEST, "Bad request", ex);
        log.warn("400 Bad Request - {} - detail='{}'", ex.getClass().getSimpleName(), safeMessage(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandler(NoHandlerFoundException ex) {
        ApiError err = base(HttpStatus.NOT_FOUND, "Endpoint not found", ex);
        log.info("404 NoHandlerFound - method={} uri={}", ex.getHttpMethod(), ex.getRequestURL());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // Fallback — catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandled(Exception ex) {
        ApiError err = base(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex);
        log.error("500 Internal Server Error - {} - message='{}'", ex.getClass().getName(), ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }

    // Helper to build base error
    private ApiError base(HttpStatus status, String title, Exception ex) {
        ApiError err = new ApiError();
        err.setStatus(status.value());
        err.setTitle(title);
        err.setDetail(null); // set per-handler
        err.setType("about:blank"); // or a URI you control for documentation
        return err;
    }

    private String safeMessage(Exception ex) {
        // Convert internal messages to safe summaries, avoid leaking DB details, etc.
        return ex.getMessage();
    }

}
