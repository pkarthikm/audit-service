package com.fortinet.auditservice.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class NotificationListener {
    private static final Logger LOG = LogManager.getLogger(NotificationListener.class);

    private final CountDownLatch latch = new CountDownLatch(3);

    @KafkaListener(topics = "${message.topic.name}", groupId = "audit-service", containerFactory = "auditServiceKafkaListenerContainerFactory")
    public void listenGroupFoo(String message) {
        LOG.info("Received Message in group 'audit-service': " + message);
        latch.countDown();
    }
}
