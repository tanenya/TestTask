package com.commerzbank.task.rest.assembler;

import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.rest.BookAvailabilityController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Test task Commerzbank
 *
 * Adding links for {@link BookAvailability} resource
 *
 * @author vtanenya
 * */

@Component
public class BookAvailabilityResourceAssembler implements SimpleRepresentationModelAssembler<BookAvailability> {

    @Override
    public void addLinks(EntityModel<BookAvailability> resource) {
        if (resource.getContent().getId() != null) {
            resource.add(linkTo(methodOn(BookAvailabilityController.class).getById(resource.getContent().getId())).withSelfRel());
            resource.add(linkTo(methodOn(BookAvailabilityController.class).listAll()).withRel("bookAvailabilities"));
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<BookAvailability>> resources) {
        resources.add(linkTo(methodOn(BookAvailabilityController.class).listAll()).withSelfRel());
    }
}
