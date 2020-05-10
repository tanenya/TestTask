package com.commerzbank.task.service.api;

import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderItem;

import java.util.List;

/**
 * Test task Commerzbank
 *
 * Interface for {@link Order} service
 *
 * @author vtanenya
 * */

public interface OrderService {
    List<Order> listAll();
    Order getById(String id);
    Order createOrder(Order order);
    Order updateOrder(String id, Order order);
    void deleteOrder(String id);
    Order cancelOrder(String id);
    Order completeOrder(String id);
    Order replaceOrderItems(String id, List<OrderItem> orderItems);
}
