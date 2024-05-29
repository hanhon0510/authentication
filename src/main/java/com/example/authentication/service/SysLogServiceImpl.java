package com.example.authentication.service;

import com.example.authentication.exception.AppException;
import com.example.authentication.model.SysLog;
import com.example.authentication.repository.SysLogRepository;
import com.example.authentication.request.SysLogRequest;
import com.example.authentication.response.SysLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.text.SimpleDateFormat;
@Service
public class SysLogServiceImpl implements SysLogService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private SysLogRepository sysLogRepository;

    @Override
    public List<SysLogResponse> filterSysLogsByMonthAndYear(SysLogRequest request) throws ParseException {

        String startDateString = request.getStartYear() + "-" + String.format("%02d", request.getStartMonth()) + "-01";
        String endDateString = request.getEndYear() + "-" + String.format("%02d", request.getEndMonth() + 1) + "-01";

        Date startDate = dateFormat.parse(startDateString);
        Date endDate = dateFormat.parse(endDateString);

        System.out.println(startDateString);

        return sysLogRepository.getFilteredSysLog(
                startDate,
                endDate,
                request.getMethod()
        );

    }
}
