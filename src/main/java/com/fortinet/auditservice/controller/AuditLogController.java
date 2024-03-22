package com.fortinet.auditservice.controller;

import com.fortinet.auditservice.interfaces.AuditLogService;
import com.fortinet.auditservice.model.AuditLog;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit-log")
public class AuditLogController {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogController.class);
    @Autowired
    AuditLogService auditLogService;

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<AuditLog>> getAuditLogs(@PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogs(pageable));
    }

    @GetMapping(value = "/id/{id}")
    @ResponseBody
    public ResponseEntity<AuditLog> getAuditLogById(@NonNull @PathVariable Long id) {
        AuditLog auditLog = auditLogService.getAuditLogById(id);
        if (auditLog == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(auditLog);
    }

    @GetMapping(value = "/event/{eventName}")
    @ResponseBody
    public ResponseEntity<Page<AuditLog>> getAuditLogsByEventName(@NonNull @PathVariable String eventName, @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEventName(eventName, pageable));
    }

    @GetMapping(value = "/entity/{entityName}")
    @ResponseBody
    public ResponseEntity<Page<AuditLog>> getAuditLogsByEntityName(@NonNull @PathVariable String entityName, @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByEntityName(entityName, pageable));
    }

    @GetMapping(value = "/tenant/{tenantName}")
    @ResponseBody
    public ResponseEntity<Page<AuditLog>> getAuditLogsByTenantName(@NonNull @PathVariable String tenantName, @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(auditLogService.getAuditLogsByTenantName(tenantName, pageable));
    }
}
