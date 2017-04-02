package org.daimler.error;

/**
 * @author Abhilash Ghosh
 */
public class OrderNotFoundException extends UnknownResourceException {

    public OrderNotFoundException(Long orderId) {
        super("Order with ID \"" + orderId + "\" not found");
    }
}
