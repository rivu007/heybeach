package org.daimler.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daimler.entity.user.User;
import org.daimler.error.EntityAlreadyExistsException;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.UnknownResourceException;
import org.daimler.error.UserNotFoundException;
import org.daimler.security.JwtAuthenticationRequest;
import org.daimler.security.JwtAuthenticationResponse;
import org.daimler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import org.daimler.security.JwtTokenUtil;
import org.daimler.security.JwtUser;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@RequestMapping("/users")
public class UserController {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private final Log logger = LogFactory.getLog(this.getClass());

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.GET, path = "/{username}")
    @ResponseBody
    public JwtUser get(@PathVariable("username") String username) throws UserNotFoundException {
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public User registration(@Valid @RequestBody final User user) throws UserNotFoundException, EntityAlreadyExistsException, EntityPersistenceException {
        if (userService.exists(user.getUsername())) {
            throw new EntityAlreadyExistsException("User with username " + user.getUsername() + " already exists");
        }
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userService.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = PUT, path = "/{username}")
    @ResponseBody
    public User update(@PathVariable("username") String username, @RequestBody @Valid final User user)
            throws EntityPersistenceException, UnknownResourceException {

        User userToBeUpdated = userService.get(username);
        if (userToBeUpdated == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        if (userToBeUpdated != userService.getLoggedInUser()) {
            throw new AccessDeniedException("User: " + userToBeUpdated.getUsername()
                    + " doesn't have privilege to update other user: "
                    + userService.getLoggedInUser().getUsername()
            );
        }
        if (user.getId() == null) {
            user.setId(userToBeUpdated.getId());
        }
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userService.save(user);
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.DELETE, path = "/{username}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("username") String username) throws UnknownResourceException, EntityPersistenceException {
        User userToBeDeleted = userService.get(username);
        if (userToBeDeleted == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        if (userToBeDeleted != userService.getLoggedInUser()) {
            throw new AccessDeniedException("User: " + userToBeDeleted.getUsername()
                    + " doesn't have privilege to delete other user: "
                    + userService.getLoggedInUser().getUsername()
            );
        }
        userService.delete(userToBeDeleted);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AccessDeniedException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        if (userDetails == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", authenticationRequest.getUsername()));
        }
        if (!bCryptPasswordEncoder.matches(authenticationRequest.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("Password mismatch! Please verify your password");
        }
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        // Perform the security
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("authenticated user " + authenticationRequest.getUsername() + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Return the token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, path = "/me")
    @ResponseBody
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

}
