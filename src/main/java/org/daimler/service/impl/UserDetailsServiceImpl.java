package org.daimler.service.impl;

import org.daimler.entity.user.User;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UserNotFoundException;
import org.daimler.security.JwtUserFactory;
import org.daimler.security.repository.UserRepository;
import org.daimler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
        return JwtUserFactory.create(user);
    }

    @Override
    public User save(User user) throws EntityPersistenceException {
        return userRepository.save(user);
    }

    @Override
    public boolean exists(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public User get(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException(String.format("No user found with username '%s'.", username)));
    }

    @Override
    public void delete(User userToBeDeleted) throws EntityPersistenceException {
        userRepository.delete(userToBeDeleted);
    }

    @Override
    public boolean isAdminUser() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
    }
}
