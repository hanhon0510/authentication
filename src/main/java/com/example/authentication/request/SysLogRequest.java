package com.example.authentication.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysLogRequest {
    @NotNull
    private int startMonth;

    @NotNull
    private int endMonth;

    @NotNull
    private int startYear;

    @NotNull
    private int endYear;

    @NotNull
    private String method;
}
