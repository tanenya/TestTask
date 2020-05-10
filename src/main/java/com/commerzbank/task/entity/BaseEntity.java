package com.commerzbank.task.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Test task Commerzbank
 *
 * Base identifiable entity with auditing
 *
 * @author vtanenya
 * */

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    @Column(name = "id")
    protected String id;

    @CreatedDate
    protected LocalDate createdDate;

    @LastModifiedDate
    protected LocalDate lastModifiedDate;

    @JsonIgnore
    @CreatedBy
    @ManyToOne
    @JoinColumn(name = "created_by")
    protected User createdBy;

    @JsonIgnore
    @LastModifiedBy
    @ManyToOne
    @JoinColumn(name = "last_modified_by")
    protected User lastModifiedBy;
}
