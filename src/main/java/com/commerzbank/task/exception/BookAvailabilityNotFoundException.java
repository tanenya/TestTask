package com.commerzbank.task.exception;

import com.commerzbank.task.entity.Book;

/**
 * Test task Commerzbank
 *
 * Throws when {@link com.commerzbank.task.entity.BookAvailability} not found
 *
 * @author vtanenya
 * */

public class BookAvailabilityNotFoundException extends RuntimeException {
    public BookAvailabilityNotFoundException() {}

    public BookAvailabilityNotFoundException(String id) {
        super("Book availability with id " + id + " not found");
    }

    public BookAvailabilityNotFoundException(Book book) {
        super("Book availability for Book " + book + " not found");
    }
}
