package com.commerzbank.task.service.impl;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.exception.BookAvailabilityDuplicateException;
import com.commerzbank.task.exception.BookAvailabilityNotFoundException;
import com.commerzbank.task.exception.BookNotFoundException;
import com.commerzbank.task.repository.BookAvailabilityRepository;
import com.commerzbank.task.repository.BookRepository;
import com.commerzbank.task.service.api.BookAvailabilityService;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Test task Commerzbank
 *
 * Implementation of {@link BookAvailabilityService} service
 *
 * @author vtanenya
 * */

@Service
@Log4j2
public class BookAvailabilityServiceImpl implements BookAvailabilityService {

    private BookAvailabilityRepository bookAvailabilityRepository;
    private BookRepository bookRepository;

    public BookAvailabilityServiceImpl(BookAvailabilityRepository bookAvailabilityRepository,
                                       BookRepository bookRepository) {
        this.bookAvailabilityRepository = bookAvailabilityRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookAvailability> listAll() {
        return bookAvailabilityRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public BookAvailability getById(String id) {
        return bookAvailabilityRepository.findById(id).orElseThrow(() -> new BookAvailabilityNotFoundException(id));
    }

    @Override
    @Transactional
    public BookAvailability createBookAvailability(BookAvailability bookAvailability) {
        Book book = bookRepository.findByIdAndOutOfStock(bookAvailability.getBook().getId(), false)
                .orElseThrow(() -> new BookNotFoundException(bookAvailability.getBook().getId()));

        // if bookAvailability already exist throw exception
        if (bookAvailabilityRepository.findByBook(book).isPresent())
            throw new BookAvailabilityDuplicateException(book.getId());

        bookAvailability.setBook(book);

        return bookAvailabilityRepository.save(bookAvailability);
    }

    @Override
    @Transactional
    public BookAvailability updateBookAvailability(String id, BookAvailability bookAvailability) {
        Book book = bookRepository.findByIdAndOutOfStock(bookAvailability.getBook().getId(), false)
                .orElseThrow(() -> new BookNotFoundException(bookAvailability.getBook().getId()));

        // if bookAvailability already exist and it is not the same as updating throw exception
        Optional<BookAvailability> existingAvailability = bookAvailabilityRepository.findByBook(book);
        if (existingAvailability.isPresent() && !id.equals(existingAvailability.get().getId()))
            throw new BookAvailabilityDuplicateException(book.getId());

        return bookAvailabilityRepository.findById(id).map(bookAvailabilityToUpdate -> {
            bookAvailabilityToUpdate.setCount(bookAvailability.getCount());
            bookAvailabilityToUpdate.setBook(book);
            return bookAvailabilityRepository.save(bookAvailabilityToUpdate);
        })
                .orElseThrow(() -> new BookAvailabilityNotFoundException(id));
    }

    @Override
    @Transactional
    public void deleteBookAvailability(String id) {
        BookAvailability bookAvailability = bookAvailabilityRepository.findById(id)
                .orElseThrow(() -> new BookAvailabilityNotFoundException(id));
        bookAvailabilityRepository.delete(bookAvailability);
    }
}
