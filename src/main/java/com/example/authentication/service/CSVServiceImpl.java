package com.example.authentication.service;

import com.example.authentication.model.SysLog;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

@Service
public class CSVServiceImpl implements CSVService{

    public void writeSysLogsToCsv(Writer writer, List<SysLog> sysLogs) throws IOException {
        try (CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("CreatedTime", "SysLogId", "method"))) {
            for (SysLog sysLog : sysLogs) {
                csvPrinter.printRecord(sysLog.getCreatedTime(), sysLog.getSysLogId(), sysLog.getMethod());
            }
        }
    }
}
