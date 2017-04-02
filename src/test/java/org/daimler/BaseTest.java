package org.daimler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.daimler.entity.user.User;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.ResourceNotFoundException;
import org.daimler.service.RoleService;
import org.daimler.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Abstract context configuration.
 *
 * @author abhilash.ghosh
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        webEnvironment = RANDOM_PORT,
        classes = HeyBeachTests.class
)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public abstract class BaseTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper mapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    public String jsonOf(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }

    public Map<String, Object> createClaims(String username) {
        Map<String, Object> claims = new HashMap();
        claims.put("sub", username);
        claims.put("audience", "testAudience");
        claims.put("created", LocalDateTime.now());
        return claims;
    }

    public User createTestUser(String username) throws ResourceNotFoundException, EntityPersistenceException {
        Role buyerRole = roleService.get(RoleName.ROLE_BUYER);

        User user = new User();
        user.setUsername(username);
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setEmailAddress("abc@example.com");
        user.setFirstname("test");
        user.setLastname("user");
        user.setEnabled(true);
        user.setRoles(Collections.singleton(buyerRole));
        return userService.save(user);
    }

}
