package org.daimler.error;

/**
 * @author Abhilash Ghosh
 */
public class ResourceNotFoundException extends UnknownResourceException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
