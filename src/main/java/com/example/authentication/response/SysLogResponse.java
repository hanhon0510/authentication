package com.example.authentication.response;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SysLogResponse {
    private String yearMonth;
    private Integer count;
}