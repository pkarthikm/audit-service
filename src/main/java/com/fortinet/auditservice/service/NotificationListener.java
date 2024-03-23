package com.fortinet.auditservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortinet.auditservice.interfaces.AuditLogService;
import com.fortinet.auditservice.model.AuditLog;
import com.fortinet.auditservice.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class NotificationListener {
    private static final Logger LOG = LogManager.getLogger(NotificationListener.class);

    @Autowired
    private AuditLogService auditLogService;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = "${message.topic.name}", groupId = "audit-service", containerFactory = "auditServiceKafkaListenerContainerFactory")
    public void listenGroupFoo(String message) {
        LOG.info("Received Message in group 'audit-service': " + message);
        Event event = null;
        try {
            event = objectMapper.readValue(message, Event.class);
        } catch (JsonProcessingException e) {
            LOG.error("Json Processing Exception while converting message in notification to event", e);
            return;
        }
        if(event == null) {
            LOG.error("Event is null");
            return;
        }
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
        latch.countDown();
    }
}
