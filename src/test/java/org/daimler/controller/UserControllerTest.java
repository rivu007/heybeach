package org.daimler.controller;

import org.daimler.BaseTest;
import org.daimler.security.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
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
                post("/contents")
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
}
