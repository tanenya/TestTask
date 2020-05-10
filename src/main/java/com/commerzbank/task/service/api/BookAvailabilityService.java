package com.commerzbank.task.service.api;

import com.commerzbank.task.entity.BookAvailability;

import java.util.List;

/**
 * Test task Commerzbank
 *
 * Interface for {@link BookAvailability} service
 *
 * @author vtanenya
 * */

public interface BookAvailabilityService {
    List<BookAvailability> listAll();
    BookAvailability getById(String id);
    BookAvailability createBookAvailability(BookAvailability book);
    BookAvailability updateBookAvailability(String id, BookAvailability book);
    void deleteBookAvailability(String id);
}
