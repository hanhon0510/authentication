package com.example.authentication.schedulingtasks;

import com.example.authentication.config.ErrorNotificationConfig;
import com.example.authentication.repository.ErrorLogRepository;
import com.example.authentication.utils.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class ScheduledTasks {
    @Autowired
    private ErrorLogRepository errorLogRepository;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private ErrorNotificationConfig errorNotificationConfig;

    // second minute hour day-of-month month day-of-week
//    @Scheduled(cron = "0 0 17 * * ?")
    @Scheduled(cron = "0 10 9 * * ?")
    public void sendErrorLogEmail() {
        LocalDateTime endDate = LocalDateTime.of(LocalDateTime.now().toLocalDate(), LocalTime.of(17, 0));
        LocalDateTime startDate = endDate.minusDays(1);
        int errorCount = errorLogRepository.countErrorLogInADay(startDate, endDate);

        String subject = "Daily Exception Report";
        String text = "Number of exceptions from " + startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                " to " + endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " is: " + errorCount;

        mailUtil.sendEmail(errorNotificationConfig.getEmail(), subject, text);
    }
}


