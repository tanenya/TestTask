package com.commerzbank.task.rest;

import com.commerzbank.task.SampleDataInitializer;
import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderItem;
import com.commerzbank.task.entity.OrderStatus;
import com.commerzbank.task.service.api.BookService;
import com.commerzbank.task.service.api.OrderService;
import com.commerzbank.task.utils.JsonUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrderControllerTest extends AbstractTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SampleDataInitializer sampleDataInitializer;

    private String orderId;

    @Before
    public void init() {
        orderId = createOrder();
    }

    @After
    public void reinitializeSampleData() throws Exception {
        sampleDataInitializer.run("");
    }

    @Test
    @WithUserDetails
    public void testGetAllOrdersSuccess() throws Exception {
        this.mockMvc.perform(get("/order")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$._embedded.orderList", hasSize(4)))
                .andExpect(jsonPath("$._embedded.orderList[0].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[0].status").value(OrderStatus.PROCESSING.name()))
                .andExpect(jsonPath("$._embedded.orderList[0].items").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[0].priceSum").value(84))
                .andExpect(jsonPath("$._embedded.orderList[0].description").value("Description for Order 0"))
                .andExpect(jsonPath("$._embedded.orderList[1].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[1].status").value(OrderStatus.PROCESSING.name()))
                .andExpect(jsonPath("$._embedded.orderList[1].items").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[1].priceSum").value(84))
                .andExpect(jsonPath("$._embedded.orderList[1].description").value("Description for Order 1"))
                .andExpect(jsonPath("$._embedded.orderList[2].id").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[2].status").value(OrderStatus.PROCESSING.name()))
                .andExpect(jsonPath("$._embedded.orderList[2].items").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[2].priceSum").value(84))
                .andExpect(jsonPath("$._embedded.orderList[2].description").value("Description for Order 2"))
                .andExpect(jsonPath("$._embedded.orderList[3].id").value(orderId))
                .andExpect(jsonPath("$._embedded.orderList[3].status").value(OrderStatus.PROCESSING.name()))
                .andExpect(jsonPath("$._embedded.orderList[2].items").isNotEmpty())
                .andExpect(jsonPath("$._embedded.orderList[3].priceSum").value(10))
                .andExpect(jsonPath("$._embedded.orderList[3].description").value("Description for new order"));
    }

    @Test
    public void testGetAllOrdersForbidden() throws Exception {
        this.mockMvc.perform(get("/order")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails
    public void testGetOrderByIdSuccess() throws Exception {
        this.mockMvc.perform(get("/order/" + orderId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(orderId))
                .andExpect(jsonPath("$.items").isNotEmpty())
                .andExpect(jsonPath("$.priceSum").value(10))
                .andExpect(jsonPath("$.description").value("Description for new order"));
    }

    @Test
    @WithUserDetails
    public void testGetOrderByIdNotFound() throws Exception {
        this.mockMvc.perform(get("/order/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testCreateOrderSuccess() throws Exception {
        List<Book> books = bookService.listAll();
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.builder().count(3).book(books.get(0)).build());
        orderItems.add(OrderItem.builder().count(5).book(books.get(1)).build());

        this.mockMvc.perform(post("/order")
                .content(JsonUtils.toJsonString(
                        Order.builder()
                                .items(orderItems)
                                .priceSum(BigDecimal.TEN)
                                .description("Description for new order")
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testCreateOrderValidationError() throws Exception {
        this.mockMvc.perform(post("/order")
                .content(JsonUtils.toJsonString(
                        Order.builder()
                                .items(null)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testUpdateOrderSuccess() throws Exception {
        List<Book> books = bookService.listAll();
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.builder().count(3).book(books.get(0)).build());
        orderItems.add(OrderItem.builder().count(5).book(books.get(1)).build());

        this.mockMvc.perform(put("/order/" + orderId)
                .content(JsonUtils.toJsonString(
                        Order.builder()
                                .items(orderItems)
                                .priceSum(BigDecimal.ONE)
                                .description("Description for updated order")
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testUpdateOrderValidationError() throws Exception {
        this.mockMvc.perform(put("/order/" + orderId)
                .content(JsonUtils.toJsonString(
                        Order.builder()
                                .items(null)
                                .build()))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testUpdateOrderItemsSuccess() throws Exception {
        List<Book> books = bookService.listAll();
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.builder().count(5).book(books.get(0)).build());
        orderItems.add(OrderItem.builder().count(8).book(books.get(1)).build());

        this.mockMvc.perform(post("/order/" + orderId + "/items")
                .content(JsonUtils.toJsonString(orderItems))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails
    public void testUpdateOrderItemsUnavailableError() throws Exception {
        List<Book> books = bookService.listAll();
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.builder().count(50).book(books.get(0)).build());
        orderItems.add(OrderItem.builder().count(80).book(books.get(1)).build());

        this.mockMvc.perform(post("/order/" + orderId + "/items")
                .content(JsonUtils.toJsonString(orderItems))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithUserDetails
    public void testDeleteOrderSuccess() throws Exception {
        this.mockMvc.perform(delete("/order/" + orderId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithUserDetails
    public void testDeleteOrderNotFound() throws Exception {
        this.mockMvc.perform(delete("/order/" + "wrongId")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails
    public void testCancelOrderSuccess() throws Exception {
        this.mockMvc.perform(post("/order/" + orderId + "/cancel")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    public void testCancelOrderNotAllowedError() throws Exception {
        this.mockMvc.perform(post("/order/" + orderId + "/complete"));
        this.mockMvc.perform(post("/order/" + orderId + "/cancel")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    @WithUserDetails
    public void testCompleteOrderSuccess() throws Exception {
        this.mockMvc.perform(post("/order/" + orderId + "/complete")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails
    public void testCompleteOrderNotAllowedError() throws Exception {
        this.mockMvc.perform(post("/order/" + orderId + "/cancel"));
        this.mockMvc.perform(post("/order/" + orderId + "/complete")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isMethodNotAllowed());
    }

    private String createOrder() {
        List<Book> books = bookService.listAll();
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.builder().count(3).book(books.get(0)).build());
        orderItems.add(OrderItem.builder().count(5).book(books.get(1)).build());
        return orderService.createOrder(Order.builder()
                .items(orderItems)
                .priceSum(BigDecimal.TEN)
                .description("Description for new order")
        .build()).getId();
    }
}
