package org.daimler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Abstract context configuration.
 *
 * @author abhilash.ghosh
 */
@RunWith(SpringRunner.class)
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

    public String jsonOf(Object value) throws JsonProcessingException {
        return mapper.writeValueAsString(value);
    }


}
