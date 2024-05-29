package com.example.authentication.controller;

import com.example.authentication.model.SysLog;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.SysLogResponse;
import com.example.authentication.service.SysLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @GetMapping("/syslogs")
    public BaseResponse<List<SysLogResponse>> getFilteredSysLogs(@Valid @RequestBody SysLogRequest request) throws ParseException {
        return BaseResponse.<List<SysLogResponse>>builder()
                .data(sysLogService.filterSysLogsByMonthAndYear(request))
                .build();
    }
}
