package com.example.authentication.service;

import com.example.authentication.model.SysLog;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.SysLogDelResponse;
import com.example.authentication.response.SysLogResponse;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

public interface SysLogService {

    List<Object[]> filterSysLogsByMonthAndYear(SysLogRequest request) throws ParseException;

    SysLogDelResponse deleteSysLogsFromArg1toArg2(SysLogDelRequest request) throws ParseException;

    List<SysLog> getSysLogs();
}