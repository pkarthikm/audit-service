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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/audit-log")
public class AuditLogController {

    private static final Logger LOG = LoggerFactory.getLogger(AuditLogController.class);
    @Autowired
    private AuditLogService auditLogService;

    @GetMapping(value = "/")
    @ResponseBody
    public ResponseEntity<Page<AuditLog>> getAuditLogs(@PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        LOG.info("User: " + auth.getName() + " is fetching audit logs");
        auth.getAuthorities().forEach(a -> LOG.info("User: " + auth.getName() + " has role: " + a.getAuthority()));
        if (auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.ok(auditLogService.getAuditLogs(pageable));
        } else if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER"))) {
            return ResponseEntity.ok(auditLogService.getAuditLogsByEntityName("PlayBook", pageable));
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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
