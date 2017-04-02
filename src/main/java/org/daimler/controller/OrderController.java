package org.daimler.controller;

import org.daimler.entity.order.Order;
import org.daimler.entity.order.OrderItem;
import org.daimler.entity.order.OrderStatus;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.ItemNotFoundException;
import org.daimler.error.OrderNotFoundException;
import org.daimler.security.JwtAuthenticationResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;


/**
 * Rest controller for order.
 * This is just the API design layout for demonstration purpose.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> get(@RequestParam("id") Long id) throws OrderNotFoundException {
        //TODO: implement the logic
        /*return "Order{" +
                "id=" + orderId +
                ", totalOrderPrice=" + "129,00" +
                ", paidDate=" + new Date() +
                ", orderStatus=" + OrderStatus.COMPLETED +
                ", transactionId='" + "TX-100100101" +
                ", userId=" + "1" +
                ", createdAt=" + new Date() +
                ", lastUpdated=" + new Date() +
                ", orderItems=" + "1" +
                '}'; */
        return ResponseEntity.ok(new Order());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, path = "/all")
    @ResponseBody
    public ResponseEntity<?> list() throws OrderNotFoundException {
        //TODO: Implement the logic for listing all orders by pageNumber
        return ResponseEntity.ok(new HashSet<Order>());
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Order order) throws EntityPersistenceException {
        //TODO: implement the logic
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.PUT, path = "{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public Order update(@PathVariable("orderId") Long orderId, @RequestBody Order order) throws EntityPersistenceException {
        //TODO: update the order entity and return the updated one
        return new Order();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.DELETE, path = "{orderId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("orderId") Long orderId) throws EntityPersistenceException {
        //TODO: Only the admins are allowed to delete an order.
        //TODO: Though in real life scenario deleting order should not be possible as it is necessary for reporting purpose
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.PATCH, path = "{orderId}/cancel")
    @ResponseBody
    public Order cancelOrder(@PathVariable("orderId") Long orderId) throws OrderNotFoundException {

        //TODO: implement the logic
        return new Order();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, path = "{orderId}/status")
    @ResponseBody
    public OrderStatus getStatus(@PathVariable("orderId") Long orderId) throws OrderNotFoundException {

        //TODO: implement the logic
        return new Order().getOrderStatus();
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = RequestMethod.GET, path = "{orderId}/items")
    @ResponseBody
    public Set<OrderItem> orderDetails(@PathVariable("orderId") Long orderId) throws ItemNotFoundException, OrderNotFoundException {

        //TODO: implement the logic
        return new Order().getOrderItems();
    }
}
