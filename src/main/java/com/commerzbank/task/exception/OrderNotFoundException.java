package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when {@link com.commerzbank.task.entity.Order} is not found
 *
 * @author vtanenya
 * */

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException() {
    }

    public OrderNotFoundException(String id) {
        super("Order with id " + id + " not found");
    }
}
