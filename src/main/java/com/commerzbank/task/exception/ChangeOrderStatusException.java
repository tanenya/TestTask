package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when attempting to change {@link com.commerzbank.task.entity.OrderStatus}
 * of {@link com.commerzbank.task.entity.Order} which is not in PROCESSING status
 *
 * @author vtanenya
 * */

public class ChangeOrderStatusException extends RuntimeException {
    public ChangeOrderStatusException() {
    }

    public ChangeOrderStatusException(String oldStatus, String newStatus) {
        super("Status change from " + oldStatus + " to " + newStatus + " is not allowed.");
    }
}
