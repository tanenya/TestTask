package com.commerzbank.task.repository;

import com.commerzbank.task.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Test task Commerzbank
 *
 * Repository for {@link Order}
 *
 * @author vtanenya
 * */

public interface OrderRepository extends JpaRepository<Order, String> {
}
