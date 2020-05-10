package com.commerzbank.task.rest;

import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.rest.assembler.BookAvailabilityResourceAssembler;
import com.commerzbank.task.service.api.BookAvailabilityService;
import com.commerzbank.task.util.RestUtil;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Test task Commerzbank
 *
 * Controller for {@link BookAvailability} entities manipulation
 *
 * @author vtanenya
 * */

@RestController
@RequestMapping("/bookAvailability")
public class BookAvailabilityController {

    private BookAvailabilityService bookAvailabilityService;
    private BookAvailabilityResourceAssembler bookAvailabilityResourceAssembler;

    public BookAvailabilityController(BookAvailabilityService bookAvailabilityService,
                                      BookAvailabilityResourceAssembler bookAvailabilityResourceAssembler) {
        this.bookAvailabilityService = bookAvailabilityService;
        this.bookAvailabilityResourceAssembler = bookAvailabilityResourceAssembler;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<BookAvailability>>> listAll() {
        return ResponseEntity.ok(bookAvailabilityResourceAssembler.toCollectionModel(bookAvailabilityService.listAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EntityModel<BookAvailability>> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookAvailabilityResourceAssembler.toModel(bookAvailabilityService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createBookAvailability(@Valid @RequestBody BookAvailability bookAvailability) {
        EntityModel<BookAvailability> resource = bookAvailabilityResourceAssembler.toModel(
                bookAvailabilityService.createBookAvailability(bookAvailability));
        return RestUtil.getCreatedResponse(resource, "Can not create " + bookAvailability);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateBookAvailability(@Valid @RequestBody BookAvailability bookAvailability, @PathVariable String id) {
        EntityModel<BookAvailability> resource = bookAvailabilityResourceAssembler.toModel(
                bookAvailabilityService.updateBookAvailability(id, bookAvailability));
        return RestUtil.getCreatedResponse(resource, "Can not update " + bookAvailability);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteBookAvailability(@PathVariable String id) {
        bookAvailabilityService.deleteBookAvailability(id);
        return ResponseEntity.noContent().build();
    }

}
