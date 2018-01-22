package tech.form3.challenge.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.CaseFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.NestedRuntimeException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.form3.challenge.dto.ErrorBody;
import tech.form3.challenge.exceptions.BadArgumentException;
import tech.form3.challenge.exceptions.ResourceNotFoundException;
import tech.form3.challenge.exceptions.UnprocessableEntityException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.io.IOException;

import static java.util.Optional.ofNullable;

/**
 * Handler to manage REST exceptions.
 */
@Slf4j
@ControllerAdvice
public class RestExceptionHandler implements EnvironmentAware {

    private static final String DEFAULT_INTERNAL_ERROR_MESSAGE = "An internal error occurred on processing request.";

    private Environment environment;

    @Value("${spring.application.name}")
    private String applicationName;

    private static String toErrorCode(String errorPrefix, String fieldName) {

        return errorPrefix + ofNullable(fieldName)
                .filter(StringUtils::hasText)
                .map(RestExceptionHandler::lowerCamelToSnakeCase)
                .map(errorFieldName -> "." + errorFieldName)
                .orElse(null);
    }

    private static ErrorBody parseErrorBody(String body) throws IOException {
        return new ObjectMapper().readValue(body, ErrorBody.class);
    }

    private static String lowerCamelToSnakeCase(String label) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, label);
    }

    private static String upperCamelToSnakeCase(String label) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, label);
    }

    private static ResponseEntity<ErrorBody> toErrorResponse(ErrorBody errorBody) {
        return new ResponseEntity<>(errorBody, ofNullable(errorBody.getStatus()).map(HttpStatus::valueOf).orElse(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleResourceNotFoundException(HttpServletRequest req, ResourceNotFoundException e) {
        log.error("Resource not found : ", e);
        return toErrorResponse(ErrorBody.builder()
                .code(e.getCode())
                .message(HttpStatus.NOT_FOUND.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.NOT_FOUND.value())
                .build());

    }

    @ExceptionHandler(NestedRuntimeException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleNestedRuntimeException(HttpServletRequest req, NestedRuntimeException e) {
        log.error("Nested runtime exception : ", e);

        return ofNullable(e.getMostSpecificCause())
                .filter(cause -> ConstraintViolationException.class.isAssignableFrom(cause.getClass()))
                .map(ConstraintViolationException.class::cast)
                .map(cause -> handleConstraintViolationException(req, cause))
                .orElseGet(() -> handleException(req, e));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleConstraintViolationException(HttpServletRequest req, ConstraintViolationException e) {
        log.error("Constraint violation errors : ", e);

        return toErrorResponse(ErrorBody.builder()
                .code("error.invalid")
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleMessageNotReadableException(HttpServletRequest req, HttpMessageNotReadableException e) {
        log.error("Message not readable : ", e);
        return toErrorResponse(ErrorBody.builder()
                .code("error.not_readable_json")
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleArgumentTypeMismatchException(HttpServletRequest req, MethodArgumentTypeMismatchException e) {
        log.error("Argument type mismatch : ", e);
        return toErrorResponse(ErrorBody.builder()
                .code("error.argument_type_mismatch")
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(value = BadArgumentException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleBadRequestException(HttpServletRequest req, BadArgumentException e) {
        log.error("Bad argument : ", e);
        return toErrorResponse(ErrorBody.builder()
                .code(e.getCode())
                .message(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.BAD_REQUEST.value())
                .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleMethodNotSupportedException(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        log.error("Method not supported : ", e);
        return toErrorResponse(ErrorBody.builder()
                .code("error.method_not_allowed")
                .message(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.METHOD_NOT_ALLOWED.value())
                .build());
    }

    @ExceptionHandler(UnprocessableEntityException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleUnprocessableEntityException(HttpServletRequest req, UnprocessableEntityException e) {
        log.error("Unprocessable entity : ", e);

        return toErrorResponse(ErrorBody.builder()
                .code(e.getCode())
                .message(HttpStatus.UNPROCESSABLE_ENTITY.getReasonPhrase())
                .description(e.getMessage())
                .service(buildServiceName(req))
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .build());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleHttpException(HttpServletRequest req, HttpClientErrorException e) {
        log.error("NinthGate OkHttp call error", e);

        ErrorBody errorBody;
        try {
            errorBody = parseErrorBody(e.getResponseBodyAsString());
        } catch (Exception e1) {
            log.error("Unable to read httpException body as an ErrorBody", e);
            return handleException(req, e);
        }

        HttpStatus status = ofNullable(e.getStatusCode()).orElse(HttpStatus.INTERNAL_SERVER_ERROR);

        return toErrorResponse(ErrorBody.builder()
                .code(errorBody.getCode())
                .message(status.getReasonPhrase())
                .description(errorBody.getDescription())
                .service(errorBody.getService())
                .status(status.value())
                .build());
    }

    @ExceptionHandler(value = Throwable.class)
    @ResponseBody
    public ResponseEntity<ErrorBody> handleException(HttpServletRequest req, Throwable e) {
        log.error("Unexpected error : ", e);
        ResponseStatus responseStatus = AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);

        HttpStatus status = ofNullable(responseStatus).map(ResponseStatus::value).orElse(HttpStatus.INTERNAL_SERVER_ERROR);
        String message = ofNullable(responseStatus).map(ResponseStatus::reason).orElseGet(() -> {
            return e.getMessage() != null ? e.getMessage() : DEFAULT_INTERNAL_ERROR_MESSAGE;
        });

        return toErrorResponse(ErrorBody.builder()
                .code("error." + upperCamelToSnakeCase(e.getClass().getSimpleName()))
                .message(status.getReasonPhrase())
                .description(message)
                .service(buildServiceName(req))
                .status(status.value())
                .build());
    }

    private String buildServiceName(HttpServletRequest req) {
        return req == null ? applicationName : applicationName + " : " + req.getMethod() + " " + req.getRequestURI();
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
