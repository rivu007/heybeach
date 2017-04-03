package org.daimler.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Abhilash Ghosh
 */

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MediaUploadException extends Exception {

    public MediaUploadException(String message, Throwable t) {
        super(message, t);
    }

    public MediaUploadException(String message) {
        super(message);
    }
}
