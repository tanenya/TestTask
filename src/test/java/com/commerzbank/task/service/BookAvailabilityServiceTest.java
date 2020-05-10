package com.commerzbank.task.service;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.exception.BookAvailabilityDuplicateException;
import com.commerzbank.task.exception.BookAvailabilityNotFoundException;
import com.commerzbank.task.exception.BookNotFoundException;
import com.commerzbank.task.service.api.BookAvailabilityService;
import com.commerzbank.task.service.api.BookService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookAvailabilityServiceTest {

    @Autowired
    private BookAvailabilityService bookAvailabilityService;

    @Autowired
    private BookService bookService;

    @Test
    @Transactional
    public void testCreateBookAvailabilitySuccess() {
        Book book = createBook();
        BookAvailability bookAvailability = bookAvailabilityService.createBookAvailability(BookAvailability.builder()
                .count(7).book(book).build());

        assertNotNull(bookAvailability.getId());
        assertEquals(7, bookAvailability.getCount().intValue());
        assertEquals(book.getId(), bookAvailability.getBook().getId());
    }

    @Test(expected = BookAvailabilityDuplicateException.class)
    public void testCreateBookAvailabilityDuplicationError() {
        Book book = bookService.listAll().get(0);
        bookAvailabilityService.createBookAvailability(BookAvailability.builder()
                .count(7).book(book).build());
    }

    @Test(expected = BookNotFoundException.class)
    public void testCreateBookAvailabilityBookNotFoundError() {
        Book book = new Book();
        book.setId("wrongId");
        bookAvailabilityService.createBookAvailability(BookAvailability.builder()
                .count(7).book(book).build());
    }

    @Test
    @Transactional
    public void testUpdateBookAvailabilitySuccess() {
        Book book = createBook();
        BookAvailability bookAvailability = bookAvailabilityService.listAll().get(0);
        bookAvailability = bookAvailabilityService.updateBookAvailability(bookAvailability.getId(),
                BookAvailability.builder().count(33).book(book).build());

        assertEquals(33, bookAvailability.getCount().intValue());
        assertEquals(book.getId(), bookAvailability.getBook().getId());
    }

    @Test(expected = BookAvailabilityDuplicateException.class)
    public void testUpdateBookAvailabilityDuplicationError() {
        Book book = bookService.listAll().get(0);
        BookAvailability bookAvailability = bookAvailabilityService.listAll().get(1);
        bookAvailabilityService.updateBookAvailability(bookAvailability.getId(),
                BookAvailability.builder()
                        .count(7).book(book).build());
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBookAvailabilityBookNotFoundError() {
        Book book = new Book();
        book.setId("wrongId");
        BookAvailability bookAvailability = bookAvailabilityService.listAll().get(0);
        bookAvailabilityService.updateBookAvailability(bookAvailability.getId(),
                BookAvailability.builder()
                .count(7).book(book).build());
    }

    @Test(expected = BookAvailabilityNotFoundException.class)
    public void testUpdateBookAvailabilityAvailabilityNotFoundError() {
        Book book = createBook();
        bookAvailabilityService.updateBookAvailability("wrongId",
                BookAvailability.builder().count(33).book(book).build());
    }

    @Test(expected = BookAvailabilityNotFoundException.class)
    @Transactional
    public void testDeleteBookAvailabilitySuccess() {
        BookAvailability bookAvailability = bookAvailabilityService.listAll().get(0);
        bookAvailabilityService.deleteBookAvailability(bookAvailability.getId());

        bookAvailabilityService.getById(bookAvailability.getId());
    }

    @Test(expected = BookAvailabilityNotFoundException.class)
    public void testDeleteBookAvailabilityAvailabilityNotFoundError() {
        bookAvailabilityService.deleteBookAvailability("wrongId");
    }

    private Book createBook() {
        return bookService.createBook(
                Book.builder()
                        .name("test book")
                        .author("test author")
                        .description("test description")
                        .price(BigDecimal.ONE)
                        .build());
    }
}
