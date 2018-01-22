package tech.form3.challenge.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a bad request is detected.
 */
public class BadArgumentException extends RestRuntimeException {

    public BadArgumentException(String message) {
        super(HttpStatus.BAD_REQUEST.toString(), message, null);
    }

}
