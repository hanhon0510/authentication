package com.example.authentication.constant;

import com.example.authentication.config.localization.ApplicationContextHolder;
import lombok.Getter;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // 1000001-1999999: Authentication Errors
    /**
     * Authentication Errors: These errors occur when users fail to authenticate or are invalid.
     * Example: Incorrect login credentials, expired session.
     */
    UNAUTHENTICATED(1000001, "unauthenticated", HttpStatus.UNAUTHORIZED),

    // 2000001-2999999: Authorization Errors
    /**
     * Authorization Errors: These errors occur when users lack access to a specific resource.
     * Example: Attempting to access a page restricted to administrators.
     */
    UNAUTHORIZED(2000001, "you_do_not_have_permission", HttpStatus.FORBIDDEN),

    // 3000001-3999999: Service Errors
    /**
     * Service Errors: These errors occur when there is a problem processing requests from the server or any services your application depends on.
     * Example: Server-side processing failure.
     */
    FIELD_DUPLICATE(3000001, "%s_already_exists", HttpStatus.BAD_REQUEST),
    FIELD_NOT_EXISTED(3000002, "%s_does_not_exist", HttpStatus.BAD_REQUEST),
    FIELD_INCORRECT(3000003, "%s_is_incorrect", HttpStatus.BAD_REQUEST),
    LOGIN_INCORRECT(3000004, "%s_or_%s_is_incorrect", HttpStatus.BAD_REQUEST),

    // 4000001-4999999: Formatting Errors
    /**
     * Formatting Errors: These errors occur when input data does not match the expected format.
     * Example: Entering numerical data into a field requiring textual data.
     */
    FIELD_LENGTH_INVALID(4000001, "%s_length_must_be_between_%d_and_%d_characters", HttpStatus.BAD_REQUEST),
    FIELD_MIN_INVALID(4000002, "%s_length_must_be_at_least_%d_characters", HttpStatus.BAD_REQUEST),
    FIELD_MAX_INVALID(4000003, "%s_length_must_be_at_most_%d_characters", HttpStatus.BAD_REQUEST),
    FIELD_NOT_NULL(4000004, "%s_must_not_be_null", HttpStatus.BAD_REQUEST),
    FIELD_NOT_EMPTY(4000005, "%s_must_not_be_null_or_empty", HttpStatus.BAD_REQUEST),
    FIELD_NOT_BLANK(4000006, "%s_must_not_be_null_empty_or_whitespace", HttpStatus.BAD_REQUEST),

    // 5000001-5999999: Query Errors
    /**
     * Query Errors: These errors occur when there is an issue with querying the database, such as an invalid query or the database being unavailable.
     * Example: Invalid database query.
     */

    // 6000000-6999999: Network Errors
    /**
     * Network Errors: These errors occur when there is a problem sending or receiving data over the network, such as lost connections or excessively long connection times.
     * Example: Lost network connection.
     */

    // 7000000-7999999: Access Resources Errors
    /**
     * Access Resources Errors: Resource access errors occur when accessing the wrong path or cors error,...
     * Example: Resources are not available.
     */
    // Add this in your ErrorCode enum
    NOT_FOUND(7000001, "resource_not_found", HttpStatus.NOT_FOUND),


    // 9000000-9999999: Undefined Errors
    /**
     * Undefined Errors: Errors that cannot be categorized into specific types.
     * Example: General unknown error.
     */
    UNCATEGORIZED_EXCEPTION(9999999, "uncategorized_error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(9000001, "invalid_key", HttpStatus.INTERNAL_SERVER_ERROR);

    private final int code;
    private final String messageKey;
    private final HttpStatus statusCode;

    ErrorCode(int code, String messageKey, HttpStatus statusCode) {
        this.code = code;
        this.messageKey = messageKey;
        this.statusCode = statusCode;
    }

    /**
     * Set message arguments for dynamic message formatting.
     *
     * @param args the arguments to be replaced in the message template
     * @return the formatted message
     */
    public String getMessage(Object... args) {
        MessageSource messageSource = ApplicationContextHolder.getContext().getBean(MessageSource.class);
        String message = messageSource.getMessage(messageKey, args, LocaleContextHolder.getLocale());
        return String.format(message, args);
    }
}