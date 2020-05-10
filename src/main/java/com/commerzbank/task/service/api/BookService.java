package com.commerzbank.task.service.api;

import com.commerzbank.task.entity.Book;

import java.util.List;

/**
 * Test task Commerzbank
 *
 * Interface for {@link Book} service
 *
 * @author vtanenya
 * */

public interface BookService {
    List<Book> listAll();
    Book getById(String id);
    Book createBook(Book book);
    Book updateBook(String id, Book book);
    void deleteBook(String id);
}
