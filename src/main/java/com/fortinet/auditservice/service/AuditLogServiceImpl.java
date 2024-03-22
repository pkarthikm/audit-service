package com.fortinet.auditservice.service;

import com.fortinet.auditservice.entity.AuditLogEntity;
import com.fortinet.auditservice.enums.Status;
import com.fortinet.auditservice.interfaces.AuditLogService;
import com.fortinet.auditservice.model.AuditLog;
import com.fortinet.auditservice.repository.AuditLogRepository;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuditLogServiceImpl implements AuditLogService {
    private static final Logger LOG = LogManager.getLogger(AuditLogServiceImpl.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    private AuditLog auditLogFromEntity(@NonNull AuditLogEntity auditLogEntity) {
        Long id = auditLogEntity.getId();
        if( null == id || id <= 0L)
            return null;

        Date timestamp = auditLogEntity.getTimestamp();
        if( null == timestamp)
            return null;
        String eventName = auditLogEntity.getEventName();
        if( null == eventName || eventName.isEmpty())
            return null;

        String tenantName = auditLogEntity.getTenantName();
        if( null == tenantName || tenantName.isEmpty())
            return null;

        String entityName = auditLogEntity.getEntityName();
        if( null == entityName || entityName.isEmpty())
            return null;

        Status status = auditLogEntity.getStatus();
        if( null == status)
            return null;

        String actorUserId = auditLogEntity.getActorUserId();
        if( null == actorUserId || actorUserId.isEmpty())
            return null;

        String actorSessionId = auditLogEntity.getActorSessionId();
        if( null == actorSessionId || actorSessionId.isEmpty())
            return null;

        String actorClient = auditLogEntity.getActorClient();

        String actorIpAddress = auditLogEntity.getActorIpAddress();

        Map<String, String> parameters = auditLogEntity.getParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> priorState = auditLogEntity.getPriorState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> resultingState = auditLogEntity.getResultingState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String apiPath = auditLogEntity.getApiPath();
        if( null == apiPath || apiPath.isEmpty())
            return null;

        String clusterId = auditLogEntity.getClusterId();
        String errorDescription = auditLogEntity.getErrorDescription();
        Integer errorStatusCode = auditLogEntity.getErrorStatusCode();
        Date created = auditLogEntity.getCreated();

        return AuditLog.builder()
                .id(id)
                .timestamp(timestamp)
                .eventName(eventName)
                .tenantName(tenantName)
                .entityName(entityName)
                .status(status)
                .actorUserId(actorUserId)
                .actorSessionId(actorSessionId)
                .actorClient(actorClient)
                .actorIpAddress(actorIpAddress)
                .parameters(parameters)
                .priorState(priorState)
                .resultingState(resultingState)
                .apiPath(apiPath)
                .clusterId(clusterId)
                .errorDescription(errorDescription)
                .errorStatusCode(errorStatusCode)
                .created(created)
                .build();
    }

    @Override
    public Page<AuditLog> getAuditLogs(@NonNull Pageable pageable) {
        Page<AuditLogEntity> pagedResult = auditLogRepository.findAll(pageable);
        if (pagedResult.hasContent()) {
            Page<AuditLog> auditLogPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::auditLogFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, auditLogPage);
            return auditLogPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("timestamp").descending())); // Regression Spring Boot 3.2.0
    }

    @Override
    public void save(@NonNull AuditLog auditLog) throws IllegalArgumentException {
        Date timestamp = auditLog.getTimestamp();

        String eventName = auditLog.getEventName();
        if(eventName.isEmpty())
            throw new IllegalArgumentException("Event Name cannot be empty");

        String tenantName = auditLog.getTenantName();
        if(tenantName.isEmpty())
            throw new IllegalArgumentException("Tenant Name cannot be empty");

        String entityName = auditLog.getEntityName();
        if(entityName.isEmpty())
            throw new IllegalArgumentException("Entity Name cannot be empty");

        Status status = auditLog.getStatus();
        String actorUserId = auditLog.getActorUserId();
        if(actorUserId.isEmpty())
            throw new IllegalArgumentException("Actor User Id cannot be empty");

        String actorSessionId = auditLog.getActorSessionId();
        if(actorSessionId.isEmpty())
            throw new IllegalArgumentException("Actor Session Id cannot be empty");

        String actorClient = auditLog.getActorClient();

        String actorIpAddress = auditLog.getActorIpAddress();

        Map<String, String> parameters = auditLog.getParameters().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> priorState = auditLog.getPriorState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Map<String, String> resultingState = auditLog.getResultingState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        String apiPath = auditLog.getApiPath();
        if(apiPath.isEmpty())
            throw new IllegalArgumentException("API Path cannot be empty");

        String clusterId = auditLog.getClusterId();
        String errorDescription = auditLog.getErrorDescription();
        Integer errorStatusCode = auditLog.getErrorStatusCode();
        Date created = auditLog.getCreated();

        AuditLogEntity auditLogEntity = auditLogRepository.save(AuditLogEntity.builder()
                .timestamp(timestamp)
                .eventName(eventName)
                .tenantName(tenantName)
                .entityName(entityName)
                .status(status)
                .actorUserId(actorUserId)
                .actorSessionId(actorSessionId)
                .actorClient(actorClient)
                .actorIpAddress(actorIpAddress)
                .parameters(parameters)
                .priorState(priorState)
                .resultingState(resultingState)
                .apiPath(apiPath)
                .clusterId(clusterId)
                .errorDescription(errorDescription)
                .errorStatusCode(errorStatusCode)
                .created(created)
                .build());
    }

    @Override
    public AuditLog getAuditLogById(Long id) {
        if(id <= 0L)
            return null;
        Optional<AuditLogEntity> auditLogEntity = auditLogRepository.findById(id);
        return auditLogEntity.map(this::auditLogFromEntity).orElse(null);
    }

    @Override
    public Page<AuditLog> getAuditLogsByEventName(@NonNull String eventName, Pageable pageable) {
        Page<AuditLogEntity> pagedResult = auditLogRepository.findAllByEventName(eventName, pageable);
        if (pagedResult.hasContent()) {
            Page<AuditLog> auditLogPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::auditLogFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, auditLogPage);
            return auditLogPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("timestamp").descending())); // Regression Spring Boot 3.2.0
    }

    @Override
    public Page<AuditLog> getAuditLogsByEntityName(@NonNull String entityName, Pageable pageable) {
        Page<AuditLogEntity> pagedResult = auditLogRepository.findAllByEntityName(entityName, pageable);
        if (pagedResult.hasContent()) {
            Page<AuditLog> auditLogPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::auditLogFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, auditLogPage);
            return auditLogPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("timestamp").descending())); // Regression Spring Boot 3.2.0
    }

    @Override
    public Page<AuditLog> getAuditLogsByTenantName(@NonNull String tenantName, Pageable pageable) {
        Page<AuditLogEntity> pagedResult = auditLogRepository.findAllByTenantName(tenantName, pageable);
        if (pagedResult.hasContent()) {
            Page<AuditLog> auditLogPage = new PageImpl<>(pagedResult.stream().sequential()
                    .map(this::auditLogFromEntity)
                    .collect(Collectors.toList()), pagedResult.getPageable(), pagedResult.getTotalElements());
            BeanUtils.copyProperties(pagedResult, auditLogPage);
            return auditLogPage;
        }
        //return Page.empty();
        return Page.empty(PageRequest.of(0, 10, Sort.by("timestamp").descending())); // Regression Spring Boot 3.2.0
    }
}
