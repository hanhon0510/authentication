package com.example.authentication.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysLogResponse {
    private String yearMonth;
    private Long count;
}