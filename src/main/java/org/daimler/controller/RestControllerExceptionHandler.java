package org.daimler.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daimler.error.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.util.stream.Collectors;

/**
 * @author abhilash.ghosh
 */
@ControllerAdvice
@SuppressWarnings("unused")
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Log LOG = LogFactory.getLog(RestControllerExceptionHandler.class);

    @ExceptionHandler({UsernameNotFoundException.class,
                       UserNotFoundException.class,
                       OrderNotFoundException.class,
                       ItemNotFoundException.class,
                       UnknownResourceException.class})
    public ResponseEntity<Object> handleContentNotFound(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleUnauthorizedContent(Exception ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleInternalServerException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({EntityPersistenceException.class})
    public ResponseEntity<Object> handleEntityPersistenceProcessing(EntityPersistenceException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestError error = new RestError(status, "Error occurred while saving the entity", ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        status = HttpStatus.BAD_REQUEST;

        RestError error = new RestError(status, "Error occurred",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::new)
                        .collect(Collectors.toList())
        );

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    @Override
    protected final ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                                   Object body,
                                                                   HttpHeaders headers,
                                                                   HttpStatus status,
                                                                   WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        RestError error = bodyToError(body, status, ex);
        LOG.error(ex.getMessage(), ex);

        // this is single entry point to ensure that error response always contains our rest error
        return new ResponseEntity<>(error, headers, status);
    }

    protected RestError bodyToError(Object body, HttpStatus status, Exception ex) {
        if (body instanceof RestError) {
            return (RestError) body;
        }

        return new RestError(status, ex.getMessage());
    }

    /*@ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleExceptionInternal(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

         error = new RestError(status, ex.getMessage());
        RestErrorreturn handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({EntityPersistenceException.class})
    public ResponseEntity<Object> handleEntityPersistenceProcessing(EntityPersistenceException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestError error = new RestError(status, "Validation failed", ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler({JsonEntityProcessingException.class})
    public ResponseEntity<Object> handleJsonEntityProcessing(JsonEntityProcessingException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        RestError error = new RestError(status, "Validation failed",
                stream(ex.getMessages().spliterator(), false)
                        .map(FieldError::new)
                        .collect(Collectors.toList())
        );

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(DataMismatchException.class)
    public ResponseEntity<Object> handleIdMismatch(DataMismatchException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        Map<String, Object> details = new HashMap<>();
        details.put("expected", ex.getExpected());
        details.put("given", ex.getGiven());

        RestError error = new RestError(status, ex.getMessage(), details);
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        status = HttpStatus.BAD_REQUEST;

        RestError error = new RestError(status, "Validation failed",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(FieldError::new)
                        .collect(Collectors.toList())
        );

        return handleExceptionInternal(ex, error, headers, status, request);
    }

    */
}
