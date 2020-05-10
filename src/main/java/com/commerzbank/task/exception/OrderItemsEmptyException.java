package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when {@link com.commerzbank.task.entity.OrderItem} list is empty
 *
 * @author vtanenya
 * */

public class OrderItemsEmptyException extends RuntimeException {
    public OrderItemsEmptyException() {}

    public OrderItemsEmptyException(String id) {
        super("Order items can not be empty for Order with id " + id);
    }
}
