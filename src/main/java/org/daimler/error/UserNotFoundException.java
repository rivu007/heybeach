package org.daimler.error;

/**
 * @author Abhilash Ghosh
 */
public class UserNotFoundException extends UnknownResourceException {

    public UserNotFoundException(String username) {
        super("User with ID \"" + username + "\" not found");
    }
}
