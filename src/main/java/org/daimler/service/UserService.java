package org.daimler.service;

import org.daimler.entity.user.User;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UserNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author abhilash.ghosh
 */

public interface UserService {

    /**
     * Creates a new user from the provided data
     *
     * @param user to save
     * @return persisted user object
     * @throws EntityPersistenceException
     */
    User save(User user) throws EntityPersistenceException;

    /**
     * update the user entity
     *
     * @param user to update
     * @return persisted user object
     * @throws EntityPersistenceException
     */
    User update(User user) throws EntityPersistenceException;

    /**
     * Checks if user exists or not
     *
     * @param username to search for
     * @return User with given username
     * @throws UserNotFoundException
     */
    User get(String username) throws UserNotFoundException;

    /**
     * Checks if user exists or not
     *
     * @param username to search for
     * @return check if with given username exists
     */
    boolean exists(String username);

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

    /**
     * Retries the authenticated user
     *
     * @return Authenticated User
     * @throws UserNotFoundException
     */
    User getLoggedInUser() throws UserNotFoundException;

}
