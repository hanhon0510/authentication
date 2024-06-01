package com.example.authentication.service;

import com.example.authentication.exception.AppException;
import com.example.authentication.model.SysLog;
import com.example.authentication.repository.SysLogRepository;
import com.example.authentication.request.SysLogDelRequest;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.SysLogDelResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
@Service
public class SysLogServiceImpl implements SysLogService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public List<Object[]> filterSysLogsByMonthAndYear(SysLogRequest request) throws ParseException {

        String startDateString = request.getStartDate();
        String endDateString = request.getEndDate();

        if (!isValidDate(startDateString) || !isValidDate(endDateString)) {
            throw new AppException(400,"Invalid date format or month range");
        }

        startDateString += "-01";
        endDateString += "-01";

        Date startDate = dateFormat.parse(startDateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateFormat.parse(endDateString));
        calendar.add(Calendar.MONTH, 1);
        Date endDate = calendar.getTime();

        return sysLogRepository.getFilteredSysLog(
                startDate,
                endDate,
                request.getMethod()
        );
    }

    @Override
    public SysLogDelResponse deleteSysLogsFromArg1toArg2(SysLogDelRequest request) {

        Long count = sysLogRepository.countSysLogs(request.getStartDate(), request.getEndDate());

        sysLogRepository.deleteSysLogs(request.getStartDate(), request.getEndDate());

        return SysLogDelResponse.builder()
                .count(count)
                .build();
    }

    @Override
    public List<SysLog> getSysLogs(SysLogDelRequest request) {

        return sysLogRepository.getSysLogs(request.getStartDate(), request.getEndDate());
    }

    private boolean isValidDate(String dateString) {
        String datePattern = "^(\\d{4})-(0[1-9]|1[0-2])$";
        return dateString.matches(datePattern);
    }
}
