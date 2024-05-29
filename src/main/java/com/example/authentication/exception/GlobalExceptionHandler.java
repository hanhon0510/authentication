package com.example.authentication.exception;

import com.example.authentication.config.ErrorNotificationConfig;
import com.example.authentication.config.localization.MyLocalResolver;
import com.example.authentication.constant.ErrorCode;
import com.example.authentication.model.ErrorLog;
import com.example.authentication.repository.ErrorLogRepository;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.utils.ErrorLogUtil;
import com.example.authentication.utils.MailUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.LocaleResolver;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private MyLocalResolver myLocalResolver;
    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ErrorNotificationConfig errorNotificationConfig;

    @Autowired
    private MailUtil mailUtil;
    @Autowired
    private ErrorLogUtil errorLogUtil;
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<BaseResponse> handleAppException(AppException exception) {
        return ResponseEntity.status(exception.getStatusCode()).body(
                BaseResponse.builder()
                        .code(exception.getStatusCode())
                        .message(exception.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse> handleValidationException(MethodArgumentNotValidException exception) {
        return ResponseEntity.badRequest().body(
                BaseResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("Validation error")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<BaseResponse> handleGenericException(Exception ex, HttpServletRequest request) {

        Locale locale = myLocalResolver.resolveLocale(request);
        //mailUtil.sendEmail(errorNotificationConfig.getEmail(), errorNotificationConfig.getSubject(), ex.getMessage());

        errorLogUtil.logError(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message(messageSource.getMessage("generalException", null, locale))
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
