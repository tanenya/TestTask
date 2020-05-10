package com.commerzbank.task.repository;

import com.commerzbank.task.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Test task Commerzbank
 *
 * Repository for {@link Book}
 *
 * @author vtanenya
 * */

public interface BookRepository extends JpaRepository<Book, String> {
    Optional<Book> findByIdAndOutOfStock(String id, Boolean outOfStock);
    List<Book> findAllByOutOfStock(Boolean outOfStock);
}
