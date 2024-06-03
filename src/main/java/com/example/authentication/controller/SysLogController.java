package com.example.authentication.controller;

import com.example.authentication.exception.AppException;
import com.example.authentication.model.SysLog;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.SysLogDelResponse;
import com.example.authentication.response.SysLogResponse;
import com.example.authentication.service.CSVService;
import com.example.authentication.service.PDFService;
import com.example.authentication.service.SysLogService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
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

    @Autowired
    private PDFService pdfService;

    @GetMapping("/syslogs")
    public List<SysLogResponse> getFilteredSysLogs(@Valid @RequestBody SysLogRequest request) throws ParseException {
        return sysLogService.filterSysLogsByMonthAndYear(request);
    }

    @DeleteMapping("/delete/syslogs")
    public BaseResponse<SysLogDelResponse> deleteSysLogs(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                         @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) throws ParseException {
        SysLogDelRequest request = new SysLogDelRequest(startDate, endDate);
        return BaseResponse.<SysLogDelResponse>builder()
                .code(200)
                .message("Deleted")
                .data(sysLogService.deleteSysLogsFromArg1toArg2(request))
                .build();
    }

    @GetMapping("/export")
    public ResponseEntity<?> export(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam("fileType") String fileType
                                            ) throws ParseException, IOException {
        SysLogDelRequest request = new SysLogDelRequest(startDate, endDate);
        List<SysLog> sysLogs = sysLogService.getSysLogs(request);

        if (fileType.equalsIgnoreCase("csv")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (Writer writer = new OutputStreamWriter(outputStream)) {
                csvService.writeSysLogsToCsv(writer, sysLogs);
            } catch (IOException e) {
                e.printStackTrace();
            }

            byte[] csvContent = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=syslogs.csv");
            headers.setContentType(MediaType.parseMediaType("text/csv"));
            headers.setContentLength(csvContent.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(new ByteArrayResource(csvContent));
        } else if (fileType.equalsIgnoreCase("pdf")) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pdfService.writeSysLogsToPdf(outputStream, sysLogs);

            byte[] pdfContent = outputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=syslogs.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfContent);
        } else {
            throw new AppException(400 ,"Not supported file type");
        }

    }
}
