package com.example.authentication.service;

import com.example.authentication.model.SysLog;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public interface CSVService {
    void writeSysLogsToCsv(Writer writer, List<SysLog> sysLogs) throws IOException;
}
