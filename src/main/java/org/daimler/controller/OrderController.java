package org.daimler.controller;

import org.daimler.entity.order.Order;
import org.daimler.entity.order.OrderStatus;
import org.daimler.error.EntityPersistenceException;
import org.daimler.error.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


/**
 * Rest controller for order.
 * This is just the API design layout for demonstration purpose.
 */
@RestController
@RequestMapping("/orders")
public class OrderController {

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Order get(@RequestParam("orderId") Long orderId) throws OrderNotFoundException {
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
        return new Order();
    }

    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Order order) throws EntityPersistenceException {
        //TODO: implement the logic
    }

    @RequestMapping(method = RequestMethod.PUT, path = "{orderId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.OK)
    public Order update(@RequestParam("orderId") Long orderId, @RequestBody Order order) throws EntityPersistenceException {
        //TODO: update the order entity and return the updated one
        return new Order();
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "{orderId}")
    @Secured({"ROLE_ADMIN"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@RequestParam("orderId") Long orderId) throws EntityPersistenceException {
        //TODO: Only the admins are allowed to delete an order.
        //TODO: Though in real life scenario deleting order should not be possible as it is necessary for reporting purpose
    }

    @RequestMapping(method = RequestMethod.PATCH, path = "{orderId}/cancel")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Order cancelOrder(@RequestParam("orderId") Long orderId) throws OrderNotFoundException {

        //TODO: implement the logic
        return new Order();
    }

    @RequestMapping(method = RequestMethod.GET, path = "{orderId}/status")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public OrderStatus getStatus(@RequestParam("orderId") Long orderId) throws OrderNotFoundException {

        //TODO: implement the logic
        return new Order().getOrderStatus();
    }
}
