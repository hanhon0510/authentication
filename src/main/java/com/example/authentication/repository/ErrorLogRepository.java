package com.example.authentication.repository;

import com.example.authentication.model.ErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, String> {
}
