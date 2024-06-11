package com.example.authentication.repository;

import com.example.authentication.model.SysLog;
import com.example.authentication.request.FilterSysLogRequest;
import com.example.authentication.response.SysLogResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long> {

    @Query(name = "SysLog.getFilteredSysLogByMonthAndYear", nativeQuery = true)
    List<SysLogResponse> getFilteredSysLogByMonthAndYear(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("method") String method
    );

    @Query(value = "SELECT TOP (20) * " +
                "FROM SysLogs s " +
                "WHERE s.createdTime BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    List<SysLog> getSysLogs(@Param("startDate") LocalDate startDate,
                            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT count(*) " +
                "FROM SysLogs s " +
                "WHERE s.createdTime BETWEEN :startDate AND :endDate",
            nativeQuery = true)
    Long countSysLogs(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @Query(value = "DELETE s " +
            "FROM SysLogs s " +
            "WHERE s.createdTime BETWEEN :startDate AND :endDate",
        nativeQuery = true)
    void deleteSysLogs(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT * FROM SysLogs s WHERE " +
            "(s.createdTime BETWEEN :startDate AND :endDate) AND " +
            "(:className IS NULL OR s.class = :className) AND " +
            "(:method IS NULL OR s.method = :method) AND " +
            "(:httpMethod IS NULL OR s.httpMethod = :httpMethod) " +
            "ORDER BY s.createdTime " +
            "OFFSET :offset ROWS FETCH NEXT :pageSize ROWS ONLY",
            nativeQuery = true)
    List<SysLog> findSysLogsByCategories(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("className") String className,
            @Param("method") String methodName,
            @Param("httpMethod") String httpMethod,
            @Param("offset") int offset,
            @Param("pageSize") int pageSize
    );
}
