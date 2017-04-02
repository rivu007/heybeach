package org.daimler.controller;

import org.daimler.BaseTest;
import org.daimler.entity.order.Order;
import org.daimler.entity.order.OrderStatus;
import org.daimler.entity.user.User;
import org.daimler.repository.OrderDAO;
import org.daimler.repository.UserDAO;
import org.daimler.security.JwtTokenUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.hamcrest.Matchers.startsWith;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link OrderController}
 *
 * @author abhilash.ghosh
 */
public class OrderControllerTest  extends BaseTest {

    @Value("${jwt.header}")
    private String JWTHeader;

    @Value("${jwt.expiration}")
    private long JWTExpiryInterval;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderDAO orderDAO;

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
    public void get_withoutJWTToken_forbiddenAccess() throws Exception {
        mvc.perform(
                get("/orders")
                    .param("id", "100")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void get_withoutId_badRequest() throws Exception {
        User user = createTestUser("test-user");
        String JWTToken = jwtTokenUtil.generateToken(createClaims(user.getUsername()));

        mvc.perform(
                get("/orders")
                    .header(JWTHeader, JWTToken)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                        jsonPath("$.message", startsWith("Required Long parameter 'id' is not present")
                )
        );
    }

    @Test
    public void get_withValidData_success() throws Exception {
        User user = createTestUser("test-user");
        String JWTToken = jwtTokenUtil.generateToken(createClaims(user.getUsername()));

        mvc.perform(
                get("/orders")
                    .param("id", "100")
                    .header(JWTHeader, JWTToken)
                )
                .andExpect(status().isOk()
        );
    }

    @Test
    public void list_withoutJWTToken_forbiddenAccess() throws Exception {
        mvc.perform(
                    get("/orders/all")
                )
                .andExpect(status().isForbidden()
        );
    }

    @Test
    public void list_validData_returnsOrderLists() throws Exception {
        User user = createTestUser("test-user");
        String JWTToken = jwtTokenUtil.generateToken(createClaims(user.getUsername()));

        mvc.perform(
                get("/orders/all")
                    .header(JWTHeader, JWTToken)
                )
                .andExpect(status().isOk()
        );
    }

    @Test
    public void create_withoutRequestBody_badRequest() throws Exception {
        User user = createTestUser("test-user");
        String JWTToken = jwtTokenUtil.generateToken(createClaims(user.getUsername()));

        mvc.perform(
                post("/orders")
                    .header(JWTHeader, JWTToken)
                )
                .andExpect(status().isBadRequest())
                .andExpect(
                    jsonPath("$.message", startsWith("Required request body is missing"))
        );
    }

    @Test
    public void create_withoutValidInput_success() throws Exception {
        User user = createTestUser("test-user");
        String JWTToken = jwtTokenUtil.generateToken(createClaims(user.getUsername()));

        Order order = new Order();
        order.setTotalOrderPrice(199.99);
        order.setPaidDate(new Date());
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setTransactionId("TX-01042017-110");
        order.setUserId(user.getId());

        mvc.perform(
                post("/orders")
                        .header(JWTHeader, JWTToken)
                        .contentType(APPLICATION_JSON_UTF8)
                        .content(jsonOf(order))
        )
                .andExpect(status().isCreated());
    }

    private Order createNewOrder(long userId) {
        Order order = new Order();
        order.setTotalOrderPrice(199.99);
        order.setPaidDate(new Date());
        order.setOrderStatus(OrderStatus.COMPLETED);
        order.setTransactionId("TX-01042017-110");
        order.setUserId(userId);
        return orderDAO.save(order);
    }
}
