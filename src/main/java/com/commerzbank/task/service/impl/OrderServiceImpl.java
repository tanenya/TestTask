package com.commerzbank.task.service.impl;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderItem;
import com.commerzbank.task.entity.OrderStatus;
import com.commerzbank.task.exception.*;
import com.commerzbank.task.repository.BookAvailabilityRepository;
import com.commerzbank.task.repository.BookRepository;
import com.commerzbank.task.repository.OrderItemRepository;
import com.commerzbank.task.repository.OrderRepository;
import com.commerzbank.task.service.api.OrderService;
import com.commerzbank.task.util.OrderUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Test task Commerzbank
 *
 * Implementation of {@link OrderService}
 *
 * @author vtanenya
 * */

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private BookRepository bookRepository;
    private BookAvailabilityRepository bookAvailabilityRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                            BookRepository bookRepository, BookAvailabilityRepository bookAvailabilityRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.bookRepository = bookRepository;
        this.bookAvailabilityRepository = bookAvailabilityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(String id) {
        return orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    }

    @Override
    @Transactional
    public Order createOrder(Order order) {
        Set<OrderItem> orderItems = new HashSet<>(order.getItems());
        order.getItems().clear();
        Order createdOrder = orderRepository.save(order);
        createItemsAndAddToOrder(orderItems, createdOrder);
        return orderRepository.save(createdOrder);
    }

    @Override
    @Transactional
    public Order updateOrder(String id, Order order) {
        Order orderToUpdate = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        // update items when order is not in PROCESSING status is prohibited
        if (order.getStatus() != OrderStatus.PROCESSING)
            throw new ReplaceOrderItemsException(id);
        increaseBookAvailability(orderToUpdate);
        createItemsAndAddToOrder(order.getItems(), orderToUpdate);
        orderToUpdate.setPriceSum(order.getPriceSum());
        orderToUpdate.setDescription(order.getDescription());
        return orderRepository.save(orderToUpdate);
    }

    @Override
    @Transactional
    public void deleteOrder(String id) {
        Order orderToDelete = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        increaseBookAvailability(orderToDelete);
        orderRepository.delete(orderToDelete);
    }

    @Override
    @Transactional
    public Order cancelOrder(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        // change status of order which is not in PROCESSING status is prohibited
        if (order.getStatus() != OrderStatus.PROCESSING)
            throw new ChangeOrderStatusException(order.getStatus().name(), OrderStatus.CANCELLED.name());

        increaseBookAvailability(order);
        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order completeOrder(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        // change status of order which is not in PROCESSING status is prohibited
        if (order.getStatus() != OrderStatus.PROCESSING)
            throw new ChangeOrderStatusException(order.getStatus().name(), OrderStatus.COMPLETED.name());

        order.setStatus(OrderStatus.COMPLETED);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order replaceOrderItems(String id, List<OrderItem> orderItems) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
        // update items when order is not in PROCESSING status is prohibited
        if (order.getStatus() != OrderStatus.PROCESSING)
            throw new ReplaceOrderItemsException(id);
        increaseBookAvailability(order);
        createItemsAndAddToOrder(new HashSet<>(orderItems), order);
        order.setPriceSum(OrderUtil.calculateOrderSum(orderItems));
        return orderRepository.save(order);
    }

    // creates and replaces order items in order
    private void createItemsAndAddToOrder(Set<OrderItem> orderItems, Order order) {
        if (orderItems.isEmpty())
            throw new OrderItemsEmptyException(order.getId());

        // removing of older items
        orderItemRepository.deleteAllByOrder(order);
        order.getItems().clear();

        orderItems.forEach(orderItem -> {
             bookRepository.findByIdAndOutOfStock(orderItem.getBook().getId(), false).map(book -> {
                 validateBookAvailability(book, orderItem);
                 orderItem.setBook(book);
                 orderItem.setOrder(order);
                 return orderItemRepository.save(orderItem);
             })
                    .orElseThrow(() -> new BookNotFoundException(orderItem.getBook().getId()));
        });

        order.getItems().addAll(orderItemRepository.findAllByOrderId(order.getId()));
    }

    // validating of book availabilities for each order item
    private void validateBookAvailability(Book book, OrderItem orderItem) {
        bookAvailabilityRepository.findByBook(book).map(bookAvailability -> {
            Integer booksRequested = orderItem.getCount();
            Integer booksAvailable = bookAvailability.getCount();
            // if count of available amount is less than requested we can not process the order
            if (booksRequested > booksAvailable)
                throw new BookUnavailableException(book.getId(), booksRequested, booksAvailable);
            // setting remaining amount after order processing
            bookAvailability.setCount(booksAvailable - booksRequested);
            return bookAvailabilityRepository.save(bookAvailability);
        })
                .orElseThrow(() -> new BookAvailabilityNotFoundException(book));

    }

    // setting actual availability count when order being updated/deleted/cancelled
    // or order items being replaced
    private void increaseBookAvailability(Order order) {
        order.getItems().forEach(orderItem -> {
            bookAvailabilityRepository.findByBook(orderItem.getBook())
                    .ifPresent( bookAvailability -> {
                        // setting remaining amount after old order items removing
                        bookAvailability.setCount(bookAvailability.getCount() + orderItem.getCount());
                        bookAvailabilityRepository.save(bookAvailability);
                    });
        });
    }
}
