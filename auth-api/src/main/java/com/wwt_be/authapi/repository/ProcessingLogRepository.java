package com.wwt_be.authapi.repository;

import com.wwt_be.authapi.entity.ProcessingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProcessingLogRepository extends JpaRepository<ProcessingLog, UUID> {

}
