package com.commerzbank.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Test task Commerzbank
 *
 * Entity representing availability of {@link Book}
 *
 * @author vtanenya
 * */

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "book_availability")
public class BookAvailability extends BaseEntity {

    @NotNull
    @PositiveOrZero
    @Column(name = "count")
    private Integer count;

    @NotNull
    @OneToOne
    @JoinColumn(name = "book_id")
    @JsonIgnoreProperties({"name", "author", "description", "price", "outOfStock", "createdDate", "lastModifiedDate"})
    private Book book;

}
