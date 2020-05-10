package com.commerzbank.task.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * Test task Commerzbank
 *
 * Entity representing order
 *
 * @author vtanenya
 * */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "order_table")
public class Order extends BaseEntity {

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.PROCESSING;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
    private Set<OrderItem> items = new HashSet<>();

    @PositiveOrZero
    @Column(name = "price_sum")
    private BigDecimal priceSum;

    @Column(name = "description")
    private String description;
}
