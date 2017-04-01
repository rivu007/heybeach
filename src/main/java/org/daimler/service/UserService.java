package org.daimler.service;

import org.daimler.entity.User;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UserNotFoundException;

import java.util.Optional;
import java.util.Set;

/**
 * @author abhilash.ghosh
 */
public interface UserService {

    /**
     * Creates a new user from the provided data
     *
     * @param user to save
     * @throws EntityPersistenceException
     */
    User save(User user) throws EntityPersistenceException;

    /**
     * Checks if user exists or not
     *
     * @param username to search for
     * @return user with given username
     * @throws UserNotFoundException
     */
    User exists(String username) throws UserNotFoundException;

    /**
     * Delete a user by username
     *
     * @param userToBeDeleted
     * @throws EntityPersistenceException
     */
    void delete(User userToBeDeleted) throws EntityPersistenceException;

    /**
     * Checks if the logged-in use is admin or not
     */
    boolean isAdminUser();

}
