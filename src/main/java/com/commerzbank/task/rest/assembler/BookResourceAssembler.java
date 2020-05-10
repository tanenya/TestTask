package com.commerzbank.task.rest.assembler;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.entity.BookAvailability;
import com.commerzbank.task.rest.BookController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

/**
 * Test task Commerzbank
 *
 * Adding links for {@link Book} resource
 *
 * @author vtanenya
 * */

@Component
public class BookResourceAssembler implements SimpleRepresentationModelAssembler<Book> {

    @Override
    public void addLinks(EntityModel<Book> resource) {
        if (resource.getContent().getId() != null) {
            resource.add(linkTo(methodOn(BookController.class).getById(resource.getContent().getId())).withSelfRel());
            resource.add(linkTo(methodOn(BookController.class).listAll()).withRel("books"));
        }
    }

    @Override
    public void addLinks(CollectionModel<EntityModel<Book>> resources) {
        resources.add(linkTo(methodOn(BookController.class).listAll()).withSelfRel());
    }
}
