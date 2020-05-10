package com.commerzbank.task.repository;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.BookAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Test task Commerzbank
 *
 * Repository for {@link BookAvailability}
 *
 * @author vtanenya
 * */

public interface BookAvailabilityRepository extends JpaRepository<BookAvailability, String> {
    Long deleteAllByBook(Book book);
    Optional<BookAvailability> findByBook(Book book);
}
