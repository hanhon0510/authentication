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
public class SysLogDelRequest {
    @NotNull
    private String startDate;

    @NotNull
    private String endDate;

}

