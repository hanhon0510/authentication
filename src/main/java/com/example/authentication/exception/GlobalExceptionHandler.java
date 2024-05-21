package com.example.authentication.exception;

import com.example.authentication.constant.ErrorCode;
import com.example.authentication.model.ErrorLog;
import com.example.authentication.repository.ErrorLogRepository;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.utils.ErrorLogUtil;
import com.example.authentication.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ErrorLogRepository errorLogRepository;

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
    public ResponseEntity<BaseResponse> handleGenericException(Exception ex, WebRequest request) {
        mailUtil.sendEmail("21021494@vnu.edu.vn", "Error", ex.getMessage());


        errorLogUtil.logError(ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                BaseResponse.builder()
                        .code(HttpStatus.BAD_REQUEST.value())
                        .message("An unexpected error occurred")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

}
