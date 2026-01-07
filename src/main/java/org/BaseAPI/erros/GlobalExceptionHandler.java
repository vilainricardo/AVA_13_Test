package org.BaseAPI.erros;

import org.BaseAPI.erros.exceptions.ExpiredSessionException;
import org.BaseAPI.erros.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {


//    @Autowired
//    private MessageSource messageSource;
//
//    private String i18n(String code) {
//        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
//    }


    @ExceptionHandler(ExpiredSessionException.class)
    public ResponseEntity<ApiError> handleExpiredSession(ExpiredSessionException ex) {
        ApiError err = base(HttpStatus.UNAUTHORIZED, "Authorization Expired.", ex);
        err.setDetail(safeMessage(ex));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiError err = base(HttpStatus.NOT_FOUND, "Resource not found", ex);
        err.setDetail(safeMessage(ex));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }


    // Validation (DTO @Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        ApiError err = base(HttpStatus.UNPROCESSABLE_ENTITY, "Validation failed", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // Validation (query/path params)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException ex) {
        ApiError err = base(HttpStatus.UNPROCESSABLE_ENTITY, "Constraint violation", ex);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
    }

    // Missing parameters, bad types, 404 routes
    @ExceptionHandler({
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex) {
        ApiError err = base(HttpStatus.BAD_REQUEST, "Bad request", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandler(NoHandlerFoundException ex) {
        ApiError err = base(HttpStatus.NOT_FOUND, "Endpoint not found", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
    }

    // Fallback — catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnhandled(Exception ex) {
        // Log full details server-side (stack trace)
        // Logger.error("Unhandled exception", ex);
        ApiError err = base(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error", ex);
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
