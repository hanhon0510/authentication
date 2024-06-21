package com.example.authentication.repository;

import com.example.authentication.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {

    @Query(value = """
            SELECT COUNT(*)
            FROM app_errorLogs e
            WHERE e.date BETWEEN :startDate AND :endDate
            """, nativeQuery = true)
    int countErrorLogInADay(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );
}
