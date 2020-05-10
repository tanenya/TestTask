package com.commerzbank.task.repository;

import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Test task Commerzbank
 *
 * Repository for {@link OrderItem}
 *
 * @author vtanenya
 * */

public interface OrderItemRepository extends JpaRepository<OrderItem, String> {
    List<OrderItem> findAllByOrderId(String orderId);
    Long deleteAllByOrder(Order order);
}
