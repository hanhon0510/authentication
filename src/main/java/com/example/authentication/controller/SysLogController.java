package com.example.authentication.controller;

import com.example.authentication.model.SysLog;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.SysLogDelResponse;
import com.example.authentication.response.SysLogResponse;
import com.example.authentication.service.CSVService;
import com.example.authentication.service.SysLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.StringWriter;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.apache.tomcat.util.http.FastHttpDateFormat.parseDate;

@RestController
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private CSVService csvService;

    @GetMapping("/syslogs")
    public List<Object[]> getFilteredSysLogs(@Valid @RequestBody SysLogRequest request) throws ParseException {
        return sysLogService.filterSysLogsByMonthAndYear(request);
    }

    @DeleteMapping("/delete/syslogs")
    public BaseResponse<SysLogDelResponse> deleteSysLogs(@Valid @RequestBody SysLogDelRequest request) throws ParseException {
        return BaseResponse.<SysLogDelResponse>builder()
                .code(200)
                .message("Deleted")
                .data(sysLogService.deleteSysLogsFromArg1toArg2(request))
                .build();
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportCSV() {
        List<SysLog> sysLogs = sysLogService.getSysLogs();

        StringWriter writer = new StringWriter();
        try {
            csvService.writeSysLogsToCsv(writer, sysLogs);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String csvContent = writer.toString();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=syslogs.csv");
        headers.setContentType(MediaType.TEXT_PLAIN);

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
