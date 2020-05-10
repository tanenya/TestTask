package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when {@link com.commerzbank.task.entity.BookAvailability} for specified
 * {@link com.commerzbank.task.entity.Book} is already created
 *
 * @author vtanenya
 * */

public class BookAvailabilityDuplicateException extends RuntimeException {
    public BookAvailabilityDuplicateException() {}

    public BookAvailabilityDuplicateException(String id) {
        super("Book availability already created for Book with id " + id);
    }
}
