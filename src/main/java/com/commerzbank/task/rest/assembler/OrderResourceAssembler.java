package com.commerzbank.task.rest.assembler;

import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderStatus;
import com.commerzbank.task.rest.OrderController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Test task Commerzbank
 *
 * Adding links for {@link Order} resource
 *
 * @author vtanenya
 * */

@Component
public class OrderResourceAssembler implements SimpleRepresentationModelAssembler<Order> {

    @Override
    public void addLinks(EntityModel<Order> resource) {
        Order order = resource.getContent();
        if (order.getId() != null) {
            resource.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withSelfRel());
            resource.add(linkTo(methodOn(OrderController.class).listAll()).withRel("orders"));

            if (resource.getContent().getStatus() == OrderStatus.PROCESSING) {
                resource.add(linkTo(methodOn(OrderController.class)
                                .cancelOrder(order.getId())).withRel("cancel"));
                resource.add(linkTo(methodOn(OrderController.class)
                                .completeOrder(order.getId())).withRel("complete"));
            }
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Order>> resources) {
        resources.add(linkTo(methodOn(OrderController.class).listAll()).withSelfRel());
    }
}