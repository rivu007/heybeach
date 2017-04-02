package org.daimler.controller;

import org.daimler.BaseTest;
import org.daimler.entity.user.User;
import org.daimler.security.JwtAuthenticationRequest;
import org.daimler.security.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link UserController}
 *
 * @author abhilash.ghosh
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class UserControllerTest extends BaseTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * Clean up the table once the tests are done.
     */
    @After
    public void cleanup() {
        userRepository.deleteAll();
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
    public void registration_alreadyExists_conflict() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        userRepository.save(user);

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
    public void login_wrongPassword_badRequest() throws Exception {
        User user = new User();
        user.setUsername("test");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        userRepository.save(user);

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
        userRepository.save(user);

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
}
