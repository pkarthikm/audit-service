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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private static final Logger LOG = LogManager.getLogger(NotificationController.class);

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value(value = "${message.topic.name}")
    private String topicName;

    @PostMapping(value = "/publish")
    @ResponseBody
    public ResponseEntity<Response> publish(@Valid @NonNull @RequestBody final Event event) throws JsonProcessingException {
        LOG.info(event.toString());
        String eventStr = objectMapper.writeValueAsString(event);
        kafkaTemplate.send(topicName, eventStr);

        return new ResponseEntity<>(Response.builder().message("Successfully published notification").build(), HttpStatus.OK);
    }
}
