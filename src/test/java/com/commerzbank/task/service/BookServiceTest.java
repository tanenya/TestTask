package com.commerzbank.task.service;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.exception.BookNotFoundException;
import com.commerzbank.task.repository.BookAvailabilityRepository;
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
public class BookServiceTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookAvailabilityRepository bookAvailabilityRepository;

    @Test
    @Transactional
    public void testCreateBookSuccess() {
        Book book = bookService.createBook(Book.builder()
                .name("test book")
                .author("test author")
                .description("test description")
                .price(BigDecimal.ONE)
                .build());

        assertNotNull(book.getId());
        assertEquals("test book", book.getName());
        assertEquals("test author", book.getAuthor());
        assertEquals("test description", book.getDescription());
        assertEquals(BigDecimal.ONE, book.getPrice());
        assertFalse(book.getOutOfStock());
    }

    @Test
    @Transactional
    public void testUpdateBookSuccess() {
        Book book = bookService.listAll().get(0);
        book = bookService.updateBook(book.getId(),
                Book.builder()
                        .name("new name")
                        .author("new author")
                        .price(BigDecimal.ONE)
                        .description("new description")
                        .build());

        assertEquals("new name", book.getName());
        assertEquals("new author", book.getAuthor());
        assertEquals(BigDecimal.ONE, book.getPrice());
        assertEquals("new description", book.getDescription());
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateBookNotFoundError() {
        bookService.updateBook("wrongId",
                Book.builder()
                        .name("new name")
                        .author("new author")
                        .price(BigDecimal.ONE)
                        .description("new description")
                        .build());
    }

    @Test
    @Transactional
    public void testDeleteBookSuccess() {
        Book book = bookService.listAll().get(0);
        bookService.deleteBook(book.getId());

        book = bookService.getById(book.getId());
        assertTrue(book.getOutOfStock());
    }

    @Test(expected = BookNotFoundException.class)
    public void testDeleteBookNotFoundError() {
        bookService.deleteBook("wrongId");
    }

    @Test
    @Transactional
    public void testDeleteBookCheckDeleteAvailabilitiesSuccess() {
        Book book = bookService.listAll().get(0);
        assertFalse(bookAvailabilityRepository.findByBook(book).isEmpty());

        bookService.deleteBook(book.getId());
        assertTrue(bookAvailabilityRepository.findByBook(book).isEmpty());
    }

}
