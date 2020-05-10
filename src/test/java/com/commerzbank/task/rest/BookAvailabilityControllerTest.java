package com.commerzbank.task.rest;

import com.commerzbank.task.SampleDataInitializer;
import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.service.api.BookAvailabilityService;
import com.commerzbank.task.service.api.BookService;
import com.commerzbank.task.utils.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookAvailabilityControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookAvailabilityService bookAvailabilityService;

    @Autowired
    private SampleDataInitializer sampleDataInitializer;

    private String bookId;
    private String bookAvailabilityId;

    @Before
    public void init() {
        this.bookId = createBook();

        Book book = new Book();
        book.setId(bookId);

        this.bookAvailabilityId = createBookAvailability(book);
    }

    @After
    public void reinitializeSampleData() throws Exception {
        sampleDataInitializer.run("");
    }

    @Test
    @WithUserDetails
    public void testGetAllBookAvailabilitiesSuccess() throws Exception {
        this.mockMvc.perform(get("/bookAvailability")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList", hasSize(4)))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[0].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[0].count").value(10))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[0].book.id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[1].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[1].count").value(10))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[1].book.id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[2].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[2].count").value(10))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[2].book.id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[3].id").value(bookAvailabilityId))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[3].count").value(5))
                .andExpect(jsonPath("$._embedded.bookAvailabilityList[3].book.id").value(bookId));
    }

    @Test
    public void testGetAllBooksAvailabilitiesForbidden() throws Exception {
        this.mockMvc.perform(get("/bookAvailability")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails
    public void testGetBookAvailabilityByIdSuccess() throws Exception {
        this.mockMvc.perform(get("/bookAvailability/" + bookAvailabilityId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(bookAvailabilityId))
                .andExpect(jsonPath("$.count").value(5))
                .andExpect(jsonPath("$.book.id").value(bookId));
    }

    @Test
    @WithUserDetails
    public void testGetBookAvailabilityByIdNotFound() throws Exception {
        this.mockMvc.perform(get("/bookAvailability/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testCreateBookAvailabilitySuccess() throws Exception {
        String newBookId = createBook();
        Book newBook = new Book();
        newBook.setId(newBookId);

        this.mockMvc.perform(post("/bookAvailability")
                .content(JsonUtils.toJsonString(
                        BookAvailability.builder()
                                .count(7)
                                .book(newBook)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testCreateBookAvailabilityDuplicationError() throws Exception {
        Book newBook = new Book();
        newBook.setId(bookId);

        this.mockMvc.perform(post("/bookAvailability")
                .content(JsonUtils.toJsonString(
                        BookAvailability.builder()
                                .count(7)
                                .book(newBook)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testUpdateBookAvailabilitySuccess() throws Exception {
        Book newBook = new Book();
        newBook.setId(bookId);

        this.mockMvc.perform(put("/bookAvailability/" + bookAvailabilityId)
                .content(JsonUtils.toJsonString(
                        BookAvailability.builder()
                                .count(5)
                                .book(newBook)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testUpdateBookAvailabilityValidationError() throws Exception {
        this.mockMvc.perform(put("/bookAvailability/" + bookAvailabilityId)
                .content(JsonUtils.toJsonString(
                        BookAvailability.builder()
                                .count(null)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testDeleteBookAvailabilitySuccess() throws Exception {
        this.mockMvc.perform(delete("/bookAvailability/" + bookAvailabilityId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails
    public void testDeleteBookAvailabilityNotFound() throws Exception {
        this.mockMvc.perform(delete("/bookAvailability/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    private String createBook() {
        return bookService.createBook(
                Book.builder()
                        .name("test book")
                        .author("test author")
                        .description("test description")
                        .price(BigDecimal.ONE)
                        .build())
                    .getId();
    }

    private String createBookAvailability(Book book) {
        return bookAvailabilityService.createBookAvailability(
                BookAvailability.builder()
                        .count(5)
                        .book(book)
                        .build())
                    .getId();
    }

}
