package com.example.authentication.response;

import com.example.authentication.model.SysLog;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FilterSysLogByCategoriesResponse {
    private List<SysLog> sysLogs;
    private int totalPages;

}
