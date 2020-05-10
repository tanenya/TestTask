package com.commerzbank.task;

import com.commerzbank.task.entity.*;
import com.commerzbank.task.repository.*;
import com.commerzbank.task.util.OrderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Test task Commerzbank
 *
 * Initializes sample data for application
 *
 * @author vtanenya
 * */

@Slf4j
@Component
public class SampleDataInitializer implements CommandLineRunner {

    private BookRepository bookRepository;
    private BookAvailabilityRepository bookAvailabilityRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public SampleDataInitializer(BookRepository bookRepository, BookAvailabilityRepository bookAvailabilityRepository,
                                 OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                                 UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.bookRepository = bookRepository;
        this.bookAvailabilityRepository = bookAvailabilityRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        log.debug("Sample data initializing...");
        bookAvailabilityRepository.deleteAll();
        orderRepository.deleteAll();
        bookRepository.deleteAll();

        log.debug("Books initializing...");
        IntStream.range(0,3).forEach(i -> bookRepository.save(Book.builder().name("Book " + i)
                .author("author of Book " + i).price(new BigDecimal(3 * (i + 1))).description("Description for Book " + i).build()));

        log.debug("Books created:");
        List<Book> books = bookRepository.findAll();
        books.forEach(book -> log.debug(book.toString()));

        log.debug("BookAvailabilities initializing...");
        books.forEach(book -> bookAvailabilityRepository.save(
                BookAvailability.builder().book(book).count(10).build()));
        log.debug("ItemAvailabilities created:");
        bookAvailabilityRepository.findAll().forEach(bookAvailability -> log.debug(bookAvailability.toString()));

        log.debug("Orders initializing...");
        IntStream.range(0,3).forEach(i -> orderRepository.save(Order.builder()
                .description("Description for Order " + i).build()));
        List<Order> orders = orderRepository.findAll();

        orders.forEach(order -> {
            IntStream.range(0,3).forEach(i -> orderItemRepository.save(
                    OrderItem.builder().book(books.get(i)).count(2 * (i + 1)).order(order).build()));
            List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(order.getId());
            order.getItems().addAll(orderItems);
            order.setPriceSum(OrderUtil.calculateOrderSum(orderItems));
            orderRepository.save(order);
        });

        log.debug("Orders created:");
        orderRepository.findAll().forEach(order -> log.debug(order.toString()));

        log.debug("Users initializing...");
        userRepository.deleteAll();
        userRepository.save(User.builder()
                .username("user")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList( "ROLE_USER"))
                .build()
        );

        userRepository.save(User.builder()
                .username("admin")
                .password(this.passwordEncoder.encode("password"))
                .roles(Arrays.asList("ROLE_USER", "ROLE_ADMIN"))
                .build()
        );

        log.debug("Users created:");
        userRepository.findAll().forEach(v -> log.debug(" User :" + v.toString()));

        log.debug("Data initializing finished");
    }
}
