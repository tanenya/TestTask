package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when attempting to replace {@link com.commerzbank.task.entity.OrderItem} of
 * {@link com.commerzbank.task.entity.Order} which is not in PROCESSING status
 *
 * @author vtanenya
 * */

public class ReplaceOrderItemsException extends RuntimeException {
    public ReplaceOrderItemsException() {}

    public ReplaceOrderItemsException(String id) {
        super("Item replace is not allowed for order with id " + id);
    }
}
