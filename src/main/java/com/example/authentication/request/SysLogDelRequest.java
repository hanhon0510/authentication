package com.example.authentication.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysLogDelRequest {
    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

}

