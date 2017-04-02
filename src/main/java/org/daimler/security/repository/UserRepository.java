package org.daimler.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.daimler.entity.user.User;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * User repository.
 * @author Abhilash Ghosh
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
