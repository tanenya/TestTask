package com.commerzbank.task.exception;

/**
 * Test task Commerzbank
 *
 * Throws when token expired or invalid
 *
 * @author vtanenya
 * */

public class InvalidJwtAuthenticationException extends RuntimeException {
    public InvalidJwtAuthenticationException() {}

    public InvalidJwtAuthenticationException(String e) {
        super(e);
    }
}
