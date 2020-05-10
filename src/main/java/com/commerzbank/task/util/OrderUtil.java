package com.commerzbank.task.util;

import com.commerzbank.task.entity.OrderItem;

import java.math.BigDecimal;
import java.util.List;

/**
 * Test task Commerzbank
 *
 * Util class for {@link com.commerzbank.task.entity.Order}
 *
 * @author vtanenya
 * */

public class OrderUtil {
    // calculates sum of order items
    public static BigDecimal calculateOrderSum(List<OrderItem> orderItems) {
        BigDecimal sum = BigDecimal.ZERO;
        for (OrderItem orderItem : orderItems) {
            sum = sum.add(orderItem.getBook().getPrice().multiply(new BigDecimal(orderItem.getCount())));
        }
        return sum;
    }
}
