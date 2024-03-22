package com.fortinet.auditservice.repository;

import com.fortinet.auditservice.entity.AuditLogEntity;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditLogRepository extends JpaRepository<AuditLogEntity, Long> {

    @NonNull
    Page<AuditLogEntity> findAll(@NonNull Pageable page);
    @NonNull
    Page<AuditLogEntity> findAllByEventName(@NonNull String eventName, @NonNull Pageable page);

    @NonNull
    Page<AuditLogEntity> findAllByEntityName(@NonNull String entityName, @NonNull Pageable page);

    @NonNull
    Page<AuditLogEntity> findAllByTenantName(@NonNull String entityName, @NonNull Pageable page);

}
