package com.example.authentication.controller;

import com.example.authentication.exception.AppException;
import com.example.authentication.model.SysLog;
import com.example.authentication.request.FilterSysLogRequest;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.BaseResponse;
import com.example.authentication.response.FilterSysLogByCategoriesResponse;
import com.example.authentication.response.SysLogDelResponse;
import com.example.authentication.response.SysLogResponse;
import com.example.authentication.service.CSVService;
import com.example.authentication.service.PDFService;
import com.example.authentication.service.SysLogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RestController
public class SysLogController {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private CSVService csvService;

    @Autowired
    private PDFService pdfService;

    @PostMapping("/syslogs")
    public List<SysLogResponse> getCountSysLogs(@Valid @RequestBody SysLogRequest request) throws ParseException {
        return sysLogService.filterSysLogsByMonthAndYear(request);
    }

    @PostMapping("/get/syslogs")
    public ResponseEntity<Page<SysLog>> getSyslogs(@RequestParam int pageNumber,
                                                   @RequestParam int pageSize) {
        return new ResponseEntity<>(sysLogService.getAllSysLogs(pageNumber, pageSize), HttpStatus.OK);
    }

    @PostMapping("/filter/syslogs")
    public ResponseEntity<FilterSysLogByCategoriesResponse> getFilteredSyslogs(@RequestParam int pageNumber,
                                                                               @RequestParam int pageSize,
                                                                               @Valid @RequestBody FilterSysLogRequest request) throws ParseException {
        return new ResponseEntity<>(sysLogService.getFilterSysLogs(pageNumber, pageSize, request), HttpStatus.OK);
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

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                            @RequestParam("fileType") String fileType
                                            ) throws ParseException {
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

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(csvContent);
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
