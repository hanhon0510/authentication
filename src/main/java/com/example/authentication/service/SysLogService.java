package com.example.authentication.service;

import com.example.authentication.model.SysLog;
import com.example.authentication.request.FilterSysLogRequest;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.FilterSysLogByCategoriesResponse;
import com.example.authentication.response.SysLogDelResponse;
import com.example.authentication.response.SysLogResponse;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface SysLogService {

    List<SysLogResponse> filterSysLogsByMonthAndYear(SysLogRequest request) throws ParseException;

    SysLogDelResponse deleteSysLogsFromArg1toArg2(SysLogDelRequest request) throws ParseException;

    List<SysLog> getSysLogs(SysLogDelRequest request) throws ParseException;
    Page<SysLog> getAllSysLogs(int pageNumber, int pageSize);

    FilterSysLogByCategoriesResponse getFilterSysLogs(int pageNumber, int pageSize, FilterSysLogRequest request)
            throws ParseException;
}
