package org.daimler.controller;

import org.daimler.BaseTest;
import org.daimler.entity.user.User;
import org.daimler.repository.UserDAO;
import org.daimler.security.JwtAuthenticationRequest;
import org.daimler.security.JwtTokenUtil;
import org.daimler.service.RoleService;
import org.daimler.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link UserController}
 *
 * @author abhilash.ghosh
 */
public class UserControllerTest extends BaseTest {

    @Value("${jwt.header}")
    private String JWTHeader;

    @Value("${jwt.expiration}")
    private long JWTExpiryInterval;

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Before
    public void init() {
        ReflectionTestUtils.setField(jwtTokenUtil, "expiration", JWTExpiryInterval);
        ReflectionTestUtils.setField(jwtTokenUtil, "secret", "mySecret");
    }
    /**
     * Clean up the table once the tests are done.
     */
    @After
    public void cleanup() {
        userDAO.deleteAll();
    }

    @Test
    public void registration_createUserWithNoRequestBody_badRequest() throws Exception {
        mvc.perform(
                post("/users")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(
                        jsonPath("$.message", startsWith("Required request body is missing:"))
                )
        ;
    }

    @Test
    public void registration_CreateUserWithoutJSON_badRequest() throws Exception {
        mvc.perform(
                post("/users")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content("some invalid TEXT data")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(
                        jsonPath("$.message").value(startsWith(
                                "Could not read document: " +
                                        "Unrecognized token 'some': was expecting ('true', 'false' or 'null')"
                        ))
                )
        ;
    }

    @Test
    public void registration_createUserWithoutInvalidArguments_badRequest() throws Exception {
        mvc.perform(
                post("/users")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content("{\"invalid\":\"data\"}")
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", startsWith("Validation failed")))
                .andExpect(jsonPath("$.details", hasSize(6)))
                .andExpect(
                        jsonPath("$.details", allOf(
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "enabled"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                )),
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "password"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                )),
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "username"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                )),
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "firstname"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                )),
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "lastname"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                )),
                                hasItem(allOf(
                                        hasEntry("objectName", "user"),
                                        hasEntry("field", "emailAddress"),
                                        hasEntry("rejectedValue", null),
                                        hasEntry("message", "may not be null")
                                ))
                        ))
                )
        ;
    }

    @Test
    @Ignore
    public void registration_alreadyExists_conflict() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        userDAO.save(user);

        mvc.perform(
                post("/users")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonOf(user))
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is("CONFLICT")))
                .andExpect(jsonPath("$.message", is("User with username " + user.getUsername() + " already exists")))
        ;
    }

    @Test
    public void registration_validData_createNewUser() throws Exception {
        User newuser = new User();
        newuser.setUsername("test");
        newuser.setPassword("password");
        newuser.setEmailAddress("abc@example.com");
        newuser.setFirstname("test");
        newuser.setLastname("user");
        newuser.setEnabled(true);

        MvcResult result = mvc.perform(
                post("/users")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonOf(newuser))
        )
                .andExpect(status().isCreated())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        User createdUser = mapper.readValue(response, User.class);

        assertEquals(newuser.getUsername(), createdUser.getUsername());
        assertEquals(newuser.getEmailAddress(), createdUser.getEmailAddress());
    }

    @Test
    public void login_noRequestBody_badRequest() throws Exception {

        mvc.perform(
                post("/users/login")
        )
                .andExpect(status().isBadRequest());
    }

    @Test
    @Ignore
    public void login_wrongPassword_badRequest() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        userDAO.save(user);

        JwtAuthenticationRequest requestWithWrongPassword = new JwtAuthenticationRequest();
        requestWithWrongPassword.setUsername("test");
        requestWithWrongPassword.setPassword("wrong-password");

        mvc.perform(
                post("/users/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonOf(requestWithWrongPassword))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("Password mismatch! Please verify your password")));
    }

    @Test
    public void login_validData_success() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        userService.save(user);

        JwtAuthenticationRequest request = new JwtAuthenticationRequest();
        request.setUsername("test");
        request.setPassword("password");

        mvc.perform(
                post("/users/login")
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonOf(request))
        )
                .andExpect(status().isOk());
    }

    @Test
    public void getAuthenticatedUser_noJWTToken_forbiddenAccess() throws Exception {

        mvc.perform(
                get("/users/me")
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void getAuthenticatedUser_withValidJWTToken_success() throws Exception {
        User user = createTestUser("test-user");

        final Map<String, Object> claims = createClaims(user.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        MvcResult result = mvc.perform(
                get("/users/me")
                .header(JWTHeader, JWTToken)
        )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        User returnedUser = mapper.readValue(response, User.class);

        assertEquals(user.getUsername(), returnedUser.getUsername());
        assertEquals(user.getFirstname(), returnedUser.getFirstname());
    }

    @Test
    public void update_userTryingToUpdateAnotherUserThatDoesnotExists_badRequest() throws Exception {
        User user = createTestUser("user1");

        final Map<String, Object> claims = createClaims(user.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        mvc.perform(
                put("/users/{username}", user.getUsername())
                        .contentType(APPLICATION_JSON_UTF8)
                        .header(JWTHeader, JWTToken)
                        .content(jsonOf(new User()))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is("BAD_REQUEST")))
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }

    @Test
    public void update_oneUserTryingToUpdateAnotherUser_forbidden() throws Exception {
        User user1 = createTestUser("user1");

        User toBeUpdatedUser = new User();
        toBeUpdatedUser.setUsername("updated-user");
        toBeUpdatedUser.setPassword(bCryptPasswordEncoder.encode("password"));
        toBeUpdatedUser.setEmailAddress("abc@example.com");
        toBeUpdatedUser.setFirstname("test");
        toBeUpdatedUser.setLastname("user");
        toBeUpdatedUser.setEnabled(true);
        userService.save(toBeUpdatedUser);

        final Map<String, Object> claims = createClaims(user1.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        mvc.perform(
                put("/users/{username}", toBeUpdatedUser.getUsername())
                        .contentType(APPLICATION_JSON_UTF8)
                        .header(JWTHeader, JWTToken)
                        .content(jsonOf(toBeUpdatedUser))
        )
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status", is("FORBIDDEN")))
                .andExpect(jsonPath("$.message", is("User: " + toBeUpdatedUser.getUsername() + " doesn't have privilege to update other user: " + user1.getUsername())));
    }

    @Test
    @Ignore
    public void update_validData_success() throws Exception {
        User user = createTestUser("user");

        assertEquals("test", user.getFirstname());
        assertEquals("abc@example.com", user.getEmailAddress());

        User toBeUpdatedUser = new User();
        toBeUpdatedUser.setId(user.getId());
        toBeUpdatedUser.setUsername(user.getUsername());
        toBeUpdatedUser.setPassword(bCryptPasswordEncoder.encode("password"));
        toBeUpdatedUser.setEmailAddress("emailUpdated@example.com");
        toBeUpdatedUser.setFirstname("firstname-updated");
        toBeUpdatedUser.setLastname("lastname-updated");
        toBeUpdatedUser.setEnabled(true);

        final Map<String, Object> claims = createClaims(user.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        MvcResult result = mvc.perform(
                put("/users/{username}", user.getUsername())
                        .contentType(APPLICATION_JSON_UTF8)
                        .header(JWTHeader, JWTToken)
                        .content(jsonOf(toBeUpdatedUser))
        )
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        User updatedUser = mapper.readValue(response, User.class);

        assertEquals("firstname-updated", updatedUser.getFirstname());
        assertEquals("emailUpdated@example.com", updatedUser.getEmailAddress());
    }

    @Test
    public void delete_userTryingToDeleteAnotherUserThatDoesnotExists_UsernameNotFoundException() throws Exception {
        User user1 = createTestUser("user1");

        final Map<String, Object> claims = createClaims(user1.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        mvc.perform(
                delete("/users/{username}", "non-existence-user")
                        .header(JWTHeader, JWTToken)
        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void delete_oneUserTryingToDeleteAnotherUser_forbidden() throws Exception {
        User user1 = createTestUser("user1");
        User user2 = createTestUser("user2");

        final Map<String, Object> claims = createClaims(user1.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        mvc.perform(
                delete("/users/{username}", user2.getUsername())
                        .header(JWTHeader, JWTToken)
        )
                .andExpect(status().isForbidden());
    }

    @Test
    public void delete_withValidData_success() throws Exception {
        User userToBeDeleted = createTestUser("user1");

        final Map<String, Object> claims = createClaims(userToBeDeleted.getUsername());
        String JWTToken = jwtTokenUtil.generateToken(claims);

        assertTrue(userService.exists(userToBeDeleted.getUsername()));

        mvc.perform(
                delete("/users/{username}", userToBeDeleted.getUsername())
                        .header(JWTHeader, JWTToken)
        )
                .andExpect(status().isNoContent());

        assertFalse(userService.exists(userToBeDeleted.getUsername()));
    }
}
