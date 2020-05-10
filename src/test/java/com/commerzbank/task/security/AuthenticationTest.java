package com.commerzbank.task.security;

import com.commerzbank.task.rest.AuthenticationRequest;
import com.commerzbank.task.utils.JsonUtils;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AuthenticationTest {

    @Autowired
    private MockMvc mockMvc;

    private String token;

    @Before
    public void setup() throws Exception {
        MvcResult result = this.mockMvc.perform(post("/auth/signin")
                .content(JsonUtils.toJsonString(
                        AuthenticationRequest.builder()
                                .username("user")
                                .password("password").build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
        log.debug("Got token:" + token);
    }

    @Test
    public void getAllBooksSuccess() throws Exception {
        this.mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization", "Bearer " + token))
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
    public void getAllBooksForbidden() throws Exception {
        this.mockMvc.perform(get("/book")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
