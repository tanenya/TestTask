package com.commerzbank.task.rest;

import com.commerzbank.task.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Test task Commerzbank
 *
 * Handles custom exceptions thrown in controllers
 *
 * @author vtanenya
 * */

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(value = {BookNotFoundException.class})
    public ResponseEntity<?> bookNotFound(BookNotFoundException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Not found", ex.getMessage()));
    }

    @ExceptionHandler(value = {BookAvailabilityNotFoundException.class})
    public ResponseEntity<?> bookAvailabilityNotFound(BookAvailabilityNotFoundException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Not found", ex.getMessage()));
    }

    @ExceptionHandler(value = {OrderNotFoundException.class})
    public ResponseEntity<?> orderNotFound(OrderNotFoundException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new VndErrors.VndError("Not found", ex.getMessage()));
    }

    @ExceptionHandler(value = {ChangeOrderStatusException.class})
    public ResponseEntity<?> changeOrderStatusNotAllowed(ChangeOrderStatusException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new VndErrors.VndError("Method not allowed", ex.getMessage()));
    }

    @ExceptionHandler(value = {OrderItemsEmptyException.class})
    public ResponseEntity<?> orderItemsAreEmpty(OrderItemsEmptyException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new VndErrors.VndError("Bad request", ex.getMessage()));
    }

    @ExceptionHandler(value = {BookUnavailableException.class})
    public ResponseEntity<?> bookUnavailable(BookUnavailableException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new VndErrors.VndError("Bad request", ex.getMessage()));
    }

    @ExceptionHandler(value = {BookAvailabilityDuplicateException.class})
    public ResponseEntity<?> bookAvailabilityDuplicate(BookAvailabilityDuplicateException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new VndErrors.VndError("Bad request", ex.getMessage()));
    }

    @ExceptionHandler(value = {ReplaceOrderItemsException.class})
    public ResponseEntity<?> replaceOrderItemsNotAllowed(ReplaceOrderItemsException ex, WebRequest request) {
        log.debug(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new VndErrors.VndError("Bad request", ex.getMessage()));
    }
}
