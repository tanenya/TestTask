package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when {@link com.commerzbank.task.entity.Book} not found
 *
 * @author vtanenya
 * */

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException() {}

    public BookNotFoundException(String id) {
        super("Book with id " + id + " not found or out of stock");
    }
}
