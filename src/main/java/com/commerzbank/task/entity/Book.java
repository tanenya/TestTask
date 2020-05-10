package com.commerzbank.task.entity;

import com.commerzbank.task.rest.BookDesearilizer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * Test task Commerzbank
 *
 * Entity representing book
 *
 * @author vtanenya
 * */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book")
@JsonDeserialize(using = BookDesearilizer.class)
public class Book extends BaseEntity {

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "author")
    private String author;

    @Column(name = "description")
    private String description;

    @NotNull
    @PositiveOrZero
    @Column(name = "price")
    private BigDecimal price;

    @NotNull
    @Builder.Default
    @Column(name = "out_of_stock")
    private Boolean outOfStock = false;
}
