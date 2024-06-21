package com.example.authentication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "app_errorLogs")
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "errMessage", columnDefinition = "TEXT")
    private String errMessage;

    @NotNull
//    @Column(name = "errStackTrace", columnDefinition = "LONGTEXT")
    @Column(name = "errStackTrace", columnDefinition = "VARCHAR(MAX)")
    private String errStackTrace;

    @NotNull
    @Column(name = "date")
    private Date date;
}
