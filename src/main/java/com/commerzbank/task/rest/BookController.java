package com.commerzbank.task.rest;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.rest.assembler.BookResourceAssembler;
import com.commerzbank.task.service.api.BookService;
import com.commerzbank.task.util.RestUtil;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Test task Commerzbank
 *
 * Controller for {@link Book} entities manipulation
 *
 * @author vtanenya
 * */

@RestController
@RequestMapping("/book")
public class BookController {

    private BookService bookService;
    private BookResourceAssembler bookResourceAssembler;

    public BookController(BookResourceAssembler bookResourceAssembler, BookService bookService) {
        this.bookResourceAssembler = bookResourceAssembler;
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<EntityModel<Book>>> listAll() {
        return ResponseEntity.ok(bookResourceAssembler.toCollectionModel(bookService.listAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<EntityModel<Book>> getById(@PathVariable String id) {
        return ResponseEntity.ok(bookResourceAssembler.toModel(bookService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody Book book) {
        EntityModel<Book> resource = bookResourceAssembler.toModel(bookService.createBook(book));
        return RestUtil.getCreatedResponse(resource, "Can not create " + book);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updateBook(@Valid @RequestBody Book book, @PathVariable String id) {
        EntityModel<Book> resource = bookResourceAssembler.toModel(bookService.updateBook(id, book));
        return RestUtil.getCreatedResponse(resource, "Can not update " + book);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
