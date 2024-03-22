package com.fortinet.auditservice.interfaces;

import com.fortinet.auditservice.model.AuditLog;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AuditLogService {
    Page<AuditLog> getAuditLogs(@NonNull Pageable pageable);
    void save(@NonNull AuditLog auditLog) throws IllegalArgumentException;
    AuditLog getAuditLogById(Long id);

    Page<AuditLog> getAuditLogsByEventName(@NonNull String eventName, Pageable pageable);

    Page<AuditLog> getAuditLogsByEntityName(@NonNull String entityName, Pageable pageable);

    Page<AuditLog> getAuditLogsByTenantName(@NonNull String tenantName, Pageable pageable);
}
