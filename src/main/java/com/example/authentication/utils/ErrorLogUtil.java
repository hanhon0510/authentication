package com.example.authentication.utils;

import com.example.authentication.model.ErrorLog;
import com.example.authentication.repository.ErrorLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class ErrorLogUtil {

    @Autowired
    private ErrorLogRepository errorLogRepository;
    public void logError(Exception ex) {
        String s = Arrays.toString(ex.getStackTrace());
        ErrorLog errorLog = ErrorLog.builder()
                .errMessage(ex.getMessage())
                .errStackTrace(s)
                .date(new Date())
                .build();

        errorLogRepository.save(errorLog);
    }
}
