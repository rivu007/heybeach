package org.daimler.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.daimler.entity.User;
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
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
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

    @RequestMapping(method = RequestMethod.GET, path = "/{username}")
    @Secured({"ROLE_ADMIN"})
    @ResponseBody
    public JwtUser get(@PathVariable("username") String username) throws UserNotFoundException {
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(CREATED)
    public User registration(@Valid @RequestBody final User user) throws EntityPersistenceException {
        String password = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(password);
        return userService.save(user);
    }

    @RequestMapping(method = PUT)
    @Secured({"ROLE_ADMIN","ROLE_SELLER","ROLE_BUYER"})
    @ResponseBody
    public User update(@SuppressWarnings("UnusedParameters") HttpServletRequest request, @RequestBody @Valid final User user)
            throws EntityPersistenceException, UnknownResourceException {

        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);

        if (!userService.isAdminUser() && !username.equals(user.getUsername())) {
            throw new UnknownResourceException("OperationNotAllowed for user:"+ username +". Only admin user have privileges to modify other user profile");
        }

        return userService.save(user);
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{username}")
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("username") String username) throws UnknownResourceException, EntityPersistenceException {
        User userToBeDeleted = userService.exists(username);

        if(userToBeDeleted == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        userService.delete(userToBeDeleted);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<?> login(HttpServletRequest request, @RequestBody JwtAuthenticationRequest authenticationRequest, Device device) throws AuthenticationException {
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

    @RequestMapping(method = RequestMethod.GET, path = "/me")
    @Secured({"ROLE_ADMIN","ROLE_SELLER","ROLE_BUYER"})
    @ResponseBody
    public JwtUser getAuthenticatedUser(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        return (JwtUser) userDetailsService.loadUserByUsername(username);
    }

}
