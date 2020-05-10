package com.commerzbank.task.service;

import com.commerzbank.task.entity.*;
import com.commerzbank.task.exception.*;
import com.commerzbank.task.repository.BookAvailabilityRepository;
import com.commerzbank.task.repository.OrderItemRepository;
import com.commerzbank.task.service.api.BookService;
import com.commerzbank.task.service.api.OrderService;
import com.commerzbank.task.util.OrderUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BookService bookService;

    @Autowired
    private BookAvailabilityRepository bookAvailabilityRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Test
    @Transactional
    public void testCreateOrderWithItemSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Integer initialCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        Order order = createOrder(book);

        assertNotNull(order.getId());
        assertEquals(1, order.getItems().iterator().next().getCount().intValue());
        assertEquals(book, order.getItems().iterator().next().getBook());
        assertEquals(BigDecimal.TEN, order.getPriceSum());
        assertEquals("Order description", order.getDescription());
        assertEquals(OrderStatus.PROCESSING, order.getStatus());

        Integer actualCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        assertEquals(initialCount.intValue(), order.getItems().iterator().next().getCount() + actualCount);

    }

    @Test(expected = BookNotFoundException.class)
    public void testCreateOrderWithItemBookNotFoundError() {
        Book book = new Book();
        book.setId("wrongId");
        createOrder(book);
    }

    @Test(expected = OrderItemsEmptyException.class)
    public void testCreateOrderWithOutItemOrderItemsEmptyError() {
        Set<OrderItem> orderItemSet = new HashSet<>();
        orderService.createOrder(
                Order.builder()
                        .items(orderItemSet)
                        .priceSum(BigDecimal.ONE)
                        .description("new Order description")
                        .build());
    }

    @Test(expected = BookUnavailableException.class)
    public void testCreateOrderWithItemBookUnavailableError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(book).count(20).build());
        orderService.createOrder(Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.TEN)
                .description("Order description")
                .build());
    }

    @Test(expected = BookAvailabilityNotFoundException.class)
    @Transactional
    public void testCreateOrderWithItemBookAvailabilityNotFoundError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        bookAvailabilityRepository.deleteAllByBook(book);

        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(book).count(2).build());
        orderService.createOrder(Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.TEN)
                .description("Order description")
                .build());
    }

    @Test
    @Transactional
    public void testUpdateOrderWithItemSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = bookList.get(1);
        Integer initialCountBook = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        Integer initialCountOtherBook = bookAvailabilityRepository.findByBook(otherBook).map(BookAvailability::getCount).get();
        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(otherBook).count(2).build());

        Order orderToUpdate = createOrder(book);
        orderToUpdate = orderService.updateOrder(orderToUpdate.getId(),
                Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.ONE)
                .description("new Order description")
                .build());

        assertEquals(1, orderToUpdate.getItems().size());
        assertEquals(2, orderToUpdate.getItems().iterator().next().getCount().intValue());
        assertEquals(otherBook, orderToUpdate.getItems().iterator().next().getBook());
        assertEquals(BigDecimal.ONE, orderToUpdate.getPriceSum());
        assertEquals("new Order description", orderToUpdate.getDescription());

        Integer actualCountBook = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        assertEquals(initialCountBook.intValue(), actualCountBook.intValue());

        Integer actualCountOtherBook = bookAvailabilityRepository.findByBook(otherBook).map(BookAvailability::getCount).get();
        assertEquals(initialCountOtherBook.intValue(), actualCountOtherBook + orderToUpdate.getItems().iterator().next().getCount());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testUpdateOrderWithItemOrderNotFoundError() {
        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(bookService.listAll().get(0)).count(2).build());

        orderService.updateOrder("wrongId",
                Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.ONE)
                .description("new Order description")
                .build());
    }

    @Test(expected = OrderItemsEmptyException.class)
    public void testUpdateOrderWithOutItemOrderItemsEmptyError() {
        Set<OrderItem> orderItemSet = new HashSet<>();
        Order orderToUpdate = createOrder(bookService.listAll().get(0));
        orderService.updateOrder(orderToUpdate.getId(),
                Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.ONE)
                .description("new Order description")
                .build());
    }

    @Test(expected = BookUnavailableException.class)
    public void testUpdateOrderWithItemBookUnavailableError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = bookList.get(1);

        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(otherBook).count(20).build());

        Order orderToUpdate = createOrder(book);
        orderService.updateOrder(orderToUpdate.getId(),
                Order.builder()
                        .items(orderItemSet)
                        .priceSum(BigDecimal.ONE)
                        .description("new Order description")
                        .build());
    }

    @Test(expected = BookNotFoundException.class)
    public void testUpdateOrderWithItemBookNotFoundError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = new Book();
        otherBook.setId("wrongId");

        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(otherBook).count(2).build());

        Order orderToUpdate = createOrder(book);
        orderService.updateOrder(orderToUpdate.getId(),
                Order.builder()
                        .items(orderItemSet)
                        .priceSum(BigDecimal.ONE)
                        .description("new Order description")
                        .build());
    }

    @Test(expected = BookAvailabilityNotFoundException.class)
    @Transactional
    public void testUpdateOrderWithItemBookAvailabilityNotFoundError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = bookList.get(1);

        bookAvailabilityRepository.deleteAllByBook(otherBook);

        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(otherBook).count(2).build());

        Order orderToUpdate = createOrder(book);
        orderService.updateOrder(orderToUpdate.getId(),
                Order.builder()
                        .items(orderItemSet)
                        .priceSum(BigDecimal.ONE)
                        .description("new Order description")
                        .build());
    }

    @Test
    @Transactional
    public void testReplaceOrderWithItemSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = bookList.get(1);
        Integer initialCountBook = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        Integer initialCountOtherBook = bookAvailabilityRepository.findByBook(otherBook).map(BookAvailability::getCount).get();
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder().book(otherBook).count(5).build());

        Order orderToUpdate = createOrder(book);
        orderToUpdate = orderService.replaceOrderItems(orderToUpdate.getId(), orderItems);

        assertEquals(1, orderToUpdate.getItems().size());
        assertEquals(5, orderToUpdate.getItems().iterator().next().getCount().intValue());
        assertEquals(otherBook, orderToUpdate.getItems().iterator().next().getBook());
        assertEquals(OrderUtil.calculateOrderSum(orderItems), orderToUpdate.getPriceSum());

        Integer actualCountBook = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        assertEquals(initialCountBook.intValue(), actualCountBook.intValue());

        Integer actualCountOtherBook = bookAvailabilityRepository.findByBook(otherBook).map(BookAvailability::getCount).get();
        assertEquals(initialCountOtherBook.intValue(), actualCountOtherBook + orderToUpdate.getItems().iterator().next().getCount());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testReplaceOrderWithItemOrderNotFoundError() {
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder().book(bookService.listAll().get(0)).count(5).build());
        orderService.replaceOrderItems("wrongId", orderItems);
    }

    @Test(expected = ReplaceOrderItemsException.class)
    @Transactional
    public void testReplaceOrderWithItemStatusError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);
        Book otherBook = bookList.get(1);
        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(OrderItem.builder().book(otherBook).count(5).build());

        Order orderToUpdate = createOrder(book);
        orderService.cancelOrder(orderToUpdate.getId());
        orderService.replaceOrderItems(orderToUpdate.getId(), orderItems);
    }

    @Test
    @Transactional
    public void testDeleteOrderSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Order orderToDelete = createOrder(book);
        Integer initialCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        orderService.deleteOrder(orderToDelete.getId());
        Integer actualCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        assertEquals(actualCount.intValue(), initialCount + orderToDelete.getItems().iterator().next().getCount());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testDeleteOrderNotFoundException() {
        orderService.deleteOrder("wrongId");
    }

    @Test
    @Transactional
    public void testCancelOrderSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Order order = createOrder(book);
        Integer initialCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        order = orderService.cancelOrder(order.getId());
        Integer actualCount = bookAvailabilityRepository.findByBook(book).map(BookAvailability::getCount).get();
        assertEquals(actualCount.intValue(), initialCount + order.getItems().iterator().next().getCount());
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testCancelOrderNotFoundException() {
        orderService.cancelOrder("wrongId");
    }

    @Test(expected = ChangeOrderStatusException.class)
    @Transactional
    public void testCancelOrderChangeStatusError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Order order = createOrder(book);
        orderService.completeOrder(order.getId());
        orderService.cancelOrder(order.getId());
    }

    @Test
    @Transactional
    public void testCompleteOrderSuccess() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Order order = createOrder(book);
        order = orderService.completeOrder(order.getId());
        assertEquals(OrderStatus.COMPLETED, order.getStatus());
    }

    @Test(expected = OrderNotFoundException.class)
    public void testCompleteOrderNotFoundException() {
        orderService.cancelOrder("wrongId");
    }

    @Test(expected = ChangeOrderStatusException.class)
    @Transactional
    public void testCompleteOrderChangeStatusError() {
        List<Book> bookList = bookService.listAll();
        Book book = bookList.get(0);

        Order order = createOrder(book);
        orderService.cancelOrder(order.getId());
        orderService.cancelOrder(order.getId());
    }

    private Order createOrder(Book book) {
        Set<OrderItem> orderItemSet = new HashSet<>();
        orderItemSet.add(OrderItem.builder().book(book).count(1).build());
        return orderService.createOrder(Order.builder()
                .items(orderItemSet)
                .priceSum(BigDecimal.TEN)
                .description("Order description")
                .build());
    }
}
