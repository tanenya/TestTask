package com.commerzbank.task.rest;

import com.commerzbank.task.entity.Book;
import com.commerzbank.task.exception.BookNotFoundException;
import com.commerzbank.task.repository.BookRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Test task Commerzbank
 *
 * Custom desearializer for {@link Book} entity
 *
 * @author vtanenya
 * */

public class BookDesearilizer extends StdDeserializer<Book> {

    @Autowired
    private BookRepository bookRepository;

    public BookDesearilizer() {
        this(null);
    }

    public BookDesearilizer(Class<?> vc) {
        super(vc);
    }

    @Override
    public Book deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node.get("id") != null) {
            String id = node.get("id").textValue();
            return bookRepository.findByIdAndOutOfStock(id, false)
                    .orElseThrow(() -> new BookNotFoundException(id));
        }
        String name = node.get("name") == null ? null : node.get("name").textValue();
        String author = node.get("author") == null ? null : node.get("author").textValue();
        String description = node.get("description") == null ? null : node.get("description").textValue();
        BigDecimal price = node.get("price") == null ? null : node.get("price").decimalValue();
        return Book.builder().name(name).author(author).description(description).price(price).build();
    }
}
