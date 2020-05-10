package com.commerzbank.task.rest;

import com.commerzbank.task.entity.Order;
import com.commerzbank.task.entity.OrderItem;
import com.commerzbank.task.rest.assembler.OrderResourceAssembler;
import com.commerzbank.task.service.api.OrderService;
import com.commerzbank.task.util.RestUtil;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Test task Commerzbank
 *
 * Controller for {@link Order} entities manipulation
 *
 * @author vtanenya
 * */

@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    private OrderService orderService;
    private OrderResourceAssembler orderResourceAssembler;

    public OrderController(OrderService orderService,
                           OrderResourceAssembler orderResourceAssembler) {
        this.orderService = orderService;
        this.orderResourceAssembler = orderResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Order>>> listAll() {
        return ResponseEntity.ok(orderResourceAssembler.toCollectionModel(orderService.listAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EntityModel<Order>> getById(@PathVariable String id) {
        return ResponseEntity.ok(orderResourceAssembler.toModel(orderService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order) {
        EntityModel<Order> resource = orderResourceAssembler.toModel(orderService.createOrder(order));
        return RestUtil.getCreatedResponse(resource, "Can not create " + order);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateOrder(@Valid @RequestBody Order order, @PathVariable String id) {
        EntityModel<Order> resource = orderResourceAssembler.toModel(orderService.updateOrder(id, order));
        return RestUtil.getCreatedResponse(resource, "Can not update " + order);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(path = "/{id}/cancel")
    public ResponseEntity<?> cancelOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderResourceAssembler.toModel(orderService.cancelOrder(id)));
    }

    @PostMapping(path = "/{id}/complete")
    public ResponseEntity<?> completeOrder(@PathVariable String id) {
        return ResponseEntity.ok(orderResourceAssembler.toModel(orderService.completeOrder(id)));
    }

    @PostMapping(path = "/{id}/items")
    public ResponseEntity<?> replaceOrderItems(@PathVariable String id, @Valid @RequestBody List<OrderItem> orderItems) {
        EntityModel<Order> resource = orderResourceAssembler.toModel(orderService.replaceOrderItems(id, orderItems));
        return RestUtil.getCreatedResponse(resource, "Can not update order items " + orderItems + "for Order with id " + id);
    }
}
