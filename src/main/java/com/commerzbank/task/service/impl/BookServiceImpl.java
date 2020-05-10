package com.commerzbank.task.service.impl;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.exception.BookNotFoundException;
import com.commerzbank.task.repository.BookAvailabilityRepository;
import com.commerzbank.task.repository.BookRepository;
import com.commerzbank.task.service.api.BookService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Test task Commerzbank
 *
 * Implementation of {@link BookService}
 *
 * @author vtanenya
 * */

@Service
@Log4j2
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;
    private BookAvailabilityRepository bookAvailabilityRepository;

    public BookServiceImpl(BookRepository bookRepository, BookAvailabilityRepository bookAvailabilityRepository) {
        this.bookRepository = bookRepository;
        this.bookAvailabilityRepository = bookAvailabilityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Book> listAll() {
        return bookRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Book getById(String id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional
    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    @Transactional
    public Book updateBook(String id, Book book) {
        return bookRepository.findByIdAndOutOfStock(id, false).map(bookToUpdate -> {
            bookToUpdate.setName(book.getName());
            bookToUpdate.setAuthor(book.getAuthor());
            bookToUpdate.setDescription(book.getDescription());
            bookToUpdate.setPrice(book.getPrice());
            return bookRepository.save(bookToUpdate);
        }).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteBook(String id) {
        // we can not delete books because of reference in orderItem
        // instead of deleting setting outOfStock flag
        Book bookToDelete = bookRepository.findByIdAndOutOfStock(id, false).map(book -> {
            book.setOutOfStock(true);
            return bookRepository.save(book);
        }).orElseThrow(() -> new BookNotFoundException(id));
        bookAvailabilityRepository.deleteAllByBook(bookToDelete);
    }
}
