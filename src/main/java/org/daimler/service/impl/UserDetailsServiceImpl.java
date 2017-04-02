package org.daimler.service.impl;

import org.daimler.entity.user.User;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UserNotFoundException;
import org.daimler.repository.UserDAO;
import org.daimler.security.JwtUserFactory;
import org.daimler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserDAO userDAO;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
        return JwtUserFactory.create(user);
    }

    @Override
    public User save(User user) throws EntityPersistenceException {
        return userDAO.save(user);
    }

    @Override
    public User update(User user) throws EntityPersistenceException {
        return userDAO.update(user);
    }

    @Override
    public boolean exists(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    @Override
    public User get(String username) throws UserNotFoundException {
        return userDAO.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
    }

    @Override
    public void delete(User userToBeDeleted) throws EntityPersistenceException {
        userDAO.delete(userToBeDeleted);
    }

    @Override
    public boolean isAdminUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }

    @Override
    public User getLoggedInUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return get(authentication.getName());
    }
}
