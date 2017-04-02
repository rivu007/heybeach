package org.daimler.error;

/**
 * @author Abhilash Ghosh
 */
public class ItemNotFoundException extends UnknownResourceException {

    public ItemNotFoundException(Long itemId) {
        super("Item with ID " + itemId + " not found");
    }

    public ItemNotFoundException(Long itemId, Long orderId) {
        super("Item with id :" + itemId +" not found for order no: " + orderId);
    }
}
