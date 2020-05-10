package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when count of {@link com.commerzbank.task.entity.Book} in {@link com.commerzbank.task.entity.BookAvailability}
 * is less than requested in {@link com.commerzbank.task.entity.OrderItem}
 *
 * @author vtanenya
 * */

public class BookUnavailableException extends RuntimeException {
    public BookUnavailableException() {}

    public BookUnavailableException(String id) {
        super("Book with id " + id + " not available");
    }

    public BookUnavailableException(String id, Integer requested, Integer available) {
        super("Book with id " + id + " not available. Requested amount " + requested + " but available " + available);
    }
}
