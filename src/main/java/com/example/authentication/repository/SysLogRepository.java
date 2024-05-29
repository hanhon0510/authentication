package com.example.authentication.repository;

import com.example.authentication.model.SysLog;
import com.example.authentication.response.SysLogResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface SysLogRepository extends JpaRepository<SysLog, Long> {
    @Query("SELECT new com.example.authentication.response.SysLogResponse(FORMAT(s.createdTime, 'yyyy/MM'), COUNT(s)) " +
            "FROM SysLog s " +
            "WHERE s.createdTime >= :startDate " +
            "AND s.createdTime < :endDate " +
            "AND s.method = :method " +
            "GROUP BY FORMAT(s.createdTime, 'yyyy/MM') " +
            "ORDER BY FORMAT(s.createdTime, 'yyyy/MM')")
    List<SysLogResponse> getFilteredSysLog(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate,
            @Param("method") String method
    );
}
