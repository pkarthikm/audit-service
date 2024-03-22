package com.fortinet.auditservice.service;

import com.fortinet.auditservice.entity.AuditLogEntity;
import com.fortinet.auditservice.enums.Status;
import com.fortinet.auditservice.interfaces.AuditLogService;
import com.fortinet.auditservice.interfaces.ConversionService;
import com.fortinet.auditservice.model.AuditLog;
import com.fortinet.auditservice.repository.AuditLogRepository;
import jakarta.transaction.Transactional;
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

    @Autowired
    private ConversionService conversionService;

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

        String statusStr = auditLogEntity.getStatus();
        if( null == statusStr || statusStr.isEmpty())
            return null;
        Status status = null;
        try {
            status = Status.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            LOG.error("Invalid Status value: " + statusStr);
            return null;
        }

        String actorUserId = auditLogEntity.getActorUserId();
        if( null == actorUserId || actorUserId.isEmpty())
            return null;

        String actorSessionId = auditLogEntity.getActorSessionId();
        if( null == actorSessionId || actorSessionId.isEmpty())
            return null;

        String actorClient = auditLogEntity.getActorClient();

        String actorIpAddress = auditLogEntity.getActorIpAddress();

        String parametersStr = auditLogEntity.getParameters();
        Map<String, String> parameters = conversionService.convertStringToStringStringMap(parametersStr);
        String priorStateStr = auditLogEntity.getPriorState();
        Map<String, String> priorState = conversionService.convertStringToStringStringMap(priorStateStr);
        String resultingStateStr = auditLogEntity.getResultingState();
        Map<String, String> resultingState = conversionService.convertStringToStringStringMap(resultingStateStr);

        String apiPath = auditLogEntity.getApiPath();
        if( null == apiPath || apiPath.isEmpty())
            return null;

        String clusterId = auditLogEntity.getClusterId();
        String errorDescription = auditLogEntity.getErrorDescription();
        String errorStatusCodeStr = auditLogEntity.getErrorStatusCode();
        Integer errorStatusCode = null;
        if(null != errorStatusCodeStr && !errorStatusCodeStr.isEmpty())
            errorStatusCode = Integer.valueOf(errorStatusCodeStr);
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
    @Transactional
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
        String parametersStr = conversionService.convertStringStringMapToString(parameters);
        Map<String, String> priorState = auditLog.getPriorState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String priorStateStr = conversionService.convertStringStringMapToString(priorState);
        Map<String, String> resultingState = auditLog.getResultingState().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String resultingStateStr = conversionService.convertStringStringMapToString(resultingState);

        String apiPath = auditLog.getApiPath();
        if(apiPath.isEmpty())
            throw new IllegalArgumentException("API Path cannot be empty");

        String clusterId = auditLog.getClusterId();
        String errorDescription = auditLog.getErrorDescription();
        Integer errorStatusCode = auditLog.getErrorStatusCode();
        String errorStatusCodeStr = null;
        if (null != errorStatusCode)
            errorStatusCodeStr = errorStatusCode.toString();
        Date created = auditLog.getCreated();

        auditLogRepository.save(AuditLogEntity.builder()
                .timestamp(timestamp)
                .eventName(eventName)
                .tenantName(tenantName)
                .entityName(entityName)
                .status(status.name())
                .actorUserId(actorUserId)
                .actorSessionId(actorSessionId)
                .actorClient(actorClient)
                .actorIpAddress(actorIpAddress)
                .parameters(parametersStr)
                .priorState(priorStateStr)
                .resultingState(resultingStateStr)
                .apiPath(apiPath)
                .clusterId(clusterId)
                .errorDescription(errorDescription)
                .errorStatusCode(errorStatusCodeStr)
                .created(created)
                .build());
    }

    @Override
    @Transactional
    public AuditLog getAuditLogById(Long id) {
        if(id <= 0L)
            return null;
        Optional<AuditLogEntity> auditLogEntity = auditLogRepository.findById(id);
        return auditLogEntity.map(this::auditLogFromEntity).orElse(null);
    }

    @Override
    @Transactional
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
    @Transactional
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
    @Transactional
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
