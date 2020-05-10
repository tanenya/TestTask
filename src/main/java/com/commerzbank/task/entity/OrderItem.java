package com.commerzbank.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

/**
 * Test task Commerzbank
 *
 * Entity representing item in {@link Order}
 *
 * @author vtanenya
 * */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item")
public class OrderItem extends BaseEntity {

    @JsonIgnore
    private String id;

    @JsonIgnore
    protected LocalDate createdDate;

    @JsonIgnore
    protected LocalDate lastModifiedDate;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"name", "author", "description", "price", "outOfStock", "createdDate", "lastModifiedDate"})
    private Book book;

    @NotNull
    @PositiveOrZero
    @Column(name = "count")
    private Integer count;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id")
    private Order order;
}
