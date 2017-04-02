package org.daimler;

import org.junit.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Simple test to perform health check and appInfo of the application
 *
 * @author abhilash.ghosh
 */
@SuppressWarnings("SpringJavaAutowiredMembersInspection")
public class SanityTest extends BaseTest {

    @Test
    public void healthCheck_exists_true() throws Exception {
        mvc.perform(get("/app/health"))
                .andExpect(status().isOk());
    }

    @Test
    public void appInfo_exists_true() throws Exception {
        mvc.perform(get("/app/info"))
                .andExpect(status().isOk());
    }

    @Test
    public void swaggerUI_exists_true() throws Exception {
        mvc.perform(get("/swagger-ui.html"))
                .andExpect(status().isOk());
    }

}
