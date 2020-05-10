package com.commerzbank.task.rest;

import com.commerzbank.task.SampleDataInitializer;
import com.commerzbank.task.entity.Book;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BookControllerTest extends AbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private SampleDataInitializer sampleDataInitializer;

    private String bookId;

    @Before
    public void init() {
        this.bookId = bookService.createBook(Book.builder()
                .name("test book")
                .author("test author")
                .description("test description")
                .price(BigDecimal.ONE).build())
                .getId();
    }

    @After
    public void reinitializeSampleData() throws Exception {
        sampleDataInitializer.run("");
    }

    @Test
    @WithUserDetails
    public void testGetAllBooksSuccess() throws Exception {
        this.mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.bookList[0].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookList[0].name").value("Book 0"))
                .andExpect(jsonPath("$._embedded.bookList[0].author").value("author of Book 0"))
                .andExpect(jsonPath("$._embedded.bookList[0].price").value(3))
                .andExpect(jsonPath("$._embedded.bookList[0].description").value("Description for Book 0"))
                .andExpect(jsonPath("$._embedded.bookList[1].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookList[1].name").value("Book 1"))
                .andExpect(jsonPath("$._embedded.bookList[1].author").value("author of Book 1"))
                .andExpect(jsonPath("$._embedded.bookList[1].price").value(6))
                .andExpect(jsonPath("$._embedded.bookList[1].description").value("Description for Book 1"))
                .andExpect(jsonPath("$._embedded.bookList[2].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.bookList[2].name").value("Book 2"))
                .andExpect(jsonPath("$._embedded.bookList[2].author").value("author of Book 2"))
                .andExpect(jsonPath("$._embedded.bookList[2].price").value(9))
                .andExpect(jsonPath("$._embedded.bookList[2].description").value("Description for Book 2"));
    }

    @Test
    public void testGetAllBooksForbidden() throws Exception {
        this.mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails
    public void testGetBookByIdSuccess() throws Exception {
        this.mockMvc.perform(get("/book/" + bookId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(bookId))
                .andExpect(jsonPath("$.name").value("test book"))
                .andExpect(jsonPath("$.author").value("test author"))
                .andExpect(jsonPath("$.price").value(1))
                .andExpect(jsonPath("$.description").value("test description"));
    }

    @Test
    @WithUserDetails
    public void testGetBookByIdNotFound() throws Exception {
        this.mockMvc.perform(get("/book/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testCreateBookSuccess() throws Exception {
        this.mockMvc.perform(post("/book")
                .content(JsonUtils.toJsonString(
                        Book.builder()
                            .name("test book")
                            .author("test author")
                            .description("test description")
                            .price(BigDecimal.ONE).build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testCreateBookValidationError() throws Exception {
        this.mockMvc.perform(post("/book")
                .content(JsonUtils.toJsonString(
                        Book.builder()
                                .name(null)
                                .author(null)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testUpdateBookSuccess() throws Exception {
        this.mockMvc.perform(put("/book/" + bookId)
                .content(JsonUtils.toJsonString(
                        Book.builder()
                                .name("test book updated")
                                .author("test author updated")
                                .description("test description updated")
                                .price(BigDecimal.TEN).build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testUpdateBookValidationError() throws Exception {
        this.mockMvc.perform(put("/book/" + bookId)
                .content(JsonUtils.toJsonString(
                        Book.builder()
                                .name(null)
                                .author(null)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testDeleteBookSuccess() throws Exception {
        this.mockMvc.perform(delete("/book/" + bookId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails
    public void testDeleteBookNotFound() throws Exception {
        this.mockMvc.perform(delete("/book/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
