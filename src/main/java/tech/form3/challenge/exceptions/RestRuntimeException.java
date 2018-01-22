package tech.form3.challenge.exceptions;

/**
 * Generic REST exception.
 */
public abstract class RestRuntimeException extends RuntimeException {

    private final String code;

    public RestRuntimeException(String message) {
        this(null, message, null);
    }

    public RestRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
