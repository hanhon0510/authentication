package com.example.authentication.repository;

import com.example.authentication.model.SysLog;
import com.example.authentication.response.SysLogResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long> {

    @Query(value = "WITH MonthSeries AS ( " +
                    "    SELECT FORMAT(CAST(:startDate AS DATE), 'yyyy/MM') AS yearMonth " +
                    "    UNION ALL " +
                    "    SELECT FORMAT(DATEADD(MONTH, 1, CAST(yearMonth + '/01' AS DATE)), 'yyyy/MM') " +
                    "    FROM MonthSeries " +
                    "    WHERE DATEADD(MONTH, 1, CAST(yearMonth + '/01' AS DATE)) <= :endDate " +
                    ") " +
                    "SELECT ms.yearMonth AS yearMonth, " +
                    "CASE WHEN COUNT(*) = 1 THEN NULL ELSE COUNT(*) END AS count " +
                    "FROM MonthSeries ms " +
                    "LEFT JOIN SysLogs s ON FORMAT(s.createdTime, 'yyyy/MM') = ms.yearMonth " +
                    "AND s.createdTime >= :startDate " +
                    "AND s.createdTime < :endDate " +
                    "AND s.method = :method " +
                    "GROUP BY ms.yearMonth " +
                    "ORDER BY ms.yearMonth " +
                    "OPTION (MAXRECURSION 0)",
            nativeQuery = true)
    List<Object[]> getFilteredSysLog(
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
}
