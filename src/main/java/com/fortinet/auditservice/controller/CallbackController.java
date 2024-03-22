package com.fortinet.auditservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortinet.auditservice.interfaces.AuditLogService;
import com.fortinet.auditservice.model.AuditLog;
import com.fortinet.auditservice.model.Event;
import com.fortinet.auditservice.model.Response;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/callback")
public class CallbackController {

    private static final Logger LOG = LogManager.getLogger(CallbackController.class);
    private static final Logger auditLogger = LogManager.getLogger("auditLogger");

    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    AuditLogService auditLogService;

    @PostMapping(value = "/publish")
    @ResponseBody
    public ResponseEntity<Response> publish(@Valid @NonNull @RequestBody final Event event) throws JsonProcessingException {
        LOG.info(event.toString());
        auditLogService.save(AuditLog.builder()
                .timestamp(event.getTimestamp())
                .eventName(event.getEventName())
                .tenantName(event.getTenantName())
                .entityName(event.getEntityName())
                .status(event.getStatus())
                .actorUserId(event.getActorUserId())
                .actorSessionId(event.getActorSessionId())
                .actorClient(event.getActorClient())
                .actorIpAddress(event.getActorIpAddress())
                .parameters(event.getParameters())
                .priorState(event.getPriorState())
                .resultingState(event.getResultingState())
                .apiPath(event.getApiPath())
                .clusterId(event.getClusterId())
                .errorDescription(event.getErrorDescription())
                .errorStatusCode(event.getErrorStatusCode())
                .build());
        return new ResponseEntity<>(Response.builder().message("Successfully processed notification").build(), HttpStatus.OK);
    }
}
