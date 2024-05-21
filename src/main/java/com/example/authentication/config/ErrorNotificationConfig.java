package com.example.authentication.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "error.notification")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ErrorNotificationConfig {
    private String email;
    private String subject;
}
