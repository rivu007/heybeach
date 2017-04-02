package org.daimler.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daimler.error.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
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

    /**
     * Handles the following exceptions:
     *
     * @see UsernameNotFoundException
     * @see UserNotFoundException
     * @see OrderNotFoundException
     * @see ItemNotFoundException
     * @see UnknownResourceException
     */
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

    /**
     * Handles the following exceptions:
     *
     * @see AuthenticationException
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleUnauthorizedContent(AuthenticationException ex, WebRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handles the following exceptions:
     *
     * @see AccessDeniedException
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleRestrictedContent(AccessDeniedException ex, WebRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * Handles the following exceptions:
     *
     * @see EntityPersistenceException
     */
    @ExceptionHandler({EntityPersistenceException.class})
    public ResponseEntity<Object> handleEntityPersistenceProcessing(EntityPersistenceException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        RestError error = new RestError(status, "Error occurred while saving the entity", ex.getMessage());

        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * This is generic method which handles:
     *
     * @see EntityAlreadyExistsException
     */
    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<Object> handleEntityAlreadyExistsException(EntityAlreadyExistsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * This is generic method which handles:
     *
     * @see EntityAlreadyExistsException
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialException(BadCredentialsException ex, WebRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        RestError error = new RestError(status, ex.getMessage());
        return handleExceptionInternal(ex, error, new HttpHeaders(), status, request);
    }

    /**
     * This is generic method which handles:
     *
     * @see RuntimeException
     */
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<Object> handleInternalServerException(RuntimeException ex, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        RestError error = new RestError(status, ex.getMessage());
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
}
