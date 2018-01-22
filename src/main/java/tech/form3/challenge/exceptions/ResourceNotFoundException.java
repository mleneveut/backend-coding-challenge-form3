package tech.form3.challenge.exceptions;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a resource is not found.
 */
public class ResourceNotFoundException extends RestRuntimeException {

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND.toString(), message, null);
    }

    public ResourceNotFoundException(String code, String message) {
        super(code, message, null);
    }

    public ResourceNotFoundException(String code, String message, Throwable cause) {
        super(code, message, cause);
    }

}
