package com.example.authentication.service;

import com.example.authentication.model.SysLog;

import java.io.OutputStream;
import java.util.List;

public interface PDFService {
    void writeSysLogsToPdf(OutputStream outputStream, List<SysLog> sysLogs);
}
